package dk.unf.MauMau.network.NetPkg;

import android.util.Log;

/**
 * Created by sdc on 7/16/14.
 */
public class PkgConnect implements NetPkg {

    public final boolean fromServer;
    public final String nickname;
    public final int id;

    public PkgConnect(String nickname, int id) {
        fromServer = id != -1;
        this.nickname = nickname;
        this.id = id;
    }

    public PkgConnect(String input) {
        String[] parts = input.split(":");
        if (parts.length == 3) {
            fromServer = Boolean.parseBoolean(parts[0]);
            nickname = parts[1];
            id = Integer.parseInt(parts[2]);
        } else if (parts.length == 2) {
            fromServer = Boolean.parseBoolean(parts[0]);
            nickname = parts[1];
            id = -1;
        } else {
            Log.e("Mau","Invalid PkgConnect " + input);
            fromServer = false;
            nickname = "";
            id = -1;
        }
    }

    @Override
    public int getType() {
        return NetPkg.PKG_CONNECT;
    }

    @Override
    public String serialize() {
        return null;
    }
}
