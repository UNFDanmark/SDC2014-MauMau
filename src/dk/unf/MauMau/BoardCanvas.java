package dk.unf.MauMau;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.*;
import android.view.View;
import dk.unf.MauMau.ui.AssetLoader;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Stack;

/**
 * Created by sdc on 7/15/14.
 */
public class BoardCanvas extends View {

    Paint paint = new Paint();
    Bitmap bm;
    Game game = new Game();
    AssetLoader loader = new AssetLoader();

    public void init(Context context){
        loader.load(context);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        paint.setColor(Color.RED);
//        canvas.drawBitmap(loader.getCard(7,3), 0, 0, null);
        for(int i = 0; i < game.getPlayers().peek().cards.size(); i++){
            System.out.println(game.getPlayers().peek().cards.get(i).cardValue + "");
            Bitmap card = loader.getCard(game.getPlayers().peek().cards.get(i).cardValue, game.getPlayers().peek().cards.get(i).color);

            canvas.drawBitmap(card, i*50, 0, null);
        }

    }

    public BoardCanvas(Context context) {
        super(context);
    }

}
