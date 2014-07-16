package dk.unf.MauMau.ui;

import android.graphics.Canvas;
import dk.unf.MauMau.CanvasManager;

import java.util.ArrayList;

/**
 * Created by sdc on 7/16/14.
 */
public class MainMenu implements UIState {

    private static final int WIDTH = 720;
    private static final int HEIGHT = 1280;

    private static final int BUTTON_WIDTH = 400;
    private static final int BUTTON_HEIGHT = 250;

    private static final int TOP_MARGIN = 50;
    private static final int SPACING = 200;

    ArrayList<Button> buttons = new ArrayList<Button>();

    public MainMenu() {
        //All construction code in init()
    }

    public void init(final CanvasManager manager) {
        buttons.add(spawnButton("Create game", 0, new OnClickListener() {
            @Override
            public void onClick(Element element) {
                manager.gotoState(CanvasManager.GAME_STATE);
            }
        }));

        buttons.add(spawnButton("Join game", 1, new OnClickListener() {
            @Override
            public void onClick(Element element) {
                //manager.gotoState(CanvasManager.JOIN_STATE);
            }
        }));

        buttons.add(spawnButton("Settings", 2, new OnClickListener() {
            @Override
            public void onClick(Element element) {

            }
        }));

        buttons.add(spawnButton("Quit", 3, new OnClickListener() {
            @Override
            public void onClick(Element element) {

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
        for (Button button : buttons) {
            button.draw(canvas);
        }
    }

    private Button spawnButton(String text, int num, OnClickListener listener) {
        Button button = new Button(text,WIDTH/2 - BUTTON_WIDTH/2, TOP_MARGIN + SPACING * num, BUTTON_WIDTH, BUTTON_HEIGHT);
        button.setOnClickListener(listener);
        return button;
    }

}
