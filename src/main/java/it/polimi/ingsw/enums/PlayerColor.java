package it.polimi.ingsw.enums;

import java.util.ArrayList;
import java.util.List;

/**
 * This class contains the colors available for the players
 */

public enum PlayerColor {
    BLACK,
    WHITE,
    GREY;

    /**
     * This method creates a list with Black and White when the players are 2. So the choice includes only 2 options
     * It adds the third color when the numPlayer is 3.
     */
    public static List<PlayerColor> getColors(int playersNumber) {
        ArrayList<PlayerColor> available = new ArrayList<>();
        available.add(BLACK);
        available.add(WHITE);
        if (playersNumber == 3) available.add(GREY);
        return available;
    }
}
