package dk.unf.MauMau.ui;

import android.graphics.Canvas;
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
        super(0,0,0,0);

        cardColor = card.color;
        cardValue = card.cardValue;
    }

    public Card toCard() {
        return new Card(cardValue,cardColor);
    }

}
