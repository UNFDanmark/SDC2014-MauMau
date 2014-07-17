package dk.unf.MauMau.network.NetPkg;

/**
 * Created by sdc on 7/17/14.
 */
public class PkgStartGame implements NetPkg {

    @Override
    public int getType() {
        return NetPkg.PKG_START_GAME;
    }

    @Override
    public String serialize() {
        return "0"+getType();
    }
}
