package dk.unf.MauMau.network.NetPkg;

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

    @Override
    public int getType() {
        return NetPkg.PKG_FACE_CARD;
    }

    @Override
    public String serialize() {
        return null;
    }
}
