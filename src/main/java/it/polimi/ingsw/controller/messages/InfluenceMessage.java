package it.polimi.ingsw.controller.messages;

import it.polimi.ingsw.controller.Action;

public class InfluenceMessage implements Message {
    private final Action action;
    private String data;
    private int islandPosition;

    public InfluenceMessage() {
        this.action = Action.GET_INFLUENCE;
    }

    @Override
    public Action getAction() {
        return action;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public int getIslandPosition() {
        return islandPosition;
    }

    public void setIslandPosition(int islandPosition) {
        this.islandPosition = islandPosition;
    }
}
