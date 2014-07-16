package dk.unf.MauMau.ui;

import android.graphics.Canvas;
import dk.unf.MauMau.CanvasManager;

/**
 * Created by sdc on 7/16/14.
 */
public interface UIState {

    public void onEnter();

    public void onLeave();

    public void onInputEvent(InputEvent event);

    public void init(CanvasManager manager);

    public void draw(Canvas canvas);
}
