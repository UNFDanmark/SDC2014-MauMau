package dk.unf.MauMau.game;

import android.util.Log;
import dk.unf.MauMau.network.NetListener;
import dk.unf.MauMau.network.NetPkg.NetPkg;
import dk.unf.MauMau.network.Server;

import java.util.*;

/**
 * Created by sdc on 7/15/14.
 */
public class Game implements Runnable, NetListener {

    Server server;
    private volatile boolean running = true;

    Stack<Card> deck = new Stack<Card>();
    Stack<Card> playedCards = new Stack<Card>();
    ArrayList<Card> cardsToGive = new ArrayList<Card>();
    Queue<Player> players = new PriorityQueue<Player>();
    Queue<Player> playerQueue = new PriorityQueue<Player>();


    public Queue<Player> getPlayers() {
        return players;
    }

    public Game(String ip) {
        server = new Server(ip);
        Thread serverThread = new Thread(server);
        serverThread.setName("ServerAcceptThread");
        serverThread.start();

        for (int i = 6; i < 13; i++) {
            for (int j = 0; j < 3; j++) {
                deck.push(new Card(i, j, 0));
            }
        }
        Collections.shuffle(deck);
        for (int i = 0; i < 5; i++) {
            cardsToGive.add(deck.pop());
        }
        players.add(new Player(cardsToGive));

    }

    public void giveCards(Player player, int amount) {
        //if there are less cards in the deck than the amount of cards
        //to be picked up by player, the player is added to a queue and
        //will get cards when they're available
        if (deck.size() < amount) {
            playerQueue.add(player);
        } else {
            while (amount > 0) {
                player.cards.add(deck.pop());
                amount--;
            }
        }
    }

    public void checkDeck() { //Checks if the deck is empty and if it is puts all the cards from played card except one into the deck
        if (deck.empty()) {
            Collections.reverse(playedCards);
            for (int i = 0; playedCards.size() != 1; i++) {
                deck.push(playedCards.pop());
            }
            Collections.shuffle(deck);
        }
    }

    public Queue<Player> reverseOrder(Queue<Player> queue) {
        Stack<Player> stack = new Stack<Player>();
        while(queue.size() > 0) {
            stack.push(queue.peek());
            queue.remove();
        }
        while(stack.size() > 0){
            queue.add(stack.pop());
        }
        return queue;
    }

    public void playCard(Card card) {
        playedCards.push(card);
    }


    @Override
    public synchronized void received(NetPkg data) {
        switch (data.getType()) {
            default:
                Log.i("Mau", "Server received unknown package of type: " + data.getType());
        }
    }

    @Override
    public void onTimeout() {

    }

    public void run() {

        while (running) {

        }

        Log.i("Mau", "Game dying");
    }

    public synchronized void stopGame() {
        running = false;
        server.close();
    }
}
