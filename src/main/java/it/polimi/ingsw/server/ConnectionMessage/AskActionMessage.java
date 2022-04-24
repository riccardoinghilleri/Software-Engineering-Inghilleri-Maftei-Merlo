package it.polimi.ingsw.server.ConnectionMessage;

import it.polimi.ingsw.controller.Action;

public class AskActionMessage implements Message{
    private final String string;
    private final Action action;

    public AskActionMessage(String string, Action action) {
        this.string = string;
        this.action = action;
    }

    public String getString() {
        return string;
    }

    public Action getAction() {
        return action;
    }
}
