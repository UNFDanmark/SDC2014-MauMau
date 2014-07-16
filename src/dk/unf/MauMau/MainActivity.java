package dk.unf.MauMau;

import android.app.Activity;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.text.format.Formatter;
import android.util.Log;
import android.view.MotionEvent;
import dk.unf.MauMau.network.Client;
import dk.unf.MauMau.network.NetPkg;
import dk.unf.MauMau.network.Server;

public class MainActivity extends Activity {

    private Client client;
    private Server server;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        BoardCanvas boardCanvas = new BoardCanvas(this);
        boardCanvas.init(getApplicationContext());
        setContentView(boardCanvas);

        WifiManager wifiManager = (WifiManager) getSystemService(WIFI_SERVICE);
        WifiInfo wifiInfo = wifiManager.getConnectionInfo();
        int ip = wifiInfo.getIpAddress();
        String ipAdress = Formatter.formatIpAddress(ip);
        Log.i("Mau", ipAdress);


                //True for server , false for client
        if (true) {
        } else {
            client = new Client();
            new Thread(new Runnable() {
                @Override
                public void run() {
                    client.connect();
                    while (client.isConnected()) {
                        client.tick();
                        try {
                            java.lang.Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }).start();
            NetPkg pkg = new NetPkg(NetPkg.PKG_CONNECT);
            pkg.addString("Ello Server!");
            client.send(pkg);

        }

    }

    @Override
    protected void onPause() {
        super.onPause();
        if (client != null) {
            client.close();
        }

        if (server != null) {
            server.close();
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        int action = event.getActionMasked();

        switch (action) {
            case MotionEvent.ACTION_DOWN: Log.i("Mau", "Detected down"); return true;
            case MotionEvent.ACTION_MOVE: Log.i("Mau", "Detected move"); return true;
            case MotionEvent.ACTION_UP: Log.i("Mau", "Detected up"); return true;
            default: return super.onTouchEvent(event);
        }
    }
}
