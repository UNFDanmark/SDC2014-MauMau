package dk.unf.MauMau.network;

import android.util.Log;
import dk.unf.MauMau.network.NetPkg.NetPkg;
import dk.unf.MauMau.network.NetPkg.PkgConnect;

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
            if (socket.getInputStream().available() > 0) {
                String inLine;

                while ((inLine = in.readLine()) != null) {
                    PkgConnect pkg = new PkgConnect(inLine);
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

    public boolean isConnected() {
        return socket.isConnected();
    }

    public synchronized void send(NetPkg pkg) {
        pkgQueue.offer(pkg);
    }

    public synchronized void addListener(NetListener listener) {
        listeners.add(listener);
    }

}
