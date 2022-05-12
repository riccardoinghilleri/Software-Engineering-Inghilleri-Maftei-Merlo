package it.polimi.ingsw.enums;

import java.util.ArrayList;
import java.util.List;

public enum PlayerColor {
    BLACK,
    WHITE,
    GREY;

    public static List<PlayerColor> getColors(int playersNumber) {
        ArrayList<PlayerColor> available = new ArrayList<>();
        available.add(BLACK);
        available.add(WHITE);
        if (playersNumber == 3) available.add(GREY);
        if (playersNumber == 4) {
            available.add(BLACK);
            available.add(WHITE);
        }
        return available;
    }
}
