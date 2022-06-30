package it.polimi.ingsw.controller;

import it.polimi.ingsw.enums.Action;
import it.polimi.ingsw.enums.CharacterColor;
import it.polimi.ingsw.enums.PlayerColor;
import it.polimi.ingsw.server.ConnectionMessage.ActionMessage;
import it.polimi.ingsw.server.VirtualView;
import it.polimi.ingsw.server.model.BoardExpert;
import it.polimi.ingsw.server.model.GameModel;
import it.polimi.ingsw.server.model.Student;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ControllerTest {
    GameHandler gameHandler;
    GameHandler gameHandler_normal;

    Controller controller;
    Controller controller_normal;
    GameModel gameModel;
    GameModel gameModel_normal;

    @BeforeEach
    void setUp() {
        List<VirtualView> clients = new ArrayList<>();
        clients.add(new VirtualView());
        clients.add(new VirtualView());
        assertEquals(2, clients.size());
        gameHandler = new GameHandler(true, clients, null);
        gameHandler_normal = new GameHandler(false, clients, null);
        gameModel = gameHandler.getGameModel();
        gameModel_normal=gameHandler_normal.getGameModel();
        controller = new Controller(gameModel, gameHandler);
        controller_normal = new Controller(gameModel_normal, gameHandler_normal);
        gameModel.createPlayer("p1", 0);
        gameModel.createPlayer("p2", 1);
        gameModel.getPlayers().get(0).setColor(PlayerColor.WHITE.toString());
        gameModel.getPlayers().get(1).setColor(PlayerColor.BLACK.toString());
        gameModel.createBoard();
        gameModel_normal.createPlayer("p1", 0);
        gameModel_normal.createPlayer("p2", 1);
        gameModel_normal.getPlayers().get(0).setColor(PlayerColor.WHITE.toString());
        gameModel_normal.getPlayers().get(1).setColor(PlayerColor.BLACK.toString());
        gameModel_normal.createBoard();
    }

    @Test
    void Controller() {
        assertEquals(Action.SETUP_CLOUD, controller.getPhase());
        assertNull(controller.getCharacterCardName());
    }

    @Test
    void testSetClouds() {
        assertTrue(gameModel.getBoard().getClouds()[0].getStudents().isEmpty());
        assertTrue(gameModel.getBoard().getClouds()[1].getStudents().isEmpty());
        controller.setClouds();
        assertEquals(3, gameModel.getBoard().getClouds()[0].getStudents().size());
        assertEquals(3, gameModel.getBoard().getClouds()[1].getStudents().size());
        assertEquals(Action.CHOOSE_ASSISTANT_CARD, controller.getPhase());
        assertEquals(0, gameModel.getCurrentPlayer().getClientID());
    }

    @Test
    void testSetAssistantCards() {
        controller.setClouds();
        ActionMessage actionMessage = new ActionMessage();
        actionMessage.setAction(Action.CHOOSE_CHARACTER_CARD);
        assertEquals("Invalid action!",controller.nextAction(actionMessage));
        actionMessage.setAction(Action.USE_CHARACTER_CARD);
        assertEquals("Invalid action!",controller.nextAction(actionMessage));
        actionMessage.setAction(Action.DEFAULT_MOVEMENTS);
        assertEquals("Invalid action!",controller.nextAction(actionMessage));
        actionMessage.setAction(Action.MOVE_MOTHER_NATURE);
        assertEquals("Invalid action!",controller.nextAction(actionMessage));
        actionMessage.setAction(Action.CHOOSE_CLOUD);
        assertEquals("Invalid action!",controller.nextAction(actionMessage));
        actionMessage.setData(5);
        actionMessage.setAction(Action.CHOOSE_ASSISTANT_CARD);
        assertEquals(0, controller.getPlayerTurnNumber());
        assertNull(controller.setAssistantCard(actionMessage));
        assertEquals(1, controller.getPlayerTurnNumber());
        assertEquals(1, gameModel.getCurrentPlayer().getClientID());
        assertEquals(">You can not choose this assistant card. " +
                "Please choose another one.", controller.setAssistantCard(actionMessage));
        actionMessage.setData(3);
        assertNull(controller.setAssistantCard(actionMessage));
        assertEquals(0, controller.getPlayerTurnNumber());
        assertEquals(1, gameModel.getCurrentPlayer().getClientID());
        assertEquals(5, gameModel.getPlayerById(0).getChosenAssistantCard().getPriority());
        assertEquals(3, gameModel.getPlayerById(1).getChosenAssistantCard().getPriority());

        controller_normal.setClouds();
        actionMessage = new ActionMessage();
        actionMessage.setAction(Action.CHOOSE_ASSISTANT_CARD);
        actionMessage.setData(5);
        assertEquals(0, controller_normal.getPlayerTurnNumber());
        assertNull(controller_normal.setAssistantCard(actionMessage));
        assertEquals(1, controller_normal.getPlayerTurnNumber());
        assertEquals(1, gameModel_normal.getCurrentPlayer().getClientID());
        actionMessage.setData(3);
        assertNull(controller_normal.setAssistantCard(actionMessage));
        assertEquals(5, gameModel_normal.getPlayerById(0).getChosenAssistantCard().getPriority());
        assertEquals(3, gameModel_normal.getPlayerById(1).getChosenAssistantCard().getPriority());
        assertEquals(Action.DEFAULT_MOVEMENTS,controller_normal.getPhase());


    }

    @Test
    void testNextActionChooseCharacterCard() {
        ActionMessage actionMessage = new ActionMessage();
        //NESSUNA SCELTA DELLA CARTA SPECIALE
        set(controller, actionMessage);
        actionMessage.setCharacterCardName(null);
        controller.nextAction(actionMessage);
        assertEquals(Action.DEFAULT_MOVEMENTS, controller.getPhase());

        //CARTE DISPONIBILI: PRIEST - QUEEN - THIEF
        setUp();
        set(controller, actionMessage);
        ((BoardExpert) gameModel.getBoard()).createThreeCharacterCards(1);
        String[] names_0 = {"QUEEN", "THIEF", "PRIEST"};
        actionMessage.setCharacterCardName("PERFORMER");
        assertEquals("This card is not on the Board!", controller.nextAction(actionMessage));
        for (String name : names_0) {
            actionMessage.setCharacterCardName(name);
            if (!name.equalsIgnoreCase("PRIEST")) {
                assertEquals("You have not got enough coins", controller.nextAction(actionMessage));
                assertEquals(Action.CHOOSE_CHARACTER_CARD, controller.getPhase());
            } else {
                assertNull(controller.nextAction(actionMessage));
                assertEquals("PRIEST", controller.getCharacterCardName());
                assertEquals(Action.USE_CHARACTER_CARD, controller.getPhase());
            }
        }

        //CARTE DISPONIBILI: KNIGHT - DIPLOMAT - CLOWN
        setUp();
        set(controller, actionMessage);
        ((BoardExpert) gameModel.getBoard()).createThreeCharacterCards(2);
        String[] names_1 = {"KNIGHT", "DIPLOMAT", "CLOWN"};
        for (String name : names_1) {
            actionMessage.setCharacterCardName(name);
            if (!name.equalsIgnoreCase("CLOWN")) {
                assertEquals("You have not got enough coins", controller.nextAction(actionMessage));
                assertEquals(Action.CHOOSE_CHARACTER_CARD, controller.getPhase());
            } else {
                assertNull(controller.nextAction(actionMessage));
                assertEquals("CLOWN", controller.getCharacterCardName());
                assertEquals(Action.USE_CHARACTER_CARD, controller.getPhase());
            }
        }

        //CARTE DISPONIBILI: POSTMAN - HERBOLARIA - CENTAUR
        setUp();
        set(controller, actionMessage);
        ((BoardExpert) gameModel.getBoard()).createThreeCharacterCards(3);
        String[] names_2 = {"HERBOLARIA", "CENTAUR", "POSTMAN"};
        for (String name : names_2) {
            actionMessage.setCharacterCardName(name);
            if (!name.equalsIgnoreCase("POSTMAN")) {
                assertEquals("You have not got enough coins", controller.nextAction(actionMessage));
                assertEquals(Action.CHOOSE_CHARACTER_CARD, controller.getPhase());
            } else {
                assertNull(controller.nextAction(actionMessage));
                assertEquals("POSTMAN", controller.getCharacterCardName());
                assertEquals(Action.DEFAULT_MOVEMENTS, controller.getPhase());
            }
        }

        //CARTE DISPONIBILI: LUMBERJACK - DINER - PERFORMER
        setUp();
        set(controller, actionMessage);
        ((BoardExpert) gameModel.getBoard()).createThreeCharacterCards(4);
        String[] names_3 = {"LUMBERJACK", "DINER", "PERFORMER"};
        for (String name : names_3) {
            actionMessage.setCharacterCardName(name);
            if (!name.equalsIgnoreCase("PERFORMER")) {
                assertEquals("You have not got enough coins", controller.nextAction(actionMessage));
            } else {
                assertEquals("You can not choose this character card. You don't have enough students in your dining room!", controller.nextAction(actionMessage));
            }
            assertEquals(Action.CHOOSE_CHARACTER_CARD, controller.getPhase());
        }

    }

    @Test
    void testNextActionNormal() {
        BoardExpert board = (BoardExpert) gameModel.getBoard();
        controller.setClouds();
        ActionMessage actionMessage = new ActionMessage();
        actionMessage.setData(5);
        actionMessage.setAction(Action.CHOOSE_ASSISTANT_CARD);
        controller.setAssistantCard(actionMessage);
        actionMessage.setData(3);
        actionMessage.setAction(Action.CHOOSE_ASSISTANT_CARD);
        controller.setAssistantCard(actionMessage);
        actionMessage = new ActionMessage();
        actionMessage.setAction(Action.CHOOSE_CHARACTER_CARD);
        actionMessage.setCharacterCardName(null);
        controller.nextAction(actionMessage);
        assertEquals(Action.DEFAULT_MOVEMENTS, controller.getPhase());
        actionMessage = new ActionMessage();
        actionMessage.setAction(Action.DEFAULT_MOVEMENTS);
        List<Student> students = board.getSchoolByOwnerId(1).getEntrance();
        List<CharacterColor> colors = new ArrayList<>(Arrays.asList(CharacterColor.values()));
        for (Student s : students) {
            colors.remove(s.getColor());
        }
        if (colors.size() > 1) {
            actionMessage.setParameter(colors.get(0).toString().toUpperCase());
            assertEquals("There is not a student of this color in your entrance.", controller.nextAction(actionMessage));
            assertEquals(0, board.getSchoolByOwnerId(1).getNumDiningRoomStudents());
        }
        CharacterColor entrance_color;
        for (int i = 0; i < 2; i++) {
            entrance_color = board.getSchoolByOwnerId(1).getEntrance().get(0).getColor();
            actionMessage.getParameters().clear();
            actionMessage.setParameter(entrance_color.toString().toUpperCase());
            //aggiungo 1 studente di un colore nella diningroom per muovere le torri nelle isole 2/3
            board.getIslands().get(2).addStudent(new Student(entrance_color));
            board.getIslands().get(3).addStudent(new Student(entrance_color));

            assertNull(controller.nextAction(actionMessage));
            assertEquals(7 - i - 1, board.getSchoolByOwnerId(1).getEntrance().size());
            assertEquals(i + 1, board.getSchoolByOwnerId(1).getNumDiningRoomStudents());
            assertEquals(1, board.getProfessorByColor(entrance_color).getOwner());
        }
        actionMessage.getParameters().clear();
        String island_color = board.getSchoolByOwnerId(1).getEntrance().get(0).getColor().toString().toUpperCase();
        actionMessage.setParameter(island_color);
        actionMessage.setData(0);
        assertNull(controller.nextAction(actionMessage));
        assertEquals(1, board.getIslands().get(0).getStudents().get(CharacterColor.valueOf(island_color)).size());
        assertEquals(4, board.getSchoolByOwnerId(1).getEntrance().size());

        assertEquals(Action.CHOOSE_CHARACTER_CARD, controller.getPhase());
        actionMessage = new ActionMessage();
        actionMessage.setAction(Action.CHOOSE_CHARACTER_CARD);
        actionMessage.setCharacterCardName(null);
        controller.nextAction(actionMessage);

        assertEquals(Action.MOVE_MOTHER_NATURE, controller.getPhase());
        actionMessage.setAction(Action.MOVE_MOTHER_NATURE);
        actionMessage.setData(6);
        assertEquals("You can not move mother nature so far", controller.nextAction(actionMessage));
        assertEquals(0, board.getMotherNaturePosition());
        actionMessage.setData(2);
        assertEquals(null, controller.nextAction(actionMessage));
        assertEquals(2, board.getMotherNaturePosition());
        assertFalse(board.getIslands().get(2).getTowers().isEmpty());
        assertEquals(gameModel.getPlayerById(1).getColor(), board.getIslands().get(2).getTowers().get(0).getColor());

        assertEquals(Action.CHOOSE_CHARACTER_CARD, controller.getPhase());
        actionMessage = new ActionMessage();
        actionMessage.setAction(Action.CHOOSE_CHARACTER_CARD);
        actionMessage.setCharacterCardName(null);
        controller.nextAction(actionMessage);

        assertEquals(Action.CHOOSE_CLOUD, controller.getPhase());
        actionMessage = new ActionMessage();
        actionMessage.setAction(Action.CHOOSE_CLOUD);
        actionMessage.setData(-1);
        assertEquals("Invalid cloud Id.", controller.nextAction(actionMessage));
        assertEquals(Action.CHOOSE_CLOUD, controller.getPhase());
        actionMessage.setData(12);
        assertEquals("Invalid cloud Id.", controller.nextAction(actionMessage));
        assertEquals(Action.CHOOSE_CLOUD, controller.getPhase());
        actionMessage.setData(1);
        assertNull(controller.nextAction(actionMessage));
        assertEquals(Action.CHOOSE_CHARACTER_CARD, controller.getPhase());
        assertTrue(board.getClouds()[1].getStudents().isEmpty());
        assertEquals(0, gameModel.getCurrentPlayer().getClientID());

        //TURNO SECONDO GIOCATORE
        actionMessage = new ActionMessage();
        actionMessage.setAction(Action.CHOOSE_CHARACTER_CARD);
        actionMessage.setCharacterCardName(null);
        controller.nextAction(actionMessage);
        assertEquals(Action.DEFAULT_MOVEMENTS, controller.getPhase());
        actionMessage = new ActionMessage();
        actionMessage.setAction(Action.DEFAULT_MOVEMENTS);
        for (int i = 0; i < 3; i++) {
            entrance_color = board.getSchoolByOwnerId(0).getEntrance().get(0).getColor();
            actionMessage.getParameters().clear();
            actionMessage.setParameter(entrance_color.toString().toUpperCase());
            actionMessage.setData(6);
            assertNull(controller.nextAction(actionMessage));
            assertEquals(7 - i - 1, board.getSchoolByOwnerId(0).getEntrance().size());
            assertEquals(0, board.getSchoolByOwnerId(0).getNumDiningRoomStudents());
        }

        assertEquals(Action.CHOOSE_CHARACTER_CARD, controller.getPhase());
        actionMessage = new ActionMessage();
        actionMessage.setAction(Action.CHOOSE_CHARACTER_CARD);
        actionMessage.setCharacterCardName(null);
        controller.nextAction(actionMessage);

        assertEquals(Action.MOVE_MOTHER_NATURE, controller.getPhase());
        actionMessage.setAction(Action.MOVE_MOTHER_NATURE);
        actionMessage.setData(1);
        assertEquals(null, controller.nextAction(actionMessage));
        assertEquals(2, board.getMotherNaturePosition());
        assertEquals(2, board.getIslands().get(2).getTowers().size());
        assertEquals(gameModel.getPlayerById(1).getColor(), board.getIslands().get(2).getTowers().get(0).getColor());
        assertEquals(11, board.getIslands().size());

        assertEquals(Action.CHOOSE_CHARACTER_CARD, controller.getPhase());
        actionMessage = new ActionMessage();
        actionMessage.setAction(Action.CHOOSE_CHARACTER_CARD);
        actionMessage.setCharacterCardName(null);
        controller.nextAction(actionMessage);

        assertEquals(Action.CHOOSE_CLOUD, controller.getPhase());
        actionMessage = new ActionMessage();
        actionMessage.setAction(Action.CHOOSE_CLOUD);
        actionMessage.setData(1);
        assertEquals("You have chosen an empty cloud.", controller.nextAction(actionMessage));
        assertEquals(Action.CHOOSE_CLOUD, controller.getPhase());
        actionMessage.setData(0);
        assertNull(controller.nextAction(actionMessage));
        assertTrue(board.getClouds()[0].getStudents().isEmpty());
        assertEquals(Action.SETUP_CLOUD, controller.getPhase());
        controller.setClouds();
        assertEquals(1, gameModel.getCurrentPlayer().getClientID());

    }

    private void set(Controller controller, ActionMessage actionMessage) {
        controller.setClouds();
        actionMessage.setData(5);
        actionMessage.setAction(Action.CHOOSE_ASSISTANT_CARD);
        controller.setAssistantCard(actionMessage);
        actionMessage.setData(3);
        actionMessage.setAction(Action.CHOOSE_ASSISTANT_CARD);
        controller.setAssistantCard(actionMessage);
        actionMessage.setData(-1);
        actionMessage.setAction(Action.CHOOSE_CHARACTER_CARD);
    }
}