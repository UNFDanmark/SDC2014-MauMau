package dk.unf.MauMau;

/**
 * Created by sdc on 7/16/14.
 */
public class Settings {

    private static volatile String ip;
    private static volatile String serverIP;
    private static volatile String nick = "Host";
    private static volatile boolean customRules = false;
    private static volatile boolean runningHost = false;
    private static volatile boolean shakeDetected = false;

    public static synchronized String getIP() {
        return ip;
    }

    public static synchronized void setIP(String ip) {
        Settings.ip = ip;
    }

    public static synchronized boolean getRunningHost() {
        return runningHost;
    }

    public static synchronized void setRunningHost(boolean runningHost) {
        Settings.runningHost = runningHost;
    }

    public static synchronized String getServerIP() {
        return serverIP;
    }

    public static synchronized void setServerIP(String ip) {
        serverIP = ip;
    }

    public static synchronized boolean usesCustomRules() {
        return customRules;
    }

    public static synchronized void setCustomRules(boolean customRules) {
        Settings.customRules = customRules;
    }

    public static synchronized String getNick() {
        return nick;
    }

    public static synchronized void setNick(String nick) {
        Settings.nick = nick;
    }

    public static synchronized boolean getShake() {
        return shakeDetected;
    }

    public static synchronized void setShake(boolean shake) {
        shakeDetected = shake;
    }
}
