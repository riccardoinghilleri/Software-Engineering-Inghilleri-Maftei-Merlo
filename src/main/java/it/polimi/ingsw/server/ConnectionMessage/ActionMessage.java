package it.polimi.ingsw.server.ConnectionMessage;

import it.polimi.ingsw.controller.Action;

import java.util.ArrayList;
import java.util.List;

public class ActionMessage implements Message{
    private Action action;
    private String characterCardName;
    private int data;
    private final List<String> parameters;

    public ActionMessage() {
        this.data = -1; //valore di default
        this.parameters = new ArrayList<>();
    }

    public void setAction(Action action) {
        this.action = action;
    }

    public Action getAction() {
        return action;
    }

    public String getCharacterCardName() {
        return characterCardName;
    }

    public void setCharacterCardName(String characterCardName) {
        this.characterCardName = characterCardName;
    }

    public List<String> getParameters() {
        return parameters;
    }

    public void setParameter(String parameter) {
        this.parameters.add(parameter);
    }

    public int getData() {
        return data;
    }

    public void setData(int data) {
        this.data = data;
    }
}
