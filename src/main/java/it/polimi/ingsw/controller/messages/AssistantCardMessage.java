package it.polimi.ingsw.controller.messages;

import it.polimi.ingsw.controller.Action;

public class AssistantCardMessage implements Message{
    private final Action action;
    private final int priority;

    public AssistantCardMessage(Action action, int priority) {
        this.action = action;
        this.priority = priority;
    }

    @Override
    public Action getAction() {
        return action;
    }

    public int getPriority() {
        return priority;
    }
}
