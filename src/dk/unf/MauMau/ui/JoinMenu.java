package dk.unf.MauMau.ui;

import android.graphics.Canvas;
import dk.unf.MauMau.CanvasManager;
import dk.unf.MauMau.MainActivity;

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

    }

    @Override
    public void onLeave() {

    }

    @Override
    public void onInputEvent(InputEvent event) {

        if (event.type == InputEvent.DOWN_EVENT) {
            startButton.tick(event);
        }
        manager.invalidate();
    }

    @Override
    public void init(CanvasManager manager) {
        this.manager = manager;
        loader = manager.getLoader();
        startButton = new Button("Start game",WIDTH/2 - BUTTON_WIDTH/2, HEIGHT-BOTTOM_MARGIN , BUTTON_WIDTH, BUTTON_HEIGHT);
    }

    @Override
    public void draw(Canvas canvas) {
        canvas.drawBitmap(loader.getBackground(HEIGHT, WIDTH, 1),0,0,null);

        startButton.draw(canvas);
    }

}
