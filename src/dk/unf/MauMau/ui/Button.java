package dk.unf.MauMau.ui;


import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

/**
 * Created by sdc on 7/16/14.
 */
public class Button extends Element {

    private String text;
    private int x;
    private int y;
    private int width;
    private int height;
    private Paint paint;

    private OnClickListener listener;

    public Button(String text, int x, int y, int width, int height) {
        super(x,y,width,height);
        this.text = text;
        paint = new Paint();
        paint.setColor(Color.WHITE);
    }

    public void tick(InputEvent event) {
        if (listener != null && inside(event.x,event.y)) {
            listener.onClick(this);
        }
    }

    public void draw(Canvas canvas) {
        canvas.drawText(text,x,y,paint);
    }

    public void setOnClickListener(OnClickListener listener) {
        this.listener = listener;
    }

}
