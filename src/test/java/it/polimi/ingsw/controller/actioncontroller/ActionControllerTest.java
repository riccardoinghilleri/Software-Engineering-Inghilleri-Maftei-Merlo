package it.polimi.ingsw.controller.actioncontroller;

import it.polimi.ingsw.controller.Controller;
import it.polimi.ingsw.server.GameHandler;
import it.polimi.ingsw.enums.Action;
import it.polimi.ingsw.enums.CharacterColor;
import it.polimi.ingsw.enums.PlayerColor;
import it.polimi.ingsw.server.ConnectionMessage.ActionMessage;
import it.polimi.ingsw.server.VirtualView;
import it.polimi.ingsw.server.model.BoardExpert;
import it.polimi.ingsw.server.model.GameModel;
import it.polimi.ingsw.server.model.Student;
import it.polimi.ingsw.server.model.Tower;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ActionControllerTest {
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
        gameModel.getPlayers().get(0).setColor(PlayerColor.WHITE.toString());
        gameModel.getPlayers().get(1).setColor(PlayerColor.BLACK.toString());
        gameModel.createBoard();
    }

    /*
        @Test
        void testMoveStudent() {
            ActionMessage actionMessage = new ActionMessage();
            set(controller, actionMessage);
            actionMessage.setCharacterCardName(null);
            controller.nextAction(actionMessage);
            assertEquals(Action.DEFAULT_MOVEMENTS, controller.getPhase());
            actionMessage = new ActionMessage();
            actionMessage.setAction(Action.DEFAULT_MOVEMENTS);
            assertEquals(7, gameModel.getBoard().getSchoolByOwnerId(1).getEntrance().size());
            gameModel.getBoard().getSchoolByOwnerId(1).getEntrance().remove(0);
            gameModel.getBoard().getSchoolByOwnerId(1).getEntrance().remove(0);
            gameModel.getBoard().getSchoolByOwnerId(1).getEntrance().remove(0);
            gameModel.getBoard().getSchoolByOwnerId(1).addEntranceStudent(new Student(CharacterColor.RED));
            gameModel.getBoard().getSchoolByOwnerId(1).addEntranceStudent(new Student(CharacterColor.RED));
            gameModel.getBoard().getSchoolByOwnerId(1).addEntranceStudent(new Student(CharacterColor.RED));
            actionMessage.setParameter("RED");
            controller.nextAction(actionMessage);
            actionMessage.getParameters().clear();
            actionMessage.setParameter("RED");
            controller.nextAction(actionMessage);
            actionMessage.getParameters().clear();
            actionMessage.setParameter("RED");
            controller.nextAction(actionMessage);
            actionMessage.getParameters().clear();
        }
    */
    @Test
    void testMoveMotherNature() {
        BoardExpert board = ((BoardExpert) gameModel.getBoard());
        ActionMessage actionMessage = new ActionMessage();
        set(controller, actionMessage);
        assertEquals(gameModel,controller.getActionController().getGameModel());
        board.createThreeCharacterCards(3);
        board.addCointoPlayer(1);
        assertEquals(2, board.getPlayerCoins(1));
        actionMessage.setCharacterCardName("HERBOLARIA");
        controller.nextAction(actionMessage);
        assertEquals(Action.USE_CHARACTER_CARD, controller.getPhase());
        actionMessage.setAction(Action.USE_CHARACTER_CARD);
        actionMessage.setData(1);
        controller.nextAction(actionMessage);
        assertTrue(board.getIslands().get(1).hasNoEntryTile());
        assertEquals(Action.DEFAULT_MOVEMENTS, controller.getPhase());

        actionMessage = new ActionMessage();
        actionMessage.setAction(Action.MOVE_MOTHER_NATURE);
        actionMessage.setData(1);
        controller.getActionController().moveMotherNature(actionMessage);
        assertEquals(1,board.getMotherNaturePosition());
        assertFalse(board.getIslands().get(1).hasNoEntryTile());
        controller.getActionController().moveMotherNature(actionMessage);
        assertEquals(2,board.getMotherNaturePosition());
        gameModel.getBoard().getIslands().get(3).addTower(new Tower(0, gameModel.getPlayerById(0).getColor()));
        actionMessage = new ActionMessage();
        actionMessage.setAction(Action.DEFAULT_MOVEMENTS);
        board.getIslands().get(3).addStudent(new Student(CharacterColor.RED));
        board.getIslands().get(3).addStudent(new Student(CharacterColor.RED));
        gameModel.getBoard().getSchoolByOwnerId(1).getEntrance().remove(0);
        gameModel.getBoard().getSchoolByOwnerId(1).getEntrance().remove(0);
        gameModel.getBoard().getSchoolByOwnerId(1).getEntrance().remove(0);
        gameModel.getBoard().getSchoolByOwnerId(1).addEntranceStudent(new Student(CharacterColor.RED));
        gameModel.getBoard().getSchoolByOwnerId(1).addEntranceStudent(new Student(CharacterColor.RED));
        gameModel.getBoard().getSchoolByOwnerId(1).addEntranceStudent(new Student(CharacterColor.RED));
        actionMessage.setParameter("RED");
        controller.nextAction(actionMessage);
        assertEquals(gameModel.getCurrentPlayer().getClientID(),gameModel.getBoard().getProfessorByColor(CharacterColor.RED).getOwner());
        controller.nextAction(actionMessage);
        controller.nextAction(actionMessage);
        assertEquals(Action.MOVE_MOTHER_NATURE, controller.getPhase());
        actionMessage = new ActionMessage();
        actionMessage.setAction(Action.MOVE_MOTHER_NATURE);
        actionMessage.setData(1);
        controller.nextAction(actionMessage);

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