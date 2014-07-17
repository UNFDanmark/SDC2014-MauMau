package dk.unf.MauMau.network.NetPkg;

/**
 * Created by sdc on 7/16/14.
 */
public class PkgUpdatePlayerStats implements NetPkg {

    public final int playerCount;
    public final int id[];
    public final int cardCount[];

    public PkgUpdatePlayerStats(int playerCount, int id[], int cardCount[]) {
        this.playerCount = playerCount;
        this.id = id;
        this.cardCount = cardCount;
    }

    public PkgUpdatePlayerStats(String input) {
        String[] parts = input.split(":");
        playerCount = Integer.parseInt(parts[0]);
        int[] idArray = new int[playerCount];
        int[] countArray = new int [playerCount];
        if (parts.length == playerCount*2+1) {
            for (int i = 0; i < playerCount; i++) {
                idArray[i] = Integer.parseInt(parts[i+1]);
            }
            for (int i = 0; i < playerCount; i++) {
                countArray[i] = Integer.parseInt(parts[i+playerCount+1]);
            }
        }
        id = idArray;
        cardCount = countArray;
    }

    @Override
    public int getType() {
        return NetPkg.PKG_UPDATE_PLAYER_STATS;
    }

    @Override
    public String serialize() {
        return null;
    }

}
