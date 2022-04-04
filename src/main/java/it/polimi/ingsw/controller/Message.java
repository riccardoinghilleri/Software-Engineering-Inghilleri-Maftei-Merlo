package it.polimi.ingsw.controller;

import it.polimi.ingsw.controller.Action;

public class Message {
    private final Action action;
    private String characterCardName;
    private int data;
    private String firstParameter;
    private String secondParameter;

    public Message(Action action) {
        this.action = action;
        this.data = -1; //valore di default
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
