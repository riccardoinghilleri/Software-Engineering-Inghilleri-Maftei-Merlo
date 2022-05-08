package it.polimi.ingsw.server.model;

import it.polimi.ingsw.constants.Constants;
import it.polimi.ingsw.enums.PlayerColor;

import java.io.Serializable;

public class Tower implements Serializable {
    private final PlayerColor color;
    private final int ownerId;

    public Tower(int ownerId, PlayerColor color) {
        this.ownerId = ownerId;
        this.color = color;
    }

    public int getOwner() {
        return ownerId;
    }

    public PlayerColor getColor() {
        return color;
    }

    @Override
    public String toString() {
        return Constants.getAnsi(color)+"Д"+Constants.ANSI_RESET;
    }
}
