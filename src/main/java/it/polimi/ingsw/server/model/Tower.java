package it.polimi.ingsw.server.model;

import it.polimi.ingsw.constants.Constants;
import it.polimi.ingsw.enums.PlayerColor;

import java.io.Serializable;

/**
 * This class represents the element 'tower' in the board.
 * It has a color and an owner.
 */
public class Tower implements Serializable {
    private final PlayerColor color;
    private final int ownerId;

    /**
     * Constructor of the class
     */
    public Tower(int ownerId, PlayerColor color) {
        this.ownerId = ownerId;
        this.color = color;
    }

    /**
     * @return the id of the tower's owner.
     */
    public int getOwner() {
        return ownerId;
    }

    /**
     * @return the color of the tower.
     */
    public PlayerColor getColor() {
        return color;
    }

    /**
     * Override of the toString method.
     * The tower will be displayed on the screen as a colorful symbol from the AnsiCode.
     */
    @Override
    public String toString() {
        return Constants.getAnsi(color)+"Д"+Constants.ANSI_RESET;
    }
}
