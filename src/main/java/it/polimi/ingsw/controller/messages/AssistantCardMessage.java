package it.polimi.ingsw.controller.messages;

import it.polimi.ingsw.controller.Action;

public class AssistantCardMessage implements Message{
    private final Action action;
    private final int priority;

    public AssistantCardMessage(int priority) {
        this.action = Action.CHOOSE_ASSISTANT_CARD;
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
