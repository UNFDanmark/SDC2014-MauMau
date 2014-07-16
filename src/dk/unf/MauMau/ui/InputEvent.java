package dk.unf.MauMau.ui;

/**
 * Created by sdc on 7/16/14.
 */
public class InputEvent {

    public final int x;
    public final int y;
    public final int pointer;
    public final int type;

    public static final int DOWN_EVENT = 0;
    public static final int UP_EVENT = 1;
    public static final int MOVE_EVENT = 2;

    public InputEvent(int x, int y, int pointer, int type) {
        this.x = x;
        this.y = y;
        this.pointer = pointer;
        this.type = type;
    }

}
