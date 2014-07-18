package dk.unf.MauMau.network.NetPkg;

/**
 * Created by sdc on 7/18/14.
 */
public class PkgWon implements NetPkg {

    public final int playerid;

    public PkgWon(int playerid) {
        this.playerid = playerid;
    }

    public PkgWon(String input) {
        this.playerid = Integer.parseInt(input);
    }

    @Override
    public int getType() {
        return PKG_WON;
    }

    @Override
    public String serialize() {
        return ("" + getType()) + playerid;
    }
}
