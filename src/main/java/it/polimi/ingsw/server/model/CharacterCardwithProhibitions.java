package it.polimi.ingsw.server.model;

import it.polimi.ingsw.enums.CharacterCardName;

/**
 * This class extends the basic CharacterCard.
 * These types of cards have several prohibition tiles, presented  in the
 * parameter 'prohibitionsNumber'
 */
public class CharacterCardwithProhibitions extends CharacterCard {
    private int prohibitionsNumber;

    /**
     * Constructor calling the super constructor of the CharacterCard class
     */
    public CharacterCardwithProhibitions(CharacterCardName name, int cost, String description, int prohibitionsNumber) {
        super(name,cost,description);
        this.prohibitionsNumber=prohibitionsNumber;
    }

    /**
     * @return the number of the prohibitions tiles available on the card
     */
    public int getProhibitionsNumber() {
        return prohibitionsNumber;
    }

    /**
     * This method removes a prohibition from the card.
     * It throws an exception if the player wants to use a prohibition tiles when they are run out
     */
    public void subProhibitionCard() {
        //TODO lanciare un'eccezione se sono finite le tessere divieto e il client vuole usarne una
        if(prohibitionsNumber>0) prohibitionsNumber--;
    }

    /**
     * This method refreshes the initial number of prohibitions on the Card,
     * adding tiles until their number is not equal to 4.
     */
    public void restockProhibitionsNumber() {
        if(prohibitionsNumber<4) prohibitionsNumber++;
    }

}
