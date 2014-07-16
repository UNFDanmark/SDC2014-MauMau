package dk.unf.MauMau.ui;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by sdc on 7/15/14.
 */
public class AssetLoader {

    public final static int HEARTS_ID = 0;
    public final static int CLUBS_ID = 1;
    public final static int SPADES_ID = 2;
    public final static int DIAMONDS_ID = 3;
    private Map<Integer, Bitmap> heartsBitmaps = new HashMap<Integer, Bitmap>();
    private Map<Integer, Bitmap> clubsBitmaps = new HashMap<Integer, Bitmap>();
    private Map<Integer, Bitmap> spadesBitmaps = new HashMap<Integer, Bitmap>();
    private Map<Integer, Bitmap> diamondsBitmaps = new HashMap<Integer, Bitmap>();
    private Bitmap cardBack;
    private Bitmap background;


    public void load(Context context) {
        /*
        Loads all the image cards by using the method
        getBitmapFromAsset. The naming convention is c<cardValue><cardColor>
         */
        for (int j = 6; j < 13; j++) {
            for (int k = 0; k < 4; k++) {
                Bitmap bitmap = getBitmapFromAsset(context,  "c" + j + getLetter(k)+".png");
                System.out.println("c" + j + getLetter(k)+".png");
                bitmap = scaleDown(bitmap, 200, true);
                switch (k) {
                    case HEARTS_ID:
                        heartsBitmaps.put(j, bitmap);
                        break;
                    case CLUBS_ID:
                        clubsBitmaps.put(j, bitmap);
                        break;
                    case SPADES_ID:
                        spadesBitmaps.put(j, bitmap);
                        break;
                    case DIAMONDS_ID:
                        diamondsBitmaps.put(j, bitmap);
                        break;
                }
            }
        }
        background = getBitmapFromAsset(context,"BestBackground.png");
        cardBack = scaleDown(getBitmapFromAsset(context, "Red_Back.png"), 200, true); //Loads the cardback for the method getFaceDown
    }

    public Bitmap getBackground(int height){
        return scaleDown(background,height,true);
    }

    public Bitmap getCard(int cardValue, int cardColor) { //Gets a bitmap given a value and a color
        switch (cardColor) {
            case HEARTS_ID:
                return heartsBitmaps.get(cardValue);
            case CLUBS_ID:
                return clubsBitmaps.get(cardValue);
            case SPADES_ID:
                return spadesBitmaps.get(cardValue);
            case DIAMONDS_ID:
                return diamondsBitmaps.get(cardValue);
            default:
                Log.e("Mau", "You called an invalid cardColor");
                return null;
        }
    }


    private Bitmap getBitmapFromAsset(Context context, String filePath) {
        AssetManager assetManager = context.getAssets();

        InputStream istr;
        Bitmap bitmap = null;
        try {
            istr = assetManager.open(filePath);
            bitmap = BitmapFactory.decodeStream(istr);
        } catch (IOException e) {
            // handle exception
        }

        return bitmap;
    }

    public Bitmap getFaceDown(){ //cardBack is set in load()
        return cardBack;
    }

    public Bitmap scaleDown(Bitmap realImage, float maxImageSize,
                                   boolean filter) { //Scales down an image to a specific height and keeps the ratio
        float ratio = Math.min(
                maxImageSize / realImage.getWidth(),
                maxImageSize / realImage.getHeight());
        int width = Math.round(ratio * realImage.getWidth());
        int height = Math.round(ratio * realImage.getHeight());

        Bitmap newBitmap = Bitmap.createScaledBitmap(realImage, width,
                height, filter);
        return newBitmap;
    }

    private char getLetter(int index) { //Gets a card letter given an index
        switch (index) {
            case HEARTS_ID:
                return 'h';
            case CLUBS_ID:
                return 'c';
            case SPADES_ID:
                return 's';
            case DIAMONDS_ID:
                return 'd';
            default:
                Log.e("Mau", "The index provided was to large");
                return 'F';
        }
    }
}
