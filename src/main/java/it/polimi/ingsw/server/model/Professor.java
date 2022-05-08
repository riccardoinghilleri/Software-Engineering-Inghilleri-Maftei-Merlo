package it.polimi.ingsw.server.model;

import it.polimi.ingsw.constants.Constants;
import it.polimi.ingsw.enums.CharacterColor;

import java.io.Serializable;

public class Professor implements Serializable {
    private int owner;
    private final CharacterColor color;

    public Professor(CharacterColor color) {
        owner = -1;
        this.color = color;
    }

    public int getOwner() {
        return owner;
    }

    public CharacterColor getColor() {
        return color;
    }

    public void setOwner(int owner) {
        this.owner = owner;
    }

    @Override
    public String toString() {
        return Constants.getAnsi(color)+"▲"+Constants.ANSI_RESET;
    }
}
