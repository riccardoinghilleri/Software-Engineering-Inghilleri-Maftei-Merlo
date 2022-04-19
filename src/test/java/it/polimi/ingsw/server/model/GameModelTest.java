package it.polimi.ingsw.server.model;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class GameModelTest {

    //TODO ho creato 2 game model. Quello normal ha solo 1 player, serve a testare le funzionalità anche nella modalità normale. ha senso o bisogna toglierlo??

    GameModel gameModelExpert;
    GameModel gameModelNormal;

    @BeforeEach
    public void setUp() {
        gameModelExpert = new GameModel(true);
        gameModelNormal = new GameModel(false);
    }

    @Test
    public void testGameModel() {
        assertEquals(0,gameModelExpert.getPlayersNumber());
        assertTrue(gameModelExpert.isExpertGame);
        assertFalse(gameModelNormal.isExpertGame);
        assertTrue(gameModelExpert.getPlayers().isEmpty());
        assertNotNull(gameModelExpert.getPlayers());
        assertTrue(gameModelExpert.getWinners().isEmpty());
        assertNotNull(gameModelExpert.getWinners());
    }

    @Test
    public void testCreateBoard() {
        assertNull(gameModelExpert.getBoard());
        assertNull(gameModelNormal.getBoard());
        gameModelExpert.createBoard();
        gameModelNormal.createBoard();
        assertNotNull(gameModelExpert.getBoard());
        assertNotNull(gameModelNormal.getBoard());
        assertTrue(gameModelExpert.getBoard() instanceof BoardExpert);
        assertFalse(gameModelNormal.getBoard() instanceof BoardExpert);
    }

    @Test
    public void testCreatePlayer() {
        assertTrue(gameModelExpert.getPlayers().isEmpty());
        assertTrue(gameModelNormal.getPlayers().isEmpty());
        gameModelExpert.createPlayer("Ricky",1);
        gameModelExpert.createPlayer("Manu",2);
        gameModelNormal.createPlayer("Dani",1);
        assertEquals(2,gameModelExpert.getPlayers().size());
        assertEquals(1,gameModelNormal.getPlayers().size());
    }

    @Test
    public void testIsExpertGame() {
        assertTrue(gameModelExpert.isExpertGame());
        assertFalse(gameModelNormal.isExpertGame());
    }

    @Test
    public void testGetCurrentPlayer() {
        gameModelExpert.createPlayer("Ricky",1);
        gameModelExpert.createPlayer("Manu",2);
        gameModelNormal.createPlayer("Dani",1);
        gameModelExpert.setCurrentPlayer(1);
        gameModelNormal.setCurrentPlayer(0);
        assertEquals(gameModelExpert.getCurrentPlayer().getClientID(),2);
        assertEquals(gameModelNormal.getCurrentPlayer().getClientID(),1);
    }

    @Test
    public void testGetPlayerByNickname() {
        gameModelExpert.createPlayer("Ricky",1);
        gameModelExpert.createPlayer("Manu",2);
        gameModelNormal.createPlayer("Dani",1);
        assertEquals("Manu",gameModelExpert.getPlayerByNickname("Manu").getNickname());
        assertEquals(2,gameModelExpert.getPlayerByNickname("Manu").getClientID());
        assertEquals("Dani",gameModelNormal.getPlayerByNickname("Dani").getNickname());
        assertEquals(1,gameModelNormal.getPlayerByNickname("Dani").getClientID());
    }

    @Test
    public void testSetPlayersOrder() {
        gameModelExpert.createPlayer("Ricky",1);
        gameModelExpert.createPlayer("Manu",2);
        gameModelNormal.createPlayer("Dani",1);
        assertEquals("Ricky",gameModelExpert.getPlayers().get(0).getNickname());
        assertEquals("Manu",gameModelExpert.getPlayers().get(1).getNickname());
        gameModelExpert.getPlayers().get(0).setAssistantCard(3);
        gameModelExpert.getPlayers().get(1).setAssistantCard(2);
        gameModelExpert.setPlayersOrder();
        assertEquals("Manu",gameModelExpert.getPlayers().get(0).getNickname());
        assertEquals("Ricky",gameModelExpert.getPlayers().get(1).getNickname());
    }

    @Test
    public void testSetPlayerDeck() {
        gameModelExpert.createPlayer("Ricky",1);
        gameModelExpert.createPlayer("Manu",2);
        gameModelNormal.createPlayer("Dani",1);
        assertNull(gameModelExpert.getPlayerByNickname("Ricky").getDeck().getWizard());
        gameModelExpert.setPlayerDeck("Ricky", "GANDALF");
        assertEquals("GANDALF", gameModelExpert.getPlayerByNickname("Ricky").getDeck().getWizard().toString());
    }

    @Test
    public void testSetWinner() {
        gameModelExpert.setWinner("Ricky");
        assertEquals("Ricky",gameModelExpert.getWinners().get(0));
    }

}
