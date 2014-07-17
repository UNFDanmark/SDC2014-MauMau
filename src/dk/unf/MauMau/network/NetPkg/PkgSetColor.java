package dk.unf.MauMau.network.NetPkg;

/**
 * Created by sdc on 7/17/14.
 */
public class PkgSetColor implements NetPkg{

    public final int color;

    public PkgSetColor(int color) {
        this.color = color;
    }

    public PkgSetColor(String input) {
        color = Integer.parseInt(input);
    }

    @Override
    public int getType() {
        return NetPkg.PKG_SET_COLOR;
    }

    @Override
    public String serialize() {
        return "" + getType() + color;
    }
}
