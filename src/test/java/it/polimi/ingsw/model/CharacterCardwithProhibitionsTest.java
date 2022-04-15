package it.polimi.ingsw.model;

import it.polimi.ingsw.model.enums.CharacterCardName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CharacterCardwithProhibitionsTest {

    @Test
    void testConstructorCharacterCardWithProhibitions(){
        CharacterCardwithProhibitions card= new CharacterCardwithProhibitions(CharacterCardName.QUEEN,2,"Descrizione a caso",3);
        assertEquals(CharacterCardName.QUEEN, card.getName());
        assertEquals(2, card.getCost());
        assertEquals("Descrizione a caso", card.getDescription());
        assertEquals(3, card.getProhibitionsNumber());
    }
    @Test
    //TODO manca Exception
     void testSubProhibitionCard(){
        CharacterCardwithProhibitions card= new CharacterCardwithProhibitions(CharacterCardName.QUEEN,2,"Descrizione a caso",3);
        card.subProhibitionCard();
        assertEquals(2,card.getProhibitionsNumber());
    }
    @Test
    //forse questo da sistemare
    void testRestock(){
        CharacterCardwithProhibitions card= new CharacterCardwithProhibitions(CharacterCardName.QUEEN,2,"Descrizione a caso",3);
          card.restockProhibitionsNumber();
          assertEquals(4,card.getProhibitionsNumber());
          card.restockProhibitionsNumber();
          assertNotEquals(5,card.getProhibitionsNumber());
    }


}