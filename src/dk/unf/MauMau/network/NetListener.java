package dk.unf.MauMau.network;

import dk.unf.MauMau.network.NetPkg.NetPkg;

/**
 * Created by sdc on 7/15/14.
 */
public interface NetListener {

    void received(NetPkg data);

    void onTimeout();

}
