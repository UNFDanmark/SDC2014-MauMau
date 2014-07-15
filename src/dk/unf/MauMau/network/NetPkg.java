package dk.unf.MauMau.network;

/**
 * Created by sdc on 7/15/14.
 */
public class NetPkg {

    private String data;

    public NetPkg() {

    }

    public void addString(String string) {
        data = string;
    }

    public String serialize() {
        return data;
    }

}
