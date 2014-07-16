package dk.unf.MauMau.network.NetPkg;

/**
 * Created by sdc on 7/16/14.
 */
public class PkgThrowCard implements NetPkg {

    public final int cardId;

    public PkgThrowCard(int cardId) {
        this.cardId = cardId;
    }

    @Override
    public int getType() {
        return NetPkg.PKG_THROW_CARD;
    }

    @Override
    public String serialize() {
        return null;
    }
}
