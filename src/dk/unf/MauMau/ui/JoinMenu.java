package dk.unf.MauMau.ui;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.widget.EditText;
import dk.unf.MauMau.CanvasManager;
import dk.unf.MauMau.MainActivity;
import dk.unf.MauMau.Settings;

import java.util.ArrayList;

/**
 * Created by sdc on 7/17/14.
 */
public class JoinMenu implements UIState {

    private static final int WIDTH = MainActivity.WIDTH;
    private static final int HEIGHT = MainActivity.HEIGHT;

    private static final int BUTTON_WIDTH = 400;
    private static final int BUTTON_HEIGHT = 100;

    private static final int BOTTOM_MARGIN = 200;

    Button startButton;
    AssetLoader loader;
    CanvasManager manager;

    public JoinMenu() {
        //All construction code in init()
    }

    @Override
    public void onEnter() {
        manager.showTextEdits();
        manager.invalidate();
    }

    @Override
    public void onLeave() {
        manager.hideTextEdits();
        manager.invalidate();
    }

    @Override
    public void onInputEvent(InputEvent event) {

        if (event.type == InputEvent.DOWN_EVENT) {
            startButton.tick(event);
        }
        manager.invalidate();
    }

    @Override
    public void init(final CanvasManager manager) {
        this.manager = manager;
        loader = manager.getLoader();
        startButton = new Button("Start game",WIDTH/2 - BUTTON_WIDTH/2, 500 , BUTTON_WIDTH, BUTTON_HEIGHT);

        startButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(Element element) {
                manager.saveTextEdits();
                manager.gotoState(CanvasManager.GAME_STATE);
            }
        });

    }

    @Override
    public void draw(Canvas canvas) {
        canvas.drawBitmap(loader.getBackground(HEIGHT, WIDTH, 1),0,0,null);
        startButton.draw(canvas);
    }

}
