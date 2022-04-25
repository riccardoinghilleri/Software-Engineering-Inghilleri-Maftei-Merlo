package it.polimi.ingsw.server.ConnectionMessage;

import it.polimi.ingsw.controller.Action;
import it.polimi.ingsw.server.model.AssistantCard;
import it.polimi.ingsw.server.model.Student;

import java.util.ArrayList;
import java.util.List;

public class AskActionMessage implements Message {
    private final Action action;
    private final int data;
    private final List<?> availability;

    public AskActionMessage(Action action, List<?> availability) {
        this.action = action;
        data = -1;
        this.availability = new ArrayList<>(availability);
    }

    public AskActionMessage(Action action, List<?> availability, int data) {
        this.action = action;
        this.data = data;
        this.availability = new ArrayList<>(availability);

    }

    public AskActionMessage(Action action, int data) {
        this.action = action;
        this.data = data;
        this.availability = null;
    }

    public int getData() {
        return data;
    }

    public List<?> getAvailability() {
        return availability;
    }

    public Action getAction() {
        return action;
    }
}
