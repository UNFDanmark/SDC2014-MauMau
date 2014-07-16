package dk.unf.MauMau.game;

import android.util.Log;
import dk.unf.MauMau.network.NetListener;
import dk.unf.MauMau.network.NetPkg;
import dk.unf.MauMau.network.Server;

import java.util.*;

/**
 * Created by sdc on 7/15/14.
 */
public class Game implements Runnable, NetListener {

    Server server;

    Stack<Card> deck = new Stack<Card>();
    Stack<Card> playedCards = new Stack<Card>();
    ArrayList<Card> cardsToGive = new ArrayList<Card>();
    Queue<Player> players = new PriorityQueue<Player>();



    public Queue<Player> getPlayers() {
        return players;
    }

    public Game(String ip) {
        server = new Server(ip);
        new Thread(server).start();

        for(int i = 6; i < 13; i++){
            for(int j = 0; j < 3; j++){
                deck.push(new Card(i, j, 0));
            }
        }
        Collections.shuffle(deck);
        for(int i = 0; i < 5; i++){
            cardsToGive.add(deck.pop());
        }
        players.add(new Player(cardsToGive));

    }

    public void giveCard(Player player) {
        player.cards.add(deck.pop());
        if(deck.empty()){
            Collections.reverse(deck);
            for(int i = 0; playedCards.size() != 1; i++){
                deck.push(playedCards.pop());
            }
            Collections.shuffle(deck);
        }
    }
    public void playCard(Card card){
        playedCards.push(card);
    }

    @Override
    public void received(NetPkg data) {
        switch (data.getType()) {
            default: Log.i("Mau", "Received unknown package of type: " + data.getType());
        }
    }

    @Override
    public void onTimeout() {

    }

    public void run() {

    }
}
