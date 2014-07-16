package dk.unf.MauMau;

import android.content.Context;
import android.graphics.*;
import android.view.View;
import dk.unf.MauMau.game.Game;
import dk.unf.MauMau.ui.AssetLoader;
import dk.unf.MauMau.ui.GameRender;

/**
 * Created by sdc on 7/15/14.
 */
public class BoardCanvas extends View {

    private final int WIDTH = 720;

    AssetLoader loader = new AssetLoader();
    GameRender gameRender;


    public void init(Context context){
        loader.load(context);
        gameRender = new GameRender();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        gameRender.draw(canvas,loader);

    }

    public BoardCanvas(Context context) {
        super(context);
    }

}
