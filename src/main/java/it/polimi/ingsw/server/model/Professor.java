package it.polimi.ingsw.server.model;

import it.polimi.ingsw.constants.Constants;
import it.polimi.ingsw.enums.CharacterColor;

import java.io.Serializable;

/**
 * This class represents the Professor.
 * It has an owner and a color.
 */
public class Professor implements Serializable {
    private int owner;
    private final CharacterColor color;

    /**
     *The constructor of the class. The owner is initialized at -1, because
     * at the begging of the game nobody owns the professor.
     * @param color color of the professor.
     */
    public Professor(CharacterColor color) {
        owner = -1;
        this.color = color;
    }

    /** Getter of the owner */
    public int getOwner() {
        return owner;
    }

    /** Getter of the color*/
    public CharacterColor getColor() {
        return color;
    }

    /** This method sets the professor's owner
     * @param owner id of the player that owns the professor.
     * */
    public void setOwner(int owner) {
        this.owner = owner;
    }

    /**
     * This method overrides the toString method, since the professor
     * will be displayed on the screen as a colorful triangle.
     * @return a string, which is actually a colored triangle
     */

    @Override
    public String toString() {
        return Constants.getAnsi(color)+"▲"+Constants.ANSI_RESET;
    }
}
