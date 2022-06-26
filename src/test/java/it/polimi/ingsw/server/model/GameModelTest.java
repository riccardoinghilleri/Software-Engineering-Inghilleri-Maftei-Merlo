package it.polimi.ingsw.server.model;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class GameModelTest {
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
        assertTrue(gameModelExpert.isExpertGame());
        assertFalse(gameModelNormal.isExpertGame());
        assertTrue(gameModelExpert.getPlayers().isEmpty());
        assertNotNull(gameModelExpert.getPlayers());
        assertNull(gameModelExpert.getWinner());
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
        assertEquals("Manu",gameModelExpert.getPlayerById(2).getNickname());
        assertEquals(2,gameModelExpert.getPlayerById(2).getClientID());
        assertEquals("Dani",gameModelNormal.getPlayerById(1).getNickname());
        assertEquals(1,gameModelNormal.getPlayerById(1).getClientID());
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
        assertNull(gameModelExpert.getPlayerById(1).getDeck().getWizard());
        gameModelExpert.setPlayerDeck(1, "GANDALF");
        assertEquals("GANDALF", gameModelExpert.getPlayerById(1).getDeck().getWizard().toString());
    }

    @Test
    public void testSetWinner() {
        Player player = new Player("Ricky", 0);
        gameModelExpert.setWinner(player);
        assertEquals("Ricky",gameModelExpert.getWinner().getNickname());
        assertEquals(0,gameModelExpert.getWinner().getClientID());
    }

}
