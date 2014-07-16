package dk.unf.MauMau.game;

import android.util.Log;
import dk.unf.MauMau.network.NetListener;
import dk.unf.MauMau.network.NetPkg.NetPkg;
import dk.unf.MauMau.network.NetPkg.PkgConnect;
import dk.unf.MauMau.network.NetPkg.PkgDrawCard;
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
    private Stack<Card> playedCards = new Stack<Card>();
    private ArrayList<Player> players = new ArrayList<Player>();
    private Queue<NetPkg> pkgQueue = new ConcurrentLinkedQueue<NetPkg>();

    private int nextPlayerID = 0;

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

    public void playCard(Card card){
        playedCards.push(card);
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
        ArrayList<Card> cards = getRandomHand(5);
        Player player = new Player(nextPlayerID++, nick, cards);
        players.add(player);
        for (Card card : cards) {
            server.sendPkg(new PkgDrawCard(card),player.getId());
        }
    }

    public synchronized void stopGame() {
        running = false;
        server.close();
    }
}
