package it.polimi.ingsw.controller.messages;

import it.polimi.ingsw.controller.Action;

public class NatureMotherMessage {
    private final Action action;
    private final int choosenSteps;

    public NatureMotherMessage(int choosenSteps) {
        this.action = Action.MOVE_NATURE_MOTHER;
        this.choosenSteps = choosenSteps;
    }

    public Action getAction() {
        return action;
    }

    public int getChoosenSteps() {
        return choosenSteps;
    }

}
