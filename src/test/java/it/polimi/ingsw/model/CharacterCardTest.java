package it.polimi.ingsw.model;

import it.polimi.ingsw.model.enums.CharacterCardName;
import it.polimi.ingsw.model.enums.PlayerColor;
import org.junit.jupiter.api.Test;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

class CharacterCardTest {

    @Test
    void testCostruttore(){
        CharacterCard card= new CharacterCard( CharacterCardName.KNIGHT ,2," descrizione di prova di una stringa");
        assertEquals(CharacterCardName.KNIGHT, card.getName());
        assertEquals(" descrizione di prova di una stringa", card.getDescription());
        assertEquals(2, card.getCost());


    }
    @Test
    void testUpdateCost(){

        CharacterCard card= new CharacterCard( CharacterCardName.LUMBERJACK ,2," un'altra descrizione di prova di una stringa");
        card.updateCost();
        assertEquals(3,card.getCost());
    }
}
