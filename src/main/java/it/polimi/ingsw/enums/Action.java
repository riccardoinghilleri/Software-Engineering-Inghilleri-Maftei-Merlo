package it.polimi.ingsw.enums;

import java.util.ArrayList;
import java.util.List;

/**
 * This class presents the actions a player can do in his turn.
 * Directly used by the controller
 */
public enum Action {
    SETUP_CLOUD,
    CHOOSE_ASSISTANT_CARD,
    CHOOSE_CHARACTER_CARD,
    USE_CHARACTER_CARD,
    DEFAULT_MOVEMENTS,
    GET_INFLUENCE,
    MOVE_MOTHER_NATURE,
    CHOOSE_CLOUD;

    /**
     * This method return a list of 3 actions, used at the begging of the game to show the available actions
     */
    public static List<Action> getDefaultActions() {
        List<Action> actions = new ArrayList<>();
        actions.add(DEFAULT_MOVEMENTS);
        actions.add(MOVE_MOTHER_NATURE);
        actions.add(CHOOSE_CLOUD);
        return actions;
    }
}
