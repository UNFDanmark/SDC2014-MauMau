package dk.unf.MauMau.ui;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import dk.unf.MauMau.CanvasManager;
import dk.unf.MauMau.network.Client;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sdc on 7/16/14.
 */
public class GameRender implements UIState {
    private final int HEIGHT = 1280;
    private final int WIDTH = 720;

    private AssetLoader loader;
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
        loader = manager.getLoader();


        cards.add(new CardElement(0,0,0,0,2,8));
        cards.add(new CardElement(0,0,0,0,1,10));
        cards.add(new CardElement(0,0,0,0,3,7));
        cards.add(new CardElement(0,0,0,0,0,12));
        cards.add(new CardElement(0,0,0,0,2,6));

        playedCards.add(new CardElement(0,0,0,0,0,12));
        /*
        client = new Client();
        new Thread(new Runnable() {
            @Override
            public void run() {
                client.connect();
                while (client.isConnected()) {
                    client.tick();
                    try {
                        java.lang.Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
        NetPkg pkg = new NetPkg(NetPkg.PKG_CONNECT);
        pkg.addString("Ello Server!");
        client.send(pkg);*/
    }

    public void onEnter() {

    }

    public void onLeave() {

    }

    public void draw(Canvas canvas){
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
}
