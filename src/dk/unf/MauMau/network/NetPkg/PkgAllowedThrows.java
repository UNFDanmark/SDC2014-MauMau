package dk.unf.MauMau.network.NetPkg;

import dk.unf.MauMau.game.Card;
import dk.unf.MauMau.ui.CardElement;

import java.util.ArrayList;

/**
 * Created by sdc on 7/16/14.
 */
public class PkgAllowedThrows implements NetPkg {

    public final int allowedCardsCount;
    public final int[] colors;
    public final int[] values;


    public PkgAllowedThrows(ArrayList<Card> card) {
        allowedCardsCount = card.size();
        colors = new int[allowedCardsCount];
        values = new int[allowedCardsCount];
        for (int i = 0; i < allowedCardsCount; i++) {
            colors[i] = card.get(i).color;
            values[i] = card.get(i).cardValue;
        }
    }

    public PkgAllowedThrows(String input) {
        String[] parts = input.split(":");
        allowedCardsCount = Integer.parseInt(parts[0]);
        int[] colorArray = new int[allowedCardsCount];
        int[] valueArray = new int [allowedCardsCount];
        if (parts.length == allowedCardsCount*2+1) {
            for (int i = 0; i < allowedCardsCount; i++) {
                colorArray[i] = Integer.parseInt(parts[i+1]);
            }
            for (int i = 0; i < allowedCardsCount; i++) {
                valueArray[i] = Integer.parseInt(parts[i+allowedCardsCount+1]);
            }
        }
        colors = colorArray;
        values = valueArray;
    }

    @Override
    public int getType() {
        return PKG_ALLOWED_THROWS;
    }

    @Override
    public String serialize() {
        StringBuilder builder = new StringBuilder();
        builder.append("0");
        builder.append(getType());
        builder.append(allowedCardsCount);
        for (int i = 0; i < allowedCardsCount; i++) {
            builder.append(":");
            builder.append(colors[i]);
        }
        for (int i = 0; i < allowedCardsCount; i++) {
            builder.append(":");
            builder.append(values[i]);
        }
        return builder.toString();
    }

    public ArrayList<Card> toArrayList() {
        ArrayList<Card> cards = new ArrayList<Card>();
        for (int i = 0; i < allowedCardsCount; i++) {
            cards.add(new Card(values[i],colors[i]));
        }
        return cards;
    }

    public ArrayList<CardElement> toElementArrayList() {
        ArrayList<CardElement> cards = new ArrayList<CardElement>();
        for (int i = 0; i < allowedCardsCount; i++) {
            cards.add(new CardElement(values[i],colors[i]));
        }
        return cards;
    }
}
