package it.polimi.ingsw.server.model.enums;

import java.util.ArrayList;
import java.util.List;

public enum Wizard {
    FAIRY,
    GANDALF,
    KING,
    WISE;

    private static final ArrayList<Wizard> available = new ArrayList<>();

    public static void reset() {
        available.clear();
        available.add(FAIRY);
        available.add(GANDALF);
        available.add(KING);
        available.add(WISE);
    }

    public static void choose(Wizard wizard) {
        available.remove(wizard);
    }

    public static List<Wizard> notChosen() {
        return available;
    }
}
