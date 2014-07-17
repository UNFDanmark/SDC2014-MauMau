package dk.unf.MauMau.ui;


import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;

/**
 * Created by sdc on 7/16/14.
 */
public class Button extends Element {

    private String text;
    private Paint paint;

    private OnClickListener listener;

    public Button(String text, int x, int y, int width, int height) {
        super(x,y,width,height);
        this.text = text;
        paint = new Paint();
        paint.setColor(Color.WHITE);
        paint.setTextSize(42);
    }

    public Button(String text, int y, int height) {
        super(0,y,0,height);
    }

    public void tick(InputEvent event) {
        if (listener != null && inside(event.x,event.y)) {
            listener.onClick(this);
        }
    }
    private int getTextHeight(String text, Paint paint) {
        Rect bounds = new Rect();
        paint.getTextBounds(text, 0, text.length(), bounds);
        int height = bounds.bottom + bounds.height();
        return height;
    }

    public void draw(Canvas canvas) {
        canvas.drawText(text,x,y + getTextHeight(text, paint),paint);
    }

    public void setOnClickListener(OnClickListener listener) {
        this.listener = listener;
    }

}
