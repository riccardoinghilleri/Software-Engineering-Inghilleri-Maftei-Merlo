package it.polimi.ingsw.server.model;

import it.polimi.ingsw.constants.Constants;
import it.polimi.ingsw.enums.PlayerColor;

import java.io.Serializable;

public class Tower implements Serializable {
    private final PlayerColor color;
    private final String owner;

    public Tower(String owner, PlayerColor color) {
        this.owner = owner;
        this.color = color;
    }

    public String getOwner() {
        return owner;
    }

    public PlayerColor getColor() {
        return color;
    }

    @Override
    public String toString() {
        return Constants.getAnsi(color)+"Д"+Constants.ANSI_RESET;
    }
}
