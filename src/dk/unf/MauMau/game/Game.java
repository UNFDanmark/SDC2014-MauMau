package dk.unf.MauMau.game;

import android.util.Log;
import dk.unf.MauMau.Settings;
import dk.unf.MauMau.network.NetListener;
import dk.unf.MauMau.network.NetPkg.*;
import dk.unf.MauMau.network.Server;

import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Created by sdc on 7/15/14.
 */
public class Game implements Runnable, NetListener {

    Server server;
    private volatile boolean running = true;

    private ArrayList<Card> deck = new ArrayList<Card>();
    private Card playedCard;
    private ArrayList<Player> players = new ArrayList<Player>();
    private Queue<NetPkg> pkgQueue = new ConcurrentLinkedQueue<NetPkg>();
    Queue<Player> playerQueue = new PriorityQueue<Player>();
    private boolean reversed;


    private int nextPlayerID = 0;
    private int currentPlayer = 0;

    public ArrayList<Player> getPlayers() {
        return players;
    }

    public Game(String ip) {
        server = new Server(ip);
        server.addListener(this);
        Thread serverThread = new Thread(server);
        serverThread.setName("ServerAcceptThread");
        serverThread.start();

        for (int i = 6; i < 14; i++) {
            for (int j = 0; j < 4; j++) {
                deck.add(new Card(i, j));
            }
        }
        Collections.shuffle(deck);
    }

    public void giveCards(Player player, int amount) {
        //if there are less cards in the deck than the amount of cards
        //to be picked up by player, the player is added to a queue and
        //will get cards when they're available
        if (deck.size() < amount) {
            playerQueue.add(player);
        } else {
            ArrayList<Card> cards = new ArrayList<Card>();
            while (amount > 0) {
                cards.add(deck.remove((int) (Math.random() * deck.size())));
                amount--;
            }
            for (Card card : cards) {
                server.sendPkg(new PkgDrawCard(card), player.getId());
                player.cards.add(card);
            }
        }

    }


    public Queue<Player> reverseOrder(Queue<Player> queue) {
        Stack<Player> stack = new Stack<Player>();
        while (queue.size() > 0) {
            stack.push(queue.peek());
            queue.remove();
        }
        while (stack.size() > 0) {
            queue.add(stack.pop());
        }
        return queue;
    }

    public void playCard(Card card) {
        playedCard = card;
    }


    @Override
    public synchronized void received(NetPkg data) {
        pkgQueue.add(data);
    }

    @Override
    public void onTimeout() {

    }

