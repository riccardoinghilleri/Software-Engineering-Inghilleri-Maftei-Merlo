package it.polimi.ingsw.server.model;

import it.polimi.ingsw.enums.CharacterCardName;

/**
 * This class extends the CharacterCard class.
 * These type of card has prohibition tiles, saved in the
 * parameter 'prohibitionsNumber'
 */
public class CharacterCardwithProhibitions extends CharacterCard {
    private int prohibitionsNumber;

    /**
     * Constructor that call the super constructor of the CharacterCard class.
     * @param name: name of the card
     * @param cost : standard cost of the characterCard
     * @param description: description of the CharacterCard power.
     * @param prohibitionsNumber : number of prohibitions.
     */
    public CharacterCardwithProhibitions(CharacterCardName name, int cost, String description, int prohibitionsNumber) {
        super(name,cost,description);
        this.prohibitionsNumber=prohibitionsNumber;
    }

    /**
     * @return the number of prohibitions tiles available on the card.
     */
    public int getProhibitionsNumber() {
        return prohibitionsNumber;
    }

    /**
     * This method removes a prohibition from the card.
     * It throws an exception if the player wants to use a prohibition tiles when they are run out
     */
    public void subProhibitionCard() {
        if(prohibitionsNumber>0) prohibitionsNumber--;
    }

    /**
     * This method add a prohibition on the Card, whether the number of prohibition is less than  4.
     */
    public void restockProhibitionsNumber() {
        if(prohibitionsNumber<4) prohibitionsNumber++;
    }

}
