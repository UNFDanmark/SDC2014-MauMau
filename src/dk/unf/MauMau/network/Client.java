package dk.unf.MauMau.network;

import android.util.Log;
import dk.unf.MauMau.network.NetPkg.NetPkg;
import dk.unf.MauMau.network.NetPkg.PkgConnect;
import dk.unf.MauMau.network.NetPkg.PkgDrawCard;
import dk.unf.MauMau.network.NetPkg.PkgHandshake;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Created by sdc on 7/15/14.
 */
public class Client {

    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;
    private Queue<NetPkg> pkgQueue;
    public volatile boolean running;

    private ArrayList<NetListener> listeners = new ArrayList<NetListener>();

    public Client() {
        pkgQueue = new ConcurrentLinkedQueue<NetPkg>();
    }

    public synchronized boolean connect() {
        try {
            Log.i("Mau","About to create socket!");
            socket = new Socket(InetAddress.getByName("10.16.111.23"),8080);
            if (socket.isConnected()) {
                Log.i("Mau", "Successfully connected!");
                running = true;
            }
            socket.setKeepAlive(true);
            socket.setTcpNoDelay(true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public synchronized void close() {
        try {
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public synchronized void tick() {
        try {
            if (in.ready()) {
                String inLine;
                while (in.ready() && (inLine = in.readLine()) != null) {
                    NetPkg pkg = createPkg(inLine);
                    for (NetListener listener : listeners) {
                        listener.received(pkg);
                    }
                }
            }
            while (!pkgQueue.isEmpty()) {
                out.println(pkgQueue.poll().serialize());
                out.flush();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private NetPkg createPkg(String data) {
        int type = Integer.parseInt(data.substring(0,2));
        switch (type) {
            case NetPkg.PKG_CONNECT: return new PkgConnect(data.substring(2));
            //case NetPkg.PKG_DISCONNECT: return new PkgDisconnect(data.substring(2));
            case NetPkg.PKG_DRAW_CARD: return new PkgDrawCard(data.substring(2));
            case NetPkg.PKG_HANDSHAKE: return new PkgHandshake();
            default: Log.e("Mau","Package type " + type + " not implemented yet...");
        }
        return new PkgHandshake();
    }

    public synchronized void send(NetPkg pkg) {
        pkgQueue.add(pkg);
    }

    public synchronized void addListener(NetListener listener) {
        listeners.add(listener);
    }

}