    public void run() {

        while (running) {

            NetPkg pkg;
            while (!pkgQueue.isEmpty()) {
                pkg = pkgQueue.poll();

                if (pkg.getType() == NetPkg.PKG_CONNECT) {
                    spawnNewPlayer(((PkgConnect) pkg).nickname);
                } else if (pkg.getType() == NetPkg.PKG_THROW_CARD) {
                    PkgThrowCard nPkg = (PkgThrowCard) pkg;
                    players.get(currentPlayer).cards.remove(nPkg.card);
                    throwCard(nPkg.card);
                } else if (pkg.getType() == NetPkg.PKG_START_GAME) {
                    startGame();
                } else if (pkg.getType() == NetPkg.PKG_SET_COLOR) {
                    playedCard = new Card(playedCard.cardValue,((PkgSetColor)pkg).color);
                    for (Player player : players) {
                        server.sendPkg(new PkgFaceCard(playedCard,currentPlayer),player.getId());
                    }
                    ArrayList<Card> throwables = throwableCards(players.get(currentPlayer));
                    if(throwables.size() == 0) {
                        while((throwables = throwableCards(players.get(currentPlayer))).size() == 0){
                            giveCards(players.get(currentPlayer), 1);
                            nextTurn();
                        }
                        for (Player player : players) {
                            server.sendPkg(new PkgNextTurn(currentPlayer),player.getId());
                        }
                    }
                    server.sendPkg(new PkgAllowedThrows(throwables),players.get(currentPlayer).getId());
                }
            }

            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        Log.i("Mau", "Game dying. Send help");
    }

    private void spawnNewPlayer(String nick) {
        Player newPlayer = new Player(nextPlayerID++, nick, new ArrayList<Card>());
        for (Player player : players) {
            server.sendPkg(new PkgConnect(newPlayer.getNick(), newPlayer.getId()), player.getId());
            server.sendPkg(new PkgConnect(player.getNick(), player.getId()), newPlayer.getId());
        }
        server.sendPkg(new PkgConnect("#", newPlayer.getId()), newPlayer.getId());
        players.add(newPlayer);
    }



    private void throwCard(Card card) {
        specialCards(card);

        playedCard = card;

        nextTurn();

        for (Player player : players) {
            server.sendPkg(new PkgFaceCard(card, currentPlayer), player.getId());
        }
        ArrayList<Card> throwables = throwableCards(players.get(currentPlayer));
        if(throwables.size() == 0) {
            while((throwables = throwableCards(players.get(currentPlayer))).size() == 0){
                giveCards(players.get(currentPlayer), 1);
                nextTurn();
            }
            for (Player player : players) {
                server.sendPkg(new PkgNextTurn(currentPlayer),player.getId());
            }
        }
        server.sendPkg(new PkgAllowedThrows(throwables),players.get(currentPlayer).getId());
    }

    private void specialCards (Card card){
        if(card.cardValue == 7 && !playerHasValue(players.get(getNextPlayer()),7)){
            giveCards(players.get(getNextPlayer()), 2);
            nextTurn();
        }else if(card.cardValue == 8 && !playerHasValue(players.get(getNextPlayer()),8)){
            nextTurn();
        } else if(playedCard.cardValue == 9 && Settings.usesCustomRules()) {
            reversed = true;
        }
    }

    private boolean playerHasValue(Player player, int value) {
        for (Card card : player.cards) {
            if (card.cardValue == value) {
                return true;
            }
        }
        return false;
    }

    private ArrayList<Card> throwableCards(Player player) {
        ArrayList<Card> throwableCards = new ArrayList<Card>();
        for (int i = 0; i < player.cards.size(); i++) {
            if (playedCard.cardValue == player.cards.get(i).cardValue ||
                    playedCard.color == player.cards.get(i).color) {
                if (playedCard.cardValue == 11 && player.cards.get(i).cardValue == 11) {
                    continue;
                }
                throwableCards.add(player.cards.get(i));
            }
        }
        return throwableCards;
    }

    private int getNextPlayer() {
        if (!reversed) {
            if (currentPlayer == players.size() - 1) {
                return 0;
            } else {
                return currentPlayer+1;
            }
        } else {
            if (currentPlayer == 0) {
                return players.size() - 1;
            } else {
                return currentPlayer;
            }
        }
    }

    private int nextTurn() {
        if (!reversed) {
            if (currentPlayer == players.size() - 1) {
                currentPlayer = 0;
                return 0;
            } else {
                currentPlayer++;
                return currentPlayer;
            }
        } else {
            if (currentPlayer == 0) {
                currentPlayer = players.size() - 1;
                return players.size() - 1;
            } else {
                currentPlayer--;
                return currentPlayer;
            }
        }
    }

    private void startGame() {
        playedCard = deck.remove((int) (Math.random() * deck.size()));
        for (Player player : players) {
            server.sendPkg(new PkgStartGame(), player.getId());
            giveCards(player, 5);
            server.sendPkg(new PkgFaceCard(playedCard, players.get(0).getId()), player.getId());
        }
        ArrayList<Card> throwables = throwableCards(players.get(currentPlayer));
        if(throwables.size() == 0) {
            while((throwables = throwableCards(players.get(currentPlayer))).size() == 0){
                giveCards(players.get(currentPlayer), 1);
                nextTurn();
            }
            for (Player player : players) {
                server.sendPkg(new PkgNextTurn(currentPlayer),player.getId());
            }
        }
        server.sendPkg(new PkgAllowedThrows(throwables),players.get(currentPlayer).getId());
    }

    public synchronized void stopGame() {
        running = false;
        server.close();
    }
}
