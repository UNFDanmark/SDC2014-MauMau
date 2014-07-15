package dk.unf.MauMau.network;

/**
 * Created by sdc on 7/15/14.
 */
public interface NetListener {

    void recieved(NetPkg data);

    void onTimeout();

}
