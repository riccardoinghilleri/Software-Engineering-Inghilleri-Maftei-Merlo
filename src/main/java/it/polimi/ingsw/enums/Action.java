package it.polimi.ingsw.enums;

import java.util.ArrayList;
import java.util.List;

public enum Action {
    SETUP_CLOUD,
    CHOOSE_ASSISTANT_CARD,
    CHOOSE_CHARACTER_CARD,
    USE_CHARACTER_CARD,
    DEFAULT_MOVEMENTS,
    GET_INFLUENCE,
    MOVE_MOTHER_NATURE,
    CHOOSE_CLOUD;

    public static List<Action> getDefaultActions() {
        List<Action> actions = new ArrayList<>();
        actions.add(DEFAULT_MOVEMENTS);
        actions.add(MOVE_MOTHER_NATURE);
        actions.add(CHOOSE_CLOUD);
        return actions;
    }
}
