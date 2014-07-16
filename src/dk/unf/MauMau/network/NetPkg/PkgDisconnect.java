package dk.unf.MauMau.network.NetPkg;

/**
 * Created by sdc on 7/16/14.
 */
public class PkgDisconnect implements NetPkg {

    public final int id;

    public PkgDisconnect() {
        id = -1;
    }

    public PkgDisconnect(int id) {
        this.id = id;
    }

    @Override
    public int getType() {
        return NetPkg.PKG_DISCONNECT;
    }

    @Override
    public String serialize() {
        return null;
    }
}
