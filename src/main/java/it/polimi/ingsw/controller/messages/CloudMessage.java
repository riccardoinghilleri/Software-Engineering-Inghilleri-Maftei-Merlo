package it.polimi.ingsw.controller.messages;

import it.polimi.ingsw.controller.Action;

public class CloudMessage {
    private final Action action;
    private final int cloudPosition;

    public CloudMessage(int cloudPosition) {
        this.action = Action.CHOOSE_CLOUD;
        this.cloudPosition = cloudPosition;
    }

    public Action getAction() {
        return action;
    }

    public int getCloudPosition() {
        return cloudPosition;
    }
}
