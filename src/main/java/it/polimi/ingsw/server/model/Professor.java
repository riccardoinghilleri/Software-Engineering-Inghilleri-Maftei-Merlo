package it.polimi.ingsw.server.model;

import it.polimi.ingsw.constants.Constants;
import it.polimi.ingsw.enums.CharacterColor;

import java.io.Serializable;

public class Professor implements Serializable {
    private String owner;
    private final CharacterColor color;

    public Professor(CharacterColor color) {
        owner = "NONE";
        this.color = color;
    }

    public String getOwner() {
        return owner;
    }

    public CharacterColor getColor() {
        return color;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    @Override
    public String toString() {
        return Constants.getAnsi(color)+"▲"+Constants.ANSI_RESET;
    }
}
