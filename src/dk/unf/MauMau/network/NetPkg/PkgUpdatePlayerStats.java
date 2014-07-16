package dk.unf.MauMau.network.NetPkg;

/**
 * Created by sdc on 7/16/14.
 */
public class PkgUpdatePlayerStats implements NetPkg {

    public final int playerCount;
    public final int id[];
    public final int cardCount[];

    public PkgUpdatePlayerStats(int playerCount, int id[], int cardCount[]) {
        this.playerCount = playerCount;
        this.id = id;
        this.cardCount = cardCount;
    }

    @Override
    public int getType() {
        return NetPkg.PKG_UPDATE_PLAYER_STATS;
    }

    @Override
    public String serialize() {
        return null;
    }

}
