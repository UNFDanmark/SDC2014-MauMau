package dk.unf.MauMau;

import java.util.*;

/**
 * Created by sdc on 7/15/14.
 */
public class Game {
    Stack<Card> deck = new Stack<Card>();
    Stack<Card> playedCards = new Stack<Card>();
    ArrayList<Card> cardsToGive = new ArrayList<Card>();
    Queue<Player> players = new PriorityQueue<Player>();

    public Queue<Player> getPlayers() {
        return players;
    }

    public Game() {
        this.deck = deck;
        this.playedCards = playedCards;
        this.players = players;
        this.cardsToGive = cardsToGive;

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

}
