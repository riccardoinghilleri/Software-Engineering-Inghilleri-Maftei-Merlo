package it.polimi.ingsw.enums;

import java.util.ArrayList;
import java.util.List;

public enum Wizard {
    FAIRY,
    GANDALF,
    KING,
    WISE;

    public static List<Wizard> getWizards() {
        ArrayList<Wizard> available = new ArrayList<>();
        available.add(FAIRY);
        available.add(GANDALF);
        available.add(KING);
        available.add(WISE);
        return available;
    }
}
