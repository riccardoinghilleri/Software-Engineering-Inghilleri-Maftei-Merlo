package it.polimi.ingsw.controller.messages;

import it.polimi.ingsw.controller.Action;

public class CloudMessage {
    private final Action action;
    private final int cloudPosition;

    public CloudMessage(Action action, int cloudPosition) {
        this.action = action;
        this.cloudPosition = cloudPosition;
    }

    public Action getAction() {
        return action;
    }

    public int getCloudPosition() {
        return cloudPosition;
    }
}
