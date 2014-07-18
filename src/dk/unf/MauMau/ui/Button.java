package dk.unf.MauMau.ui;


import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import dk.unf.MauMau.MainActivity;

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
//        paint.setStrokeWidth(2);
//        paint.setStyle(Paint.Style.STROKE);
        this.x = MainActivity.WIDTH/2 - getTextWidth()/2;
    }

    public Button(String text, int y, int height) {
        super(0,y,0,height);
    }

    public void tick(InputEvent event) {
        if (listener != null && inside(event.x,event.y)) {
            listener.onClick(this);
        }
    }
    private int getTextHeight() {
        Rect bounds = new Rect();
        paint.getTextBounds(text, 0, text.length(), bounds);
        int height = bounds.bottom + bounds.height();
        return height;
    }
    private int getTextWidth() {
        Rect bounds = new Rect();
        paint.getTextBounds(text, 0, text.length(), bounds);
        int height = bounds.right + bounds.height();
        return height;
    }

    public void draw(Canvas canvas) {
        canvas.drawText(text, x,y + getTextHeight(),paint);
//        canvas.drawRect(x,y,x+width,y+height,paint);
        //I know this is fucked but i don't care. i do what i want
    }

    public void setOnClickListener(OnClickListener listener) {
        this.listener = listener;
    }

}
