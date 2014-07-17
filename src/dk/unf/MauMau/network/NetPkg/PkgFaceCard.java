package dk.unf.MauMau.network.NetPkg;

import android.util.Log;
import dk.unf.MauMau.game.Card;

/**
 * Created by sdc on 7/16/14.
 */
public class PkgFaceCard implements NetPkg{

    public final Card card;
    public final int nextPlayer;

    public PkgFaceCard(Card card, int nextPlayer) {
        this.card = card;
        this.nextPlayer = nextPlayer;
    }

    public PkgFaceCard(String input) {
        String[] parts = input.split(":");
        if (parts.length == 3) {
            int cardValue = Integer.parseInt(parts[0]);
            int color = Integer.parseInt(parts[1]);
            nextPlayer = Integer.parseInt(parts[2]);
            card = new Card(cardValue,color);
        } else {
            Log.e("Mau", "Invalid PkgFaceCard");
            card = new Card(0,0);
            nextPlayer = -1;
        }
    }

    @Override
    public int getType() {
        return NetPkg.PKG_FACE_CARD;
    }

    @Override
    public String serialize() {
        return "0" + getType() + card.cardValue + ":" + card.color + ":" + nextPlayer;
    }
}
