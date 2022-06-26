package it.polimi.ingsw.server.model;


import it.polimi.ingsw.enums.CharacterCardName;
import java.io.Serializable;

/**
 * This class represents the CharacterCard, which can be played only if the Game mode is expert
 * It has a cost, a description which tells the player what he can do additionally if he chooses the card, and the name
 */

public class CharacterCard implements Serializable {
    private int cost;
    private final String description;
    private final CharacterCardName name;

    /**
     * The constructor of the CharacterCard
     */
    public CharacterCard(CharacterCardName name, int cost, String description) {
        this.name = name;
        this.cost = cost;
        this.description = description;
    }

    /**
     * This method returns an instance of enum CharacterCardName
     */
    public CharacterCardName getName() {
        return name;
    }
    /**
     * This method returns the cost of the card
     */
    public int getCost() {
        return cost;
    }
    /**
     * This method returns a string which describes the power of the card
     */
    public String getDescription() {
        return description;
    }

    /**
     * This method increases the cost of the card
     */
    public void updateCost() {
        this.cost++;
    }

}
