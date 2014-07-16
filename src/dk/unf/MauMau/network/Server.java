package dk.unf.MauMau.network;

import android.util.Log;

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
    private InetSocketAddress address;

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
                new Thread(thread).start();
                threads.add(thread);
            }

        } catch (IOException e) {
            Log.e("Mau","Unable to open server channel");
            e.printStackTrace();
        }
    }

    public synchronized void kill() {
        running = false;
    }

    public void addListener(NetListener listener) {

    }

    public void close() {
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
        private boolean running = true;

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
                while (running) {
                    if (socket.getInputStream().available() > 0) {
                        String inputLine;
                        while ((inputLine = in.readLine()) != null) {
                            System.out.println("Got msg: " + inputLine);
                            Log.i("Mau","Ello:" + inputLine);
                        }
                    }
                    out.println("200");
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
