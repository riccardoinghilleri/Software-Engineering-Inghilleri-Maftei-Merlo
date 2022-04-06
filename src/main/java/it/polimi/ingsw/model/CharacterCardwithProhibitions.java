package it.polimi.ingsw.model;

import it.polimi.ingsw.model.enums.CharacterCardName;

public class CharacterCardwithProhibitions extends CharacterCard {
    private int prohibitionsNumber;

    public CharacterCardwithProhibitions(CharacterCardName name, int cost, String description, int prohibitionsNumber) {
        super(name,cost,description);
        this.prohibitionsNumber=prohibitionsNumber;
    }

    public int getProhibitionsNumber() {
        return prohibitionsNumber;
    }

    public void subProhibitionCard() {
        //TODO lanciare un'eccezione se sono finite le tessere divieto e il client vuole usarne una
        if(prohibitionsNumber>0) prohibitionsNumber--;
    }
    public void restockProhibitionsNumber() {
        if(prohibitionsNumber<4) prohibitionsNumber++;
    }
}
