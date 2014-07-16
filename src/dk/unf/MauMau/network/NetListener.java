package dk.unf.MauMau.network;

/**
 * Created by sdc on 7/15/14.
 */
public interface NetListener {

    void received(NetPkg data);

    void onTimeout();

}
