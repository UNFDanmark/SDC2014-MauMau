package dk.unf.MauMau.network;

/**
 * Created by sdc on 7/15/14.
 */
public class NetPkg {

    public static final int PKG_CONNECT = 0;
    public static final int PKG_DISCONNECT = 1;

    private String data;
    private int type;

    public NetPkg(int type) {
        this.type = type;
    }

    public int getType() {
        return type;
    }

    public void addString(String string) {
        data = string;
    }

    public String serialize() {
        return data;
    }

}
