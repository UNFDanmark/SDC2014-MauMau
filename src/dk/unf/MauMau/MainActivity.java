package dk.unf.MauMau;

import android.app.Activity;
import android.graphics.Point;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.text.format.Formatter;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import dk.unf.MauMau.ui.InputEvent;

public class MainActivity extends Activity {

    private CanvasManager canvasManager;

    public static String ip;
    public static int HEIGHT;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        HEIGHT = size.y;
        canvasManager = new CanvasManager(this);
        canvasManager.init(getApplicationContext());
        setContentView(canvasManager);


        WifiManager wifiManager = (WifiManager) getSystemService(WIFI_SERVICE);
        WifiInfo wifiInfo = wifiManager.getConnectionInfo();
        int ip = wifiInfo.getIpAddress();
        String ipAdress = Formatter.formatIpAddress(ip);
        Settings.setIP(ipAdress);
        Log.i("Mau", ipAdress);

    }

    @Override
    protected void onPause() {
        super.onPause();
        canvasManager.onPause();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getActionMasked();
        switch (action) {
            case MotionEvent.ACTION_DOWN: canvasManager.onTouchEvent(new InputEvent((int) event.getX(), (int) event.getY(), 1, InputEvent.DOWN_EVENT)); return true;
            case MotionEvent.ACTION_MOVE: canvasManager.onTouchEvent(new InputEvent((int) event.getX(), (int) event.getY(), 1, InputEvent.UP_EVENT)); return true;
            case MotionEvent.ACTION_UP: return true;
            default: return super.onTouchEvent(event);
        }
    }


}
