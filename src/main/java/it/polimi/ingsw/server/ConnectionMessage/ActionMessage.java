package it.polimi.ingsw.server.ConnectionMessage;


import it.polimi.ingsw.client.Cli;
import it.polimi.ingsw.controller.Action;

import java.util.List;

public class ActionMessage implements Message{
    private Action action;
    private String characterCardName;
    private int data;
    private String firstParameter;
    private String secondParameter;

    private List<String> parameters;

    public ActionMessage() {
        this.data = -1; //valore di default
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

    public String getFirstParameter() {
        return firstParameter;
    }

    public void setFirstParameter(String firstParameter) {
        this.firstParameter = firstParameter;
    }

    public String getSecondParameter() {
        return secondParameter;
    }

    public void setSecondParameter(String secondParameter) {
        this.secondParameter = secondParameter;
    }
}
