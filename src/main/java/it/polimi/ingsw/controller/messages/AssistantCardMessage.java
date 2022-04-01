package it.polimi.ingsw.controller.messages;

import it.polimi.ingsw.controller.Action;

public class AssistantCardMessage implements Message{
    private Action action;
    private int priority;

    public AssistantCardMessage(int priority) {
        this.priority = priority;
        //TODO come gestiamo l'attributo action?? è final??
    }

    @Override
    public Action getAction() {
        return action;
    }

    public int getPriority() {
        return priority;
    }
}
