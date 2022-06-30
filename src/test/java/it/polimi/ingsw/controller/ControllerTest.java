package it.polimi.ingsw.controller;

import it.polimi.ingsw.enums.Action;
import it.polimi.ingsw.enums.CharacterColor;
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
    Controller controller;
    GameModel gameModel;

    @BeforeEach
    void setUp() {
        List<VirtualView> clients = new ArrayList<>();
        clients.add(new VirtualView());
        clients.add(new VirtualView());
        assertEquals(2, clients.size());
        gameHandler = new GameHandler(true, clients, null);
        gameModel = gameHandler.getGameModel();
        controller = new Controller(gameModel, gameHandler);
        gameModel.createPlayer("p1", 0);
        gameModel.createPlayer("p2", 1);
        gameModel.createBoard();
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
    }

    @Test
    void testNextActionChooseCharacterCard() {
        ActionMessage actionMessage = new ActionMessage();
        set(controller,actionMessage);
        actionMessage.setCharacterCardName(null);
        controller.nextAction(actionMessage);
        assertEquals(Action.DEFAULT_MOVEMENTS, controller.getPhase());
        String[] names_0 = {"DINER", "DIPLOMAT", "HERBOLARIA", "CENTAUR", "KNIGHT", "THIEF", "LUMBERJACK", "QUEEN"};
        for (String name : names_0) {
            setUp();
            set(controller, actionMessage);
            actionMessage.setCharacterCardName(name);
            if (((BoardExpert) gameModel.getBoard()).getCharacterCardbyName(actionMessage.getCharacterCardName()) == null) {
                assertEquals("This card is not on the Board!", controller.nextAction(actionMessage));
            } else {
                assertEquals("You have not got enough coins", controller.nextAction(actionMessage));
            }
        }

        String[] names_1 = {"PRIEST", "POSTMAN", "CLOWN", "PERFORMER"};
        for (String name : names_1) {
            setUp();
            set(controller, actionMessage);
            actionMessage.setCharacterCardName(name);
            if (((BoardExpert) gameModel.getBoard()).getCharacterCardbyName(actionMessage.getCharacterCardName()) == null) {
                assertEquals("This card is not on the Board!", controller.nextAction(actionMessage));
            } else {
                if (name.equals("PERFORMER"))
                    assertEquals("You can not choose this character card. You don't have enough students in your dining room!", controller.nextAction(actionMessage));
                else {
                    assertNull(controller.nextAction(actionMessage));
                    assertEquals(name, controller.getCharacterCardName());
                    if (!name.equals("POSTMAN"))
                        assertEquals(Action.USE_CHARACTER_CARD, controller.getPhase());
                    else {
                        assertNotEquals(Action.USE_CHARACTER_CARD, controller.getPhase());
                        assertEquals(Action.DEFAULT_MOVEMENTS, controller.getPhase());
                    }
                }
            }
            ((BoardExpert) gameModel.getBoard()).addCointoPlayer(1);
        }
        gameModel.getBoard().getSchoolByOwnerId(1).addDiningRoomStudent(new Student(CharacterColor.RED));
        actionMessage.setCharacterCardName("PERFORMER");
        if (((BoardExpert) gameModel.getBoard()).getCharacterCardbyName(actionMessage.getCharacterCardName()) == null) {
            assertEquals("This card is not on the Board!", controller.nextAction(actionMessage));
        } else {
            assertNull(controller.nextAction(actionMessage));
            assertEquals("PERFORMER", controller.getCharacterCardName().toUpperCase());
        }
    }

    @Test
    void testNext() {
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

        for (int i = 0; i < 2; i++) {
            actionMessage.getParameters().clear();
            actionMessage.setParameter(board.getSchoolByOwnerId(1).getEntrance().get(0).getColor().toString().toUpperCase());
            assertNull(controller.nextAction(actionMessage));
            assertEquals(7 - i - 1, board.getSchoolByOwnerId(1).getEntrance().size());
            assertEquals(i + 1, board.getSchoolByOwnerId(1).getNumDiningRoomStudents());
        }
        actionMessage.getParameters().clear();
        String color = board.getSchoolByOwnerId(1).getEntrance().get(0).getColor().toString().toUpperCase();
        actionMessage.setParameter(color);
        actionMessage.setData(0);
        assertNull(controller.nextAction(actionMessage));
        assertEquals(1, board.getIslands().get(0).getStudents().get(CharacterColor.valueOf(color)).size());
        assertEquals(4, board.getSchoolByOwnerId(1).getEntrance().size());

    }

    private void set(Controller controller, ActionMessage actionMessage){
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