package it.polimi.ingsw.enums;

import java.util.ArrayList;
import java.util.List;

/**
 * This class contains the names of the wizards of each deck
 */
public enum Wizard {
    FAIRY,
    GANDALF,
    KING,
    WISE;

    /**
     * This method creates a list with all the wizards, to be used when displaying the available wizards
     */
    public static List<Wizard> getWizards() {
        ArrayList<Wizard> available = new ArrayList<>();
        available.add(FAIRY);
        available.add(GANDALF);
        available.add(KING);
        available.add(WISE);
        return available;
    }
}
