package dk.unf.MauMau.ui;

/**
 * Created by sdc on 7/16/14.
 */
public class CardElement extends Element {
    int cardValue;
    int cardColor;


    public CardElement(int id, int x, int y, int width, int height, int cardColor, int cardValue) {
        super(id, x, y, width, height);

        this.cardColor = cardColor;
        this.cardValue = cardValue;
    }
}
