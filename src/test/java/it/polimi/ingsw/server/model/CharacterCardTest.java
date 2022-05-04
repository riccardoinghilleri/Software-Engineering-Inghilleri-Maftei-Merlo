package it.polimi.ingsw.server.model;

import it.polimi.ingsw.enums.CharacterCardName;
import org.junit.jupiter.api.Test;

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
