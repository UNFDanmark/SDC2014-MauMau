package dk.unf.MauMau.ui;

/**
 * Created by sdc on 7/16/14.
 */
public abstract class Element {

    protected int x;
    protected int y;
    protected int width;
    protected int height;


    public Element(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public boolean inside(int u, int v) {
        return (x > u && x+width < u && y > v && y < v+height);
    }

}
