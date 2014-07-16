package dk.unf.MauMau.network.NetPkg;

/**
 * Created by sdc on 7/16/14.
 */
public class PkgAllowedThrows implements NetPkg {

    public final int allowedCardsCount;
    public final int[] cardIds;

    public PkgAllowedThrows(int[] cardIds) {
        allowedCardsCount = cardIds.length;
        this.cardIds = cardIds;
    }

    @Override
    public int getType() {
        return PKG_ALLOWED_THROWS;
    }

    @Override
    public String serialize() {
        return null;
    }
}
