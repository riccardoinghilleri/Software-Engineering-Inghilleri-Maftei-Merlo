package it.polimi.ingsw.server.model;

import it.polimi.ingsw.enums.CharacterCardName;
import it.polimi.ingsw.enums.CharacterColor;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BoardExpertTest {
    Board boardExpert;
    GameModel gameModel;

    @BeforeEach
    void setUp() {
        gameModel = new GameModel(true);
        gameModel.createPlayer("Ricky", 0);
        gameModel.createPlayer("Manu", 1);
        gameModel.getPlayerById(0).setColor("WHITE");
        gameModel.getPlayerById(1).setColor("BLACK");
        boardExpert = new BoardExpert(gameModel);
    }

    @Test
    public void testBoardExpert() {
        assertEquals(18, ((BoardExpert) boardExpert).getBoardCoins());
        assertEquals(1, ((BoardExpert) boardExpert).getPlayerCoins(0));
        assertEquals(1, ((BoardExpert) boardExpert).getPlayerCoins(1));
        assertEquals(3, ((BoardExpert) boardExpert).getCharacterCards().length);
    }

    @Test
    public void testGetCoins() {
        int[] playerCoins = {1, 1};
        assertEquals(playerCoins[0], ((BoardExpert) boardExpert).getCoins()[0]);
        assertEquals(playerCoins[1], ((BoardExpert) boardExpert).getCoins()[1]);
        ((BoardExpert) boardExpert).addCointoPlayer(0);
        playerCoins[0]++;
        assertEquals(playerCoins[0], ((BoardExpert) boardExpert).getCoins()[0]);
    }


    @Test
    public void testGetCharacterCardByName() {
        CharacterCard[] characterCards = ((BoardExpert) boardExpert).getCharacterCards();
        assertEquals(characterCards[0].getName(), ((BoardExpert) boardExpert).getCharacterCardbyName(characterCards[0].getName().toString()).getName());
        assertEquals(characterCards[0].getCost(), ((BoardExpert) boardExpert).getCharacterCardbyName(characterCards[0].getName().toString()).getCost());
        assertEquals(characterCards[0].getDescription(), ((BoardExpert) boardExpert).getCharacterCardbyName(characterCards[0].getName().toString()).getDescription());
    }

    @Test
    public void testMoveCoin() {
        //TODO ho testato solo 1 carta (DIPLOMAT)
        ((BoardExpert) boardExpert).addCointoPlayer(0);
        ((BoardExpert) boardExpert).addCointoPlayer(0);
        ((BoardExpert) boardExpert).addCointoPlayer(0);
        ((BoardExpert) boardExpert).addCointoPlayer(0);
        ((BoardExpert) boardExpert).addCointoPlayer(0);
        assertEquals(6, ((BoardExpert) boardExpert).getPlayerCoins(0));
        assertEquals(13, ((BoardExpert) boardExpert).getBoardCoins());
        CharacterCard characterCard = new CharacterCard(CharacterCardName.valueOf("DIPLOMAT"), 3, "Scegli un'isola " +
                "e calcola la maggioranza come se Madre Natura avesse terminato il suo movimento lì." +
                "In questo turno Madre Natura si muoverà come di consueto e nell'isola dove terminerà il suo movimento" +
                "la maggioranza verrà normalmente calcolata.");
        ((BoardExpert) boardExpert)
                .moveCoin(0, characterCard);
        assertEquals(3, ((BoardExpert) boardExpert).getPlayerCoins(0));
        assertEquals(15, ((BoardExpert) boardExpert).getBoardCoins());
        assertEquals(4, characterCard.getCost());

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
    public void testCreateThreeCharacterCards() {
        CharacterCardName[] values = CharacterCardName.values();
        CharacterCard[] cards;
        for (int i = 1; i < 5; i++) {
            ((BoardExpert)boardExpert).createThreeCharacterCards(i);
            cards = ((BoardExpert) boardExpert).getCharacterCards();
            CharacterCardName name0 = cards[0].getName();
            CharacterCardName name1 = cards[1].getName();
            CharacterCardName name2 = cards[2].getName();
            assertSame(name0, values[3 * (i - 1)]);
            assertSame(name1, values[i * 2 + (i-2)]);
            assertSame(name2, values[i*2+(i-1)]);
            assertNotSame(name1, name2);
            assertNotSame(name0, name2);
            assertNotSame(name0, name1);
        }
    }

    @Test
    public void testCheckNearIsland() {
        BoardExpert board = (BoardExpert) boardExpert;
        int num_students = 0;
        assertEquals(12, board.getIslands().size());

        // [ISLAND 0 - 1 DIVIENTO - 2 TW] [ISLAND 1 - 1 TW] [ISLAND 2 - 2 DIVIENTI - 1 TW]
        board.moveTower(0, 0, "island");
        board.moveTower(0, 1, "island");
        board.moveTower(0, 2, "island");
        board.getIslands().get(2).setNoEntryTile(true);
        board.getIslands().get(2).setNoEntryTile(true);
        board.getIslands().get(0).setNoEntryTile(true);
        assertEquals(5, board.getSchoolByOwnerId(0).getTowersNumber());
        assertEquals(1, board.getIslands().get(0).getTowers().size());
        assertEquals(1, board.getIslands().get(1).getTowers().size());
        assertEquals(1, board.getIslands().get(2).getTowers().size());
        board.checkNearIsland(1, false);
        assertEquals(10, board.getIslands().size());
        assertEquals(3, board.getIslands().get(0).getTowers().size());
        assertEquals(3, board.getIslands().get(0).getNoEntryTile());
        for (CharacterColor c : CharacterColor.values()) {
            num_students += board.getIslands().get(0).getStudents().get(c).size();
        }
        assertEquals(2, num_students);

        num_students = 0;
        board.moveTower(0, 1, "island");
        board.checkNearIsland(0, false);
        assertEquals(9, board.getIslands().size());
        assertEquals(4, board.getIslands().get(0).getTowers().size());
        for (CharacterColor c : CharacterColor.values()) {
            num_students += board.getIslands().get(0).getStudents().get(c).size();
        }
        assertEquals(3, num_students);

    }


    @AfterEach
    void tearDown() {
    }
}