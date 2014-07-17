package dk.unf.MauMau.ui;

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
}
