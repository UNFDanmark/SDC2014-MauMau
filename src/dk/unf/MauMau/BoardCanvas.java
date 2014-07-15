package dk.unf.MauMau;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.*;
import android.view.View;

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



    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        paint.setColor(Color.RED);
        for(int i = 0; i < game.getPlayers().peek().cards.size(); i++){
            Card card = game.getPlayers().peek().cards.get(i);
            String path = "android.resource://dk.unf.MauMau/drawable-xhdpi/c" + card.cardValue + card.color + ".png";
            bm = BitmapFactory.decodeFile(getBitmapFromAsset());
            bm = scaleDown(bm, 200, true);
            canvas.drawBitmap(bm, i, 0, null);
        }

    }

    public BoardCanvas(Context context) {
        super(context);
    }

}
