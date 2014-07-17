package dk.unf.MauMau.ui;

import android.graphics.*;
import android.util.Log;
import dk.unf.MauMau.CanvasManager;
import dk.unf.MauMau.MainActivity;
import dk.unf.MauMau.Settings;
import dk.unf.MauMau.game.Player;
import dk.unf.MauMau.network.Client;
import dk.unf.MauMau.network.NetListener;
import dk.unf.MauMau.network.NetPkg.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sdc on 7/16/14.
 */
public class GameRender implements UIState, NetListener {
    private final int HEIGHT = MainActivity.HEIGHT;
    private final int WIDTH = MainActivity.WIDTH;

    private String serverIP;

    private AssetLoader loader;
    private CanvasManager canvasManager;
    private boolean gameRunning = false;
    private boolean gameStarting = false;
    Client client;
    Paint cardPaint = new Paint();
    Paint textPaint = new Paint();
    List<CardElement> cards = new ArrayList<CardElement>();
    List<CardElement> playedCards = new ArrayList<CardElement>();
    List<PlayerInfo> players = new ArrayList<PlayerInfo>();
    int currentPlayersTurn;
    int spacing = 100;
    int cardWidth = Math.round(200 * 0.7106f);
    int margin;

    private boolean yourTurn = false;
    private int yourId = 0;

    public GameRender() {
        //All construction code in init()
    }

    public void init(CanvasManager manager){
        canvasManager = manager;
        loader = manager.getLoader();

        textPaint.setTextSize(40);
        textPaint.setColor(Color.WHITE);

    }

    @Override
    public void onEnter() {
        if (Settings.getRunningHost()) {
            serverIP = Settings.getIP();
        } else {
            serverIP = Settings.getServerIP();
        }
        serverIP = "10.16.111.23";

        
        client = new Client();
        client.addListener(this);
        Thread clientThread = new Thread(new Runnable() {
            @Override
            public void run() {
                client.connect(serverIP);
                while (client.running) {
                    client.tick();
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        clientThread.setName("ClientSocketTickerThread");
        clientThread.start();

        margin = WIDTH/2 - (spacing * (5 - 1) + cardWidth)/2;
    }

    @Override
    public void onLeave() {
        client.running = false;
        client.close();
    }

    @Override
    public void onInputEvent(InputEvent event) {
        if (Settings.getRunningHost() && !gameRunning) {
            Log.i("Mau", "Starting game");
            client.send(new PkgStartGame());
            Log.i("Mau","Told server to start");
            gameRunning = true;
            gameStarting = true;
        }

        if (gameRunning && currentPlayersTurn == yourId) {
            checkClick(event);
        }

        canvasManager.invalidate();
    }

    public void checkClick(InputEvent event) {
        for(int i = cards.size(); i > 0; i--){
            int a = i*spacing+margin;
            if (event.x > (a - cardWidth) && event.x < a && event.y > (HEIGHT-400) && event.y < (HEIGHT-400) + 200) {
                client.send(new PkgThrowCard(cards.get(i-1).toCard()));
                cards.remove(cards.get(i-1));
                return;
            }
        }
    }

    public void draw(Canvas canvas){
        canvas.drawBitmap(loader.getBackground(HEIGHT, WIDTH, 0), 0, 0, null);
        cardPaint.setColor(Color.RED);
        for(int i = 0; i < cards.size(); i++){
            Bitmap card = loader.getCard(cards.get(i).cardValue, cards.get(i).cardColor);
            canvas.drawBitmap(card, i*spacing+margin, HEIGHT-400, null);
        }
        for (int i = 0; i < 10; i++) { //Show the deck
            canvas.drawBitmap(loader.getFaceDown(), WIDTH / 2 - loader.getFaceDown().getWidth() / 2, 20+i*5, null);
        }

        for( int i = 0; i < playedCards.size(); i ++) { //Show played cards
            Bitmap card = loader.getCard(playedCards.get(i).cardValue, playedCards.get(i).cardColor);
            canvas.drawBitmap(card, WIDTH / 2 - card.getWidth() / 2, 320, null);
        }

        if (gameStarting) {
            String text = "Game starting...";
            Rect bounds = new Rect();
            textPaint.getTextBounds(text,0,text.length(),bounds);
            canvas.drawText(text,WIDTH/2 - bounds.width()/2,500,textPaint);
        }

        for (int i = 0; i < players.size(); i++) {
            canvas.drawText(players.get(i).getNick() + ": " + players.get(i).getId(),50,i*50+200,textPaint);
        }

    }

    @Override
    public synchronized void received(NetPkg data) {

        switch (data.getType()) {
            case NetPkg.PKG_DRAW_CARD: cards.add(new CardElement(((PkgDrawCard) data).card)); break;
            case NetPkg.PKG_FACE_CARD: playedCards.add(new CardElement(((PkgFaceCard) data).card)); nextTurn(((PkgFaceCard) data).nextPlayer); break;
            case NetPkg.PKG_NEXT_TURN: nextTurn(((PkgNextTurn) data).playerId); break;
            case NetPkg.PKG_HANDSHAKE: if (!gameRunning) client.send(new PkgConnect("Player",-1)); break;
            case NetPkg.PKG_START_GAME: gameRunning = true; gameStarting = false; break;
            case NetPkg.PKG_CONNECT: addPlayer((PkgConnect) data); break;
            default: Log.i("Mau", "Client received unknown package of type: " + data.getType());
        }
        canvasManager.postInvalidate();

    }

    private void nextTurn(int nextId) {
        currentPlayersTurn = nextId;
        Log.i("Mau","Now player " + currentPlayersTurn + "s turn");
        if (currentPlayersTurn == yourId) {
            yourTurn = true;
        }
    }

    private void addPlayer(PkgConnect data) {
        if (data.nickname.equalsIgnoreCase("#")) {
            yourId = data.id;
        } else {
            players.add(new PlayerInfo(data.id,data.nickname));
        }
    }


    @Override
    public synchronized void onTimeout() {

    }
}
