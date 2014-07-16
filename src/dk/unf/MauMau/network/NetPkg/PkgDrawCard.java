package dk.unf.MauMau.network.NetPkg;

import android.util.Log;
import dk.unf.MauMau.game.Card;

/**
 * Created by sdc on 7/16/14.
 */
public class PkgDrawCard implements NetPkg {

    public final Card card;

    public PkgDrawCard(Card card) {
        this.card = card;
    }

    public PkgDrawCard(String input) {
        String[] parts = input.split(":");
        if (parts.length == 2) {
            int cardValue = Integer.parseInt(parts[0]);
            int color = Integer.parseInt(parts[1]);
            card = new Card(cardValue,color);
        } else {
            Log.e("Mau", "Invalid PkgConnect " + input);
            card = new Card(0,0);
        }
    }

    @Override
    public int getType() {
        return NetPkg.PKG_DRAW_CARD;
    }

    @Override
    public String serialize() {
        return "0" + getType() + card.cardValue + ":" + card.color;
    }
}
