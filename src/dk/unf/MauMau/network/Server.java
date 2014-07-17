package dk.unf.MauMau.network;

import android.util.Log;
import dk.unf.MauMau.network.NetPkg.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Created by sdc on 7/15/14.
 */
public class Server implements Runnable {
    private ServerSocket socket;
    private volatile boolean running = true;
    private ArrayList<ServerThread> threads;
    private ArrayList<NetListener> listeners = new ArrayList<NetListener>();
    private InetSocketAddress address;
    private int connectionID;

    public Server(String ip) {
        threads = new ArrayList<ServerThread>();
        address = new InetSocketAddress(ip,8080);
    }

    public void run() {

        try {
            socket = new ServerSocket();
            socket.bind(address);

            while (running) {
                Log.i("Mau","Waiting for new connection");
                ServerThread thread = new ServerThread(socket.accept());
                Thread clientThread = new Thread(thread);
                clientThread.setName("ClientThread"+connectionID++);
                clientThread.start();
                threads.add(thread);
            }

        } catch (IOException e) {
            Log.e("Mau","Unable to open server channel");
            e.printStackTrace();
        }
    }

    public void addListener(NetListener listener) {
        listeners.add(listener);
    }

    public synchronized void sendPkg(NetPkg pkg, int id) {
        threads.get(id).sendPkg(pkg);
    }

    public synchronized void close() {
        running = false;
        Log.i("Mau","Closing server thread");
        for (ServerThread thread : threads) {
            thread.closeSocket();
        }
        try {
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private class ServerThread implements Runnable {

        private Socket socket;
        private volatile boolean running = true;

        private Queue<NetPkg> socketQueue = new ConcurrentLinkedQueue<NetPkg>();

        public ServerThread(Socket socket) {
            Log.i("Mau","New connection!");
            this.socket = socket;
        }

        public void sendPkg(NetPkg pkg) {
            socketQueue.add(pkg);
        }

        public void run() {
            PrintWriter out;
            BufferedReader in;
            try {
            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            out.println(new PkgHandshake().serialize());
            running = true;
            while (running) {
                if (in.ready()) {
                    String inputLine;
                    while (in.ready() && (inputLine = in.readLine()) != null) {
                        NetPkg pkg = createPkg(inputLine);
                        for (NetListener listener : listeners) {
                            listener.received(pkg);
                        }
                    }
                }
                while (!socketQueue.isEmpty()) {
                    out.println(socketQueue.poll().serialize());
                }
            }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        private NetPkg createPkg(String data) {
            int type = Integer.parseInt(data.substring(0,2));
            switch (type) {
                case NetPkg.PKG_CONNECT: return new PkgConnect(data.substring(2));
                case NetPkg.PKG_START_GAME: return new PkgStartGame();
                case NetPkg.PKG_THROW_CARD: return new PkgThrowCard(data.substring(2));
                case NetPkg.PKG_SET_COLOR: return new PkgSetColor(data.substring(2));
                //case NetPkg.PKG_DISCONNECT: return new PkgDisconnect(data.substring(2));
                default: Log.e("Mau","Package type " + type + " not implemented yet...");
            }
            return new PkgHandshake();
        }

        public synchronized void closeSocket() {
            try {
                running = false;
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
