package dk.unf.MauMau.game;

import android.util.Log;
import dk.unf.MauMau.network.NetListener;
import dk.unf.MauMau.network.NetPkg.*;
import dk.unf.MauMau.network.Server;

import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.SynchronousQueue;

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

        for(int i = 6; i < 13; i++){
            for(int j = 0; j < 3; j++){
                deck.add(new Card(i, j));
            }
        }
        Collections.shuffle(deck);
    }

    /*public void giveCard(Player player) {
        player.cards.add(deck.pop());
        if(deck.empty()){
            Collections.reverse(deck);
            for(int i = 0; playedCards.size() != 1; i++){
                deck.push(playedCards.pop());
            }
            Collections.shuffle(deck);
        }
    }*/

    private ArrayList<Card> getRandomHand(int size) {
        ArrayList<Card> hand = new ArrayList<Card>();
        for (int i = 0; i < size; i++) {
            hand.add(deck.remove((int)(Math.random() * deck.size())));
        }

        return hand;
    }

    @Override
    public synchronized void received(NetPkg data) {
       pkgQueue.add(data);
    }

    @Override
    public void onTimeout() {

    }

    public void run() {

        while(running) {

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
                }
            }

            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        Log.i("Mau", "Game dying");
    }

    private void spawnNewPlayer(String nick) {
        Player newPlayer = new Player(nextPlayerID++, nick, new ArrayList<Card>());
        for (Player player : players) {
            server.sendPkg(new PkgConnect(newPlayer.getNick(),newPlayer.getId()),player.getId());
            server.sendPkg(new PkgConnect(player.getNick(),player.getId()),newPlayer.getId());
        }
        server.sendPkg(new PkgConnect("#",newPlayer.getId()),newPlayer.getId());
        players.add(newPlayer);
    }

    private void throwCard(Card card) {
        for (Player player : players) {
            server.sendPkg(new PkgFaceCard(card,getNextPlayerID()),player.getId());
        }
    }

    private int getNextPlayerID() {
        if (currentPlayer == players.size()) {
            currentPlayer = 0;
            return 0;
        } else {
            currentPlayer++;
            return currentPlayer;
        }
    }

    private void startGame() {
        playedCard = deck.remove((int)(Math.random() * deck.size()));
        for (Player player : players) {
            server.sendPkg(new PkgStartGame(),player.getId());
            player.cards = getRandomHand(5);
            for (Card card : player.cards) {
                server.sendPkg(new PkgDrawCard(card),player.getId());
            }
            server.sendPkg(new PkgFaceCard(playedCard,players.get(0).getId()),player.getId());
        }
    }

    public synchronized void stopGame() {
        running = false;
        server.close();
    }
}
