package it.polimi.ingsw.server.model.enums;

import java.util.ArrayList;
import java.util.List;

public enum PlayerColor {
    BLACK,
    WHITE,
    GREY;

    private static final ArrayList<PlayerColor> available = new ArrayList<>();

    public static void reset(int playersNumber) {
        available.clear();
        available.add(BLACK);
        available.add(WHITE);
        if(playersNumber==3) available.add(GREY);
    }

    public static void choose(PlayerColor PlayerColor) {
        available.remove(PlayerColor);
    }
    public static List<PlayerColor> notChosen() {
        return available;
    }
}
