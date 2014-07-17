package dk.unf.MauMau.ui;

import dk.unf.MauMau.game.Player;

/**
 * Created by sdc on 7/17/14.
 */
public class PlayerInfo {

    private int id;
    private String nick;
    private int cards;

    public PlayerInfo(int id, String nick) {
        this.id = id;
        this.nick = nick;
        cards = 0;
    }

    public String getNick() {
        return nick;
    }

    public int getId() {
        return id;
    }

    public int getCards() {
        return cards;
    }

}
