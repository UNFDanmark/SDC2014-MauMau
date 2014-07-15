package dk.unf.MauMau;

import android.content.Context;
import android.graphics.*;
import android.view.View;

/**
 * Created by sdc on 7/15/14.
 */
public class BoardCanvas extends View {

    Paint paint = new Paint();
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        paint.setColor(Color.RED);
        Bitmap b = BitmapFactory.decodeResource(getResources(), R.drawable.card);
        canvas.drawBitmap(b, 0, 0, null);
    }

    public BoardCanvas(Context context) {
        super(context);


    }
}
