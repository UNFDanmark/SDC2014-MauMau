package dk.unf.MauMau.network.NetPkg;

/**
 * Created by sdc on 7/16/14.
 */
public class PkgNextTurn implements NetPkg {

    public final int playerId;

    public PkgNextTurn(int playerId) {
        this.playerId = playerId;
    }

    public PkgNextTurn(String input) {
        playerId = Integer.parseInt(input);
    }

    @Override
    public int getType() {
        return NetPkg.PKG_NEXT_TURN;
    }

    @Override
    public String serialize() {
        return "0"+getType() + playerId;
    }
}
