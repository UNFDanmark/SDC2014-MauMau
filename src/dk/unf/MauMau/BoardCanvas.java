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
        Bitmap bm = BitmapFactory.decodeResource(getResources(), R.drawable.card);
        bm = scaleDown(bm, 200, true);
        canvas.drawBitmap(bm, 0, 0, null);
    }

    public BoardCanvas(Context context) {
        super(context);


    }
    public static Bitmap scaleDown(Bitmap realImage, float maxImageSize,
                                   boolean filter) {
        float ratio = Math.min(
                (float) maxImageSize / realImage.getWidth(),
                (float) maxImageSize / realImage.getHeight());
        int width = Math.round((float) ratio * realImage.getWidth());
        int height = Math.round((float) ratio * realImage.getHeight());

        Bitmap newBitmap = Bitmap.createScaledBitmap(realImage, width,
                height, filter);
        return newBitmap;
    }
}
