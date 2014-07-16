package dk.unf.MauMau;

import android.app.Activity;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.text.format.Formatter;
import android.util.Log;
import android.view.MotionEvent;
import android.view.Window;
import dk.unf.MauMau.network.Client;

public class MainActivity extends Activity {

    private Client client;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        CanvasManager canvasManager = new CanvasManager(this);
        canvasManager.init(getApplicationContext());
        setContentView(canvasManager);

        WifiManager wifiManager = (WifiManager) getSystemService(WIFI_SERVICE);
        WifiInfo wifiInfo = wifiManager.getConnectionInfo();
        int ip = wifiInfo.getIpAddress();
        String ipAdress = Formatter.formatIpAddress(ip);
        Log.i("Mau", ipAdress);


    }

    @Override
    protected void onPause() {
        super.onPause();
        if (client != null) {
            client.close();
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
