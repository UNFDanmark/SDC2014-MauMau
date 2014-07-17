package dk.unf.MauMau.ui;

import dk.unf.MauMau.game.Card;

/**
 * Created by sdc on 7/16/14.
 */
public class CardElement extends Element {
    int cardValue;
    int cardColor;


    public CardElement(int x, int y, int width, int height, int cardValue, int cardColor) {
        super(x, y, width, height);

        this.cardColor = cardColor;
        this.cardValue = cardValue;
    }

    public CardElement(Card card) {
        this(card.cardValue, card.color);
    }

    public CardElement(int value, int color) {
        super(0,0,0,0);

        cardColor = color;
        cardValue = value;
    }

    public Card toCard() {
        return new Card(cardValue,cardColor);
    }


    @Override
    public boolean equals(Object o) {
        if (o instanceof CardElement) {
            CardElement other = (CardElement) o;
            return (cardValue == other.cardValue && cardColor == other.cardColor);
        } else {
            return false;
        }
    }
}
