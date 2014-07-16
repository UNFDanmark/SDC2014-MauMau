package dk.unf.MauMau.game;

import java.util.ArrayList;

/**
 * Created by sdc on 7/15/14.
 */
public class Player {

    private int id;
    private String nick;
    private ArrayList<Card> cards = new ArrayList<Card>();

    public Player(int id, String nick, ArrayList<Card> cards) {
        this.id = id;
        this.nick = nick;
        this.cards = cards;
    }

    public int getId() {
        return id;
    }
}
