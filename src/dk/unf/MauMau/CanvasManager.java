package dk.unf.MauMau;

import android.content.Context;
import android.graphics.*;
import android.util.Log;
import android.view.View;
import dk.unf.MauMau.game.Game;
import dk.unf.MauMau.ui.*;

/**
 * Created by sdc on 7/15/14.
 */
public class CanvasManager extends View {

    private final int WIDTH = 720;

    AssetLoader loader = new AssetLoader();

    //No other place to put it?
    private Game game;

    public final static int MAIN_MENU_STATE = 0;
    public final static int GAME_STATE = 1;
    public final static int JOIN_STATE = 2;
    public final static int SETTINGS_STATE = 3;

    private UIState currentState;
    private UIState[] states = {new MainMenu(),new GameRender()};

    public void init(Context context){
        loader.load(context);
        for (UIState state : states) {
            state.init(this);
        }
        currentState = states[MAIN_MENU_STATE];
        currentState.onEnter();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        currentState.draw(canvas);
    }

    public void gotoState(int state) {
        leaveCurrentState();
        states[state].onEnter();
        currentState = states[state];
    }

    public void onPause() {
        if (game != null) {
            game.stopGame();
        }
        for (UIState state : states) {
            state.onLeave();
        }
        currentState = states[MAIN_MENU_STATE];
    }

    private void leaveCurrentState() {
        currentState.onLeave();
    }

    public AssetLoader getLoader() {
        return loader;
    }

    public CanvasManager(Context context) {
        super(context);
    }

    void onTouchEvent(InputEvent event) {
        currentState.onInputEvent(event);
    }

    public void startHost() {
        game = new Game(Settings.getIP());
        Thread gameServerThread = new Thread(game);
        gameServerThread.setName("GameServerThread");
        gameServerThread.start();
        Settings.setRunningHost(true);
    }

}
