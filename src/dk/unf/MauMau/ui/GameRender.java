package dk.unf.MauMau.ui;

import android.content.Context;
import android.graphics.*;
import android.os.Vibrator;
import android.util.Log;
import dk.unf.MauMau.CanvasManager;
import dk.unf.MauMau.MainActivity;
import dk.unf.MauMau.Settings;
import dk.unf.MauMau.game.Card;
import dk.unf.MauMau.game.Player;
import dk.unf.MauMau.network.Client;
import dk.unf.MauMau.network.NetListener;
import dk.unf.MauMau.network.NetPkg.*;

import java.lang.reflect.Array;
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

    private boolean suitSelect = false;
    private int jackCache = -1;

    private int winner = -1;

    Client client;
    Paint cardPaint = new Paint();
    Paint textPaint = new Paint();

    List<CardElement> cards = new ArrayList<CardElement>();
    List<CardElement> playedCards = new ArrayList<CardElement>();
    List<PlayerInfo> players = new ArrayList<PlayerInfo>();
    List<CardElement> allowedThrows = new ArrayList<CardElement>();

    int currentPlayersTurn;
    int spacing = 100;
    int cardWidth = Math.round(200 * 0.7106f);
    int margin;
    Vibrator v;

    private boolean yourTurn = false;
    private int yourId = 0;

    public GameRender() {
        //All construction code in init()
    }

    public void init(CanvasManager manager) {
        canvasManager = manager;
        loader = manager.getLoader();

        textPaint.setTextSize(40);
        textPaint.setColor(Color.WHITE);
        v = (Vibrator) manager.getContext().getSystemService(Context.VIBRATOR_SERVICE);
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

    }

    @Override
    public void onLeave() {
        client.running = false;
        client.close();
    }

    @Override
    public void onInputEvent(InputEvent event) {
        if (event.type == InputEvent.DOWN_EVENT) {
            if (Settings.getRunningHost() && !gameRunning) {
                Log.i("Mau", "Starting game");
                client.send(new PkgStartGame());
                Log.i("Mau", "Told server to start");
                gameRunning = true;
                gameStarting = true;
            }

            if (gameRunning && currentPlayersTurn == yourId && winner == -1) {
                checkClick(event);
            }
            canvasManager.invalidate();
        }

    }


    public void checkClick(InputEvent event) {
        if (suitSelect) {
            int newSuit = 0;
            if (event.x < WIDTH / 2 && event.y < HEIGHT / 2) { //clubs
                newSuit = AssetLoader.CLUBS_ID;
            } else if (event.x > WIDTH / 2 && event.y < HEIGHT / 2) { //hearts
                newSuit = AssetLoader.HEARTS_ID;
            } else if (event.x < WIDTH / 2 && event.y > HEIGHT / 2) { //diamonds
                newSuit = AssetLoader.DIAMONDS_ID;
            } else if (event.x > WIDTH / 2 && event.y > HEIGHT / 2) { //spades
                newSuit = AssetLoader.SPADES_ID;
            }
            client.send(new PkgThrowCard(cards.get(jackCache).toCard()));
            client.send(new PkgSetColor(newSuit));
            cards.remove(cards.get(jackCache));
            suitSelect = false;
        } else {
            if (yourTurn) {
                for (int i = cards.size(); i > 0; i--) {
                    int a = i * spacing + margin;
                    if (event.x
 > (a - cardWidth) && event.x < a && event.y > (HEIGHT - 400) && event.y < (HEIGHT - 400) + 200) {
                        if (allowedThrows.contains(cards.get(i - 1))) {
                            if (cards.get(i-1).cardValue == 11) {
                                jackCache = i-1;
                                suitSelect = true;
                                yourTurn = false;
                                return;
                            } else {
                                client.send(new PkgThrowCard(cards.get(i - 1).toCard()));
                                cards.remove(cards.get(i - 1));
                                yourTurn = false;
                                return;
                            }
                        }
                    }
                }
            }
        }
    }

    public void draw(Canvas canvas) {


        margin = WIDTH / 2 - (spacing * (cards.size() - 1) + cardWidth) / 2;

        canvas.drawBitmap(loader.getBackground(HEIGHT, WIDTH, 0), 0, 0, null);
        cardPaint.setColor(Color.RED);
        for (int i = 0; i < cards.size(); i++) {
            Bitmap card = loader.getCard(cards.get(i).cardValue, cards.get(i).cardColor);
            canvas.drawBitmap(card, i * spacing + margin, HEIGHT - 400, null);
        }
        for (int i = 0; i < 10; i++) { //Show the deck
            canvas.drawBitmap(loader.getFaceDown(), WIDTH / 2 - loader.getFaceDown().getWidth() / 2, 20 + i * 5, null);
        }

        for (int i = 0; i < playedCards.size(); i++) { //Show played cards
            Bitmap card = loader.getCard(playedCards.get(i).cardValue, playedCards.get(i).cardColor);
            canvas.drawBitmap(card, WIDTH / 2 - card.getWidth() / 2, 320, null);
        }

        if (gameStarting) {
            String text = "Game starting...";
            Rect bounds = new Rect();
            textPaint.getTextBounds(text, 0, text.length(), bounds);
            canvas.drawText(text, WIDTH / 2 - bounds.width() / 2, HEIGHT / 2, textPaint);
        } else if (yourTurn) {
            String text = "Your turn";
            Rect bounds = new Rect();

            textPaint.getTextBounds(text, 0, text.length(), bounds);
            canvas.drawText(text, WIDTH / 2 - bounds.width() / 2, HEIGHT / 2, textPaint);
        } else if (!gameRunning) {
            String text = "IP: " + Settings.getIP();
            Rect bounds = new Rect();
            textPaint.getTextBounds(text, 0, text.length(), bounds);
            canvas.drawText(text, WIDTH / 2 - bounds.width() / 2, HEIGHT / 2, textPaint);
        } else if (!gameRunning) {
            String text = "IP: " + Settings.getIP();
            Rect bounds = new Rect();
            textPaint.getTextBounds(text, 0, text.length(), bounds);
            canvas.drawText(text, WIDTH / 2 - bounds.width() / 2, HEIGHT / 2, textPaint);
        } else if (winner != -1) {
            String text = "Player " + players.get(winner).getNick() + " has won!" + Settings.getIP();
            Rect bounds = new Rect();
            textPaint.getTextBounds(text, 0, text.length(), bounds);
            canvas.drawText(text, WIDTH / 2 - bounds.width() / 2, HEIGHT / 2, textPaint);
        }

        for (int i = 0; i < players.size(); i++) {
            canvas.drawText(players.get(i).getNick() + ": " + players.get(i).getId(), 50, i * 50 + 200, textPaint);
        }
        if(suitSelect) {
            drawJack(canvas);
        }

    }

    public void drawJack(Canvas canvas) {
        canvas.drawBitmap(loader.getSuits(0), 0, 0, null);
        canvas.drawBitmap(loader.getSuits(1), WIDTH - loader.getSuits(1).getWidth(), 0, null);
        canvas.drawBitmap(loader.getSuits(2), WIDTH - loader.getSuits(2).getWidth(), HEIGHT - loader.getSuits(2).getHeight(), null);
        canvas.drawBitmap(loader.getSuits(3), 0, HEIGHT - loader.getSuits(3).getHeight(), null);
    }

    @Override
    public synchronized void received(NetPkg data) {

        switch (data.getType()) {
            case NetPkg.PKG_DRAW_CARD:
                cards.add(new CardElement(((PkgDrawCard) data).card));
                v.vibrate(500);
                break;
            case NetPkg.PKG_FACE_CARD:
                playedCards.add(new CardElement(((PkgFaceCard) data).card));
                nextTurn(((PkgFaceCard) data).nextPlayer);
                break;
            case NetPkg.PKG_NEXT_TURN:
                nextTurn(((PkgNextTurn) data).playerId);
                break;
            case NetPkg.PKG_HANDSHAKE:
                if (!gameRunning) client.send(new PkgConnect(Settings.getNick(), -1));
                break;
            case NetPkg.PKG_START_GAME:
                gameRunning = true;
                gameStarting = false;
                break;
            case NetPkg.PKG_CONNECT:
                addPlayer((PkgConnect) data);
                break;
            case NetPkg.PKG_ALLOWED_THROWS:
                allowedThrows = ((PkgAllowedThrows)data).toElementArrayList();
                break;
            case NetPkg.PKG_WON:
                winner = ((PkgWon)data).playerid;
                break;
            default:
                Log.i("Mau", "Client received unknown package of type: " + data.getType());
        }
        canvasManager.postInvalidate();

    }

    private void nextTurn(int nextId) {
        currentPlayersTurn = nextId;
        Log.i("Mau", "Now player " + currentPlayersTurn + "s turn");
        if (currentPlayersTurn == yourId) {
            yourTurn = true;
        } else {
            yourTurn = false;
        }
    }

    private void addPlayer(PkgConnect data) {
        if (data.nickname.equalsIgnoreCase("#")) {
            yourId = data.id;
        } else {
            players.add(new PlayerInfo(data.id, data.nickname));
        }
    }


    @Override
    public synchronized void onTimeout() {

    }
}
