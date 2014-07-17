package dk.unf.MauMau.game;

/**
 * Created by sdc on 7/15/14.
 */
public class Card {
    public final int cardValue;
    public final int color;


    public Card(int cardValue, int color) {
        this.cardValue = cardValue;
        this.color = color;
    }


    @Override
    public boolean equals(Object o) {
        if (o instanceof Card) {
            Card c = (Card) o;
            if (c.color == -1) {
                return (c.cardValue == cardValue);
            } else {
                return (c.cardValue == cardValue && c.color == color);
            }
        } else {
            return false;
        }
    }
}
