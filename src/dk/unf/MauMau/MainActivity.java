package dk.unf.MauMau;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.Point;
import android.hardware.SensorListener;
import android.hardware.SensorManager;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.text.format.Formatter;
import android.util.Log;
import android.view.*;
import android.widget.EditText;
import android.widget.FrameLayout;
import dk.unf.MauMau.ui.InputEvent;

public class MainActivity extends Activity  implements SensorListener{

    private CanvasManager canvasManager;
    private SensorManager sensorManager;

    public static String ip;
    public static int HEIGHT;
    public static int WIDTH;
    private FrameLayout frameLayout;
    private EditText ipField;
    private EditText nickField;

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
        WIDTH = size.x;

        canvasManager = new CanvasManager(this);
        canvasManager.init(getApplicationContext());

        frameLayout = new FrameLayout(this);
        frameLayout.addView(canvasManager);

        setupFrameLayout(frameLayout);
        setContentView(frameLayout);

        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        sensorManager.registerListener(this,
                SensorManager.SENSOR_ACCELEROMETER,
                SensorManager.SENSOR_DELAY_GAME);

        WifiManager wifiManager = (WifiManager) getSystemService(WIFI_SERVICE);
        WifiInfo wifiInfo = wifiManager.getConnectionInfo();
        int ip = wifiInfo.getIpAddress();
        String ipAdress = Formatter.formatIpAddress(ip);
        Settings.setIP(ipAdress);
        Log.i("Mau", ipAdress);

    }

    private void setupFrameLayout(FrameLayout layout) {
        ipField = new EditText(getApplicationContext());
        ipField.setText("Enter IP here!");
        ipField.setBackgroundColor(Color.WHITE);
        ipField.setTextColor(Color.BLACK);
        FrameLayout.LayoutParams ipParams = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        ipParams.setMargins(WIDTH/2-120,250,0,0);
        ipField.setLayoutParams(ipParams);
        frameLayout.addView(ipField);

        nickField = new EditText(getApplicationContext());
        nickField.setText("Enter Nick here!");
        nickField.setBackgroundColor(Color.WHITE);
        nickField.setTextColor(Color.BLACK);
        FrameLayout.LayoutParams nickParams = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        nickParams.setMargins(WIDTH/2-138,400,0,0);
        nickField.setLayoutParams(nickParams);
        frameLayout.addView(nickField);

        ipField.setVisibility(View.INVISIBLE);
        nickField.setVisibility(View.INVISIBLE);
    }

    public EditText getIpField() {
        return ipField;
    }

    public EditText getNickField() {
        return nickField;
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

    public static void brutallyMurderMe() {
        Log.i("Mau","Brutally murdered");
        android.os.Process.killProcess(android.os.Process.myPid());
        System.exit(0);
    }

    @Override
    public void onBackPressed() {
        MainActivity.brutallyMurderMe();
    }

    @Override
    protected void onUserLeaveHint() {
        super.onUserLeaveHint();

        MainActivity.brutallyMurderMe();
    }

    private long lastUpdate;
    private float lastX;
    private float lastY;
    private float lastZ;

    public void onSensorChanged(int sensor, float[] values) {
        if (sensor == SensorManager.SENSOR_ACCELEROMETER) {
            long curTime = System.currentTimeMillis();

            if ((curTime - lastUpdate) > 100) {
                long diffTime = (curTime - lastUpdate);
                lastUpdate = curTime;

                float x = values[SensorManager.DATA_X];
                float y = values[SensorManager.DATA_Y];
                float z = values[SensorManager.DATA_Z];

                float speed = Math.abs(x+y+z - lastX - lastY - lastZ) / diffTime * 10000;
                if (speed > 800) {
                    Log.d("Mau", "shake detected w/ speed: " + speed);
                    Settings.setShake(true);
                }
                lastX = x;
                lastY = y;
                lastZ = z;
            }
        }
    }

    @Override
    public void onAccuracyChanged(int sensor, int accuracy) {
    }
}
