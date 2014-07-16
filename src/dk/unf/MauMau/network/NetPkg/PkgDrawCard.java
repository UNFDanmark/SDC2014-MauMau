package dk.unf.MauMau.network.NetPkg;

import dk.unf.MauMau.game.Card;

/**
 * Created by sdc on 7/16/14.
 */
public class PkgDrawCard implements NetPkg {

    public final int cardId;
    public final Card card;

    public PkgDrawCard(int cardId, Card card) {
        this.cardId = cardId;
        this.card = card;
    }

    @Override
    public int getType() {
        return NetPkg.PKG_DRAW_CARD;
    }

    @Override
    public String serialize() {
        return null;
    }
}
