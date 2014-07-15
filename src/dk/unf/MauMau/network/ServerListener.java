package dk.unf.MauMau.network;

/**
 * Created by sdc on 7/15/14.
 */
public interface ServerListener extends NetListener {

    void onConnection();

    void onDisconnect();


}