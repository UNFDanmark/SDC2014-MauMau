package dk.unf.MauMau.ui;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;
import dk.unf.MauMau.CanvasManager;
import dk.unf.MauMau.MainActivity;
import dk.unf.MauMau.Settings;
import dk.unf.MauMau.network.Client;
import dk.unf.MauMau.network.NetListener;
import dk.unf.MauMau.network.NetPkg.NetPkg;
import dk.unf.MauMau.network.NetPkg.PkgConnect;
import dk.unf.MauMau.network.NetPkg.PkgDrawCard;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sdc on 7/16/14.
 */
public class GameRender implements UIState, NetListener {
    private final int HEIGHT = MainActivity.HEIGHT;
    private final int WIDTH = 720;

    private String serverIP;

    private AssetLoader loader;
    private CanvasManager canvasManager;
    Client client;
    Paint paint = new Paint();
    List<CardElement> cards = new ArrayList<CardElement>();
    List<CardElement> playedCards = new ArrayList<CardElement>();
    int spacing = 50;
    int cardWidth = Math.round(200 * 0.7106f);

    public GameRender() {
        //All construction code in init()
    }

    public void init(CanvasManager manager){
        canvasManager = manager;
        loader = manager.getLoader();

        playedCards.add(new CardElement(0,0,0,0,0,12));

    }

    @Override
    public void onEnter() {
        if (Settings.getRunningHost()) {
            serverIP = Settings.getIP();
        } else {
            serverIP = Settings.getServerIP();
        }

        
        client = new Client();
        client.addListener(this);
        Thread clientThread = new Thread(new Runnable() {
            @Override
            public void run() {
                client.connect();
                while (client.running) {
                    client.tick();
                }
            }
        });
        clientThread.setName("ClientSocketTickerThread");
        clientThread.start();
    }

    @Override
    public void onLeave() {
        client.running = false;
        client.close();
    }

    @Override
    public void onInputEvent(InputEvent event) {

    }

    public void draw(Canvas canvas){
        canvas.drawBitmap(loader.getBackground(HEIGHT, 0), 0, 0, null);
        int x = WIDTH/2 - (spacing * (5 - 1) + cardWidth)/2;
        paint.setColor(Color.RED);
        for(int i = 0; i < cards.size(); i++){
            Bitmap card = loader.getCard(cards.get(i).cardValue, cards.get(i).cardColor);
            System.out.println(cardWidth + " ");
            canvas.drawBitmap(card, i*spacing+x, HEIGHT-400, null);
        }
        for (int i = 0; i < 10; i++) { //Show the deck
            canvas.drawBitmap(loader.getFaceDown(), WIDTH / 2 - loader.getFaceDown().getWidth() / 2, 20+i*5, null);
        }

        for( int i = 0; i < playedCards.size(); i ++) { //Show played cards
            Bitmap card = loader.getCard(playedCards.get(i).cardValue, playedCards.get(i).cardColor);
            canvas.drawBitmap(card, WIDTH / 2 - card.getWidth() / 2, 320, null);
        }

    }

    @Override
    public synchronized void received(NetPkg data) {

        switch (data.getType()) {
            case NetPkg.PKG_DRAW_CARD: cards.add(new CardElement(0, 0, 0, 0, ((PkgDrawCard) data).card.cardValue, ((PkgDrawCard) data).card.color)); break;
            case NetPkg.PKG_HANDSHAKE: client.send(new PkgConnect("Player",-1)); break;
            default: Log.i("Mau", "Client received unknown package of type: " + data.getType());
        }
        canvasManager.postInvalidate();

    }

    @Override
    public synchronized void onTimeout() {

    }
}
