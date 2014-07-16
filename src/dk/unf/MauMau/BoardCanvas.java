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

    private final int WIDTH = 720;

    Paint paint = new Paint();
    Bitmap bm;
    Game game = new Game();
    AssetLoader loader = new AssetLoader();
    int spacing = 50;
    int cardWidth = Math.round(200 * 0.7106f);
    int x = WIDTH/2 - (spacing * (5 - 1) + cardWidth)/2;


    public void init(Context context){
        loader.load(context);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        paint.setColor(Color.RED);
//        canvas.drawBitmap(loader.getCard(7,3), 0, 0, null);
        for(int i = 0; i < game.getPlayers().peek().cards.size(); i++){
            Bitmap card = loader.getCard(game.getPlayers().peek().cards.get(i).cardValue, game.getPlayers().peek().cards.get(i).color);
            System.out.println(cardWidth + " ");
            canvas.drawBitmap(card, i*spacing+x, getHeight()-200, null);
        }

    }

    public BoardCanvas(Context context) {
        super(context);
    }

}
