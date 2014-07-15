package dk.unf.MauMau;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Stack;

/**
 * Created by sdc on 7/15/14.
 */
public class Game {
    Stack<Card> deck = new Stack<Card>();
    Stack<Card> playedCards = new Stack<Card>();

    public Game(Stack deck, Stack<Card> playedCards) {
        this.deck = deck;
        this.playedCards = playedCards;
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
