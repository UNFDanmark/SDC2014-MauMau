package dk.unf.MauMau.ui;

import android.graphics.Canvas;
import android.util.Log;
import dk.unf.MauMau.CanvasManager;
import dk.unf.MauMau.MainActivity;

import java.util.ArrayList;

/**
 * Created by sdc on 7/16/14.
 */
public class MainMenu implements UIState {

    private static final int WIDTH = MainActivity.WIDTH;
    private static final int HEIGHT = MainActivity.HEIGHT;

    private static final int BUTTON_WIDTH = 250;
    private static final int BUTTON_HEIGHT = 100;

    private static final int TOP_MARGIN = 300;
    private static final int SPACING = 200;

    ArrayList<Button> buttons = new ArrayList<Button>();

    AssetLoader loader;

    public MainMenu() {
        //All construction code in init()
    }

    @Override
    public void init(final CanvasManager manager) {
        loader = manager.getLoader();
        buttons.add(spawnButton("Create game", 0, new OnClickListener() {
            @Override
            public void onClick(Element element) {
                manager.startHost();
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    Log.e("Mau", "Failed to wait for socket");
                    e.printStackTrace();
                }
                manager.gotoState(CanvasManager.GAME_STATE);
            }
        }));

        buttons.add(spawnButton("Join game", 1, new OnClickListener() {
            @Override
            public void onClick(Element element) {
                Log.i("Mau", "Clicked on Join Game");
                manager.gotoState(CanvasManager.JOIN_STATE);
            }
        }));

        buttons.add(spawnButton("Quit", 2, new OnClickListener() {
            @Override
            public void onClick(Element element) {
                Log.i("Mau", "Clicked on Quit");
            }
        }));
    }

    @Override
    public void onEnter() {

    }

    @Override
    public void onLeave() {

    }

    @Override
    public void draw(Canvas canvas) {
        canvas.drawBitmap(loader.getBackground(HEIGHT, WIDTH, 1),0,0,null);

        for (Button button : buttons) {
            button.draw(canvas);
        }
    }

    @Override
    public void onInputEvent(InputEvent event) {
        if (event.type == InputEvent.DOWN_EVENT) {
            for (Button button : buttons) {
                button.tick(event);
            }
        }
    }

    private Button spawnButton(String text, int num, OnClickListener listener) {
        Button button = new Button(text,WIDTH/2, TOP_MARGIN + SPACING * num, BUTTON_WIDTH, BUTTON_HEIGHT);
        button.setOnClickListener(listener);
        return button;
    }

}
