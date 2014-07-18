package dk.unf.MauMau.network.NetPkg;

/**
 * Created by sdc on 7/17/14.
 */
public class PkgMauMauShake implements NetPkg {

    public final boolean succeeded;

    public PkgMauMauShake(boolean succeeded) {
        this.succeeded = succeeded;
    }

    public PkgMauMauShake(String input) {
        succeeded = Boolean.parseBoolean(input);
    }

    @Override
    public int getType() {
        return PKG_MAU_MAU_SHAKE;
    }

    @Override
    public String serialize() {
        return getType() + Boolean.toString(succeeded);
    }
}
