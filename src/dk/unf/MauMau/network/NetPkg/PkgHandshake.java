package dk.unf.MauMau.network.NetPkg;

/**
 * Created by sdc on 7/16/14.
 */
public class PkgHandshake implements NetPkg {

    @Override
    public int getType() {
        return PKG_HANDSHAKE;
    }

    @Override
    public String serialize() {
        return "0"+getType();
    }
}
