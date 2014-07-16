package dk.unf.MauMau.network;

import android.util.Log;
import dk.unf.MauMau.network.NetPkg.NetPkg;
import dk.unf.MauMau.network.NetPkg.PkgConnect;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

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

        public ServerThread(Socket socket) {
            Log.i("Mau","New connection!");
            this.socket = socket;
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
            out.println("Connected");
            while (running) {
                if (socket.getInputStream().available() > 0) {
                    String inputLine;
                    PkgConnect pkg = new PkgConnect("Player",1);
                    while ((inputLine = in.readLine()) != null) {
                        for (NetListener listener : listeners) {
                            listener.received(pkg);
                        }
                    }
                }
            }
            } catch (IOException e) {
                e.printStackTrace();
            }
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
