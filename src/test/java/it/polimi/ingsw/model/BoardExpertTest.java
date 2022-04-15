package it.polimi.ingsw.model;

import it.polimi.ingsw.model.enums.CharacterCardName;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class BoardExpertTest {
    List<Player> players = new ArrayList<Player>();
    Board boardExpert;

    @BeforeEach
    void setUp() {
        players=new ArrayList<>();
        players.add(new Player("Ricky",1));
        players.add(new Player("Manu",2));
        boardExpert= new BoardExpert(players,new GameModel(true));
    }

    @Test
    public void testBoardExpert() {
        assertEquals(18,((BoardExpert)boardExpert).getBoardCoins());
        assertEquals(1,((BoardExpert) boardExpert).getPlayerCoins("Ricky"));
        assertEquals(1,((BoardExpert) boardExpert).getPlayerCoins("Manu"));
        assertEquals(3,((BoardExpert) boardExpert).getCharacterCards().length);
    }

    @Test
    public void testGetCharacterCardbyName() {
        CharacterCard[] characterCards = ((BoardExpert) boardExpert).getCharacterCards();
        assertEquals(characterCards[0].getName(),((BoardExpert)boardExpert).getCharacterCardbyName(characterCards[0].getName().toString()).getName());
        assertEquals(characterCards[0].getCost(),((BoardExpert)boardExpert).getCharacterCardbyName(characterCards[0].getName().toString()).getCost());
        assertEquals(characterCards[0].getDescription(),((BoardExpert)boardExpert).getCharacterCardbyName(characterCards[0].getName().toString()).getDescription());
    }

    @Test
    public void testMoveCoin() {
        //TODO ho testato solo 1 carta (DIPLOMAT)
        ((BoardExpert)boardExpert).addCointoPlayer("Ricky");
        ((BoardExpert)boardExpert).addCointoPlayer("Ricky");
        ((BoardExpert)boardExpert).addCointoPlayer("Ricky");
        ((BoardExpert)boardExpert).addCointoPlayer("Ricky");
        ((BoardExpert)boardExpert).addCointoPlayer("Ricky");
        assertEquals(6,((BoardExpert) boardExpert).getPlayerCoins("Ricky"));
        assertEquals(13,((BoardExpert) boardExpert).getBoardCoins());
        CharacterCard characterCard = new CharacterCard(CharacterCardName.valueOf("DIPLOMAT"),3,"Scegli un'isola " +
                "e calcola la maggioranza come se Madre Natura avesse terminato il suo movimento lì." +
                "In questo turno Madre Natura si muoverà come di consueto e nell'isola dove terminerà il suo movimento" +
                "la maggioranza verrà normalmente calcolata.");
        ((BoardExpert) boardExpert)
                .moveCoin("Ricky", characterCard);
        assertEquals(3,((BoardExpert) boardExpert).getPlayerCoins("Ricky"));
        assertEquals(15,((BoardExpert) boardExpert).getBoardCoins());
        assertEquals(4,characterCard.getCost());

    }

    @Test
    public void testRemoveNoEntryTiles() {
        assertFalse(boardExpert.getIslands().get(0).hasNoEntryTile());
        boardExpert.getIslands().get(0).setNoEntryTile(true);
        assertTrue(boardExpert.getIslands().get(0).hasNoEntryTile());
        ((BoardExpert) boardExpert).removeNoEntryTiles(0);
        assertFalse(boardExpert.getIslands().get(0).hasNoEntryTile());
    }

    @Test
    public void testCreateThreeRandomCharacterCards() {
        //TODO non so come testarla..

    }


    @AfterEach
    void tearDown() {
    }
}