package it.polimi.ingsw.controller;

import it.polimi.ingsw.controller.actioncontroller.*;
import it.polimi.ingsw.enums.Action;
import it.polimi.ingsw.enums.CharacterColor;
import it.polimi.ingsw.enums.PlayerColor;
import it.polimi.ingsw.server.ConnectionMessage.ActionMessage;
import it.polimi.ingsw.server.VirtualView;
import it.polimi.ingsw.server.model.*;
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
        gameModel_normal = gameHandler_normal.getGameModel();
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
        assertEquals("Invalid action!", controller.nextAction(actionMessage));
        actionMessage.setAction(Action.USE_CHARACTER_CARD);
        assertEquals("Invalid action!", controller.nextAction(actionMessage));
        actionMessage.setAction(Action.DEFAULT_MOVEMENTS);
        assertEquals("Invalid action!", controller.nextAction(actionMessage));
        actionMessage.setAction(Action.MOVE_MOTHER_NATURE);
        assertEquals("Invalid action!", controller.nextAction(actionMessage));
        actionMessage.setAction(Action.CHOOSE_CLOUD);
        assertEquals("Invalid action!", controller.nextAction(actionMessage));
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
        assertEquals(Action.DEFAULT_MOVEMENTS, controller_normal.getPhase());


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
            //aggiungo uno studente di un colore nella Dining Room per muovere le torri nelle isole 2/3
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
        assertNull(controller.nextAction(actionMessage));
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
        assertNull(controller.nextAction(actionMessage));
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

    @Test

    void testLumberjack(){
        ActionMessage actionMessage = new ActionMessage();
        //USO IL LUMBERJACK IN UNA SITAZIONE DI MINORANZA (escluso il colore)
        //[CLIENT0 - YELLOW-RED-TOWER] [CLIENT1 GREEN]
        BoardExpert board=chooseLumberjack(actionMessage);
        board.getSchoolByOwnerId(0).addDiningRoomStudent(new Student(CharacterColor.RED));
        board.getIslands().get(6).addStudent(new Student(CharacterColor.RED));
        board.getProfessorByColor(CharacterColor.RED).setOwner(0);
        board.getSchoolByOwnerId(0).addDiningRoomStudent(new Student(CharacterColor.YELLOW));
        board.getIslands().get(6).addStudent(new Student(CharacterColor.YELLOW));
        board.getProfessorByColor(CharacterColor.YELLOW).setOwner(0);
        board.getSchoolByOwnerId(1).addDiningRoomStudent(new Student(CharacterColor.GREEN));
        board.getIslands().get(6).addStudent(new Student(CharacterColor.GREEN));
        board.getProfessorByColor(CharacterColor.GREEN).setOwner(1);
        board.moveTower(0,6,"island");
        actionMessage.setAction(Action.MOVE_MOTHER_NATURE);
        actionMessage.setData(6);
        controller.getActionController().moveMotherNature(actionMessage);
        assertEquals(0,board.getIslands().get(6).getTowers().get(0).getOwner());

        //USO IL LUMBERJACK IN UNA SITAZIONE DI PAREGGIO  (escluso il colore)
        //[CLIENT0 - RED-TOWER] [CLIENT1 GREEN]
        board=chooseLumberjack(actionMessage);
        board.getSchoolByOwnerId(0).addDiningRoomStudent(new Student(CharacterColor.RED));
        board.getIslands().get(6).addStudent(new Student(CharacterColor.RED));
        board.getProfessorByColor(CharacterColor.RED).setOwner(0);
        board.getSchoolByOwnerId(1).addDiningRoomStudent(new Student(CharacterColor.GREEN));
        board.getIslands().get(6).addStudent(new Student(CharacterColor.GREEN));
        board.getProfessorByColor(CharacterColor.GREEN).setOwner(1);
        board.moveTower(0,6,"island");
        actionMessage.setAction(Action.MOVE_MOTHER_NATURE);
        actionMessage.setData(6);
        controller.getActionController().moveMotherNature(actionMessage);
        assertEquals(0,board.getIslands().get(6).getTowers().get(0).getOwner());

        //USO IL LUMBERJACK IN UNA SITAZIONE DI MAGGIORANZA  (escluso il colore)
        //[CLIENT0 - RED] [CLIENT1 GREEN]
        board=chooseLumberjack(actionMessage);
        board.getSchoolByOwnerId(0).addDiningRoomStudent(new Student(CharacterColor.RED));
        board.getIslands().get(6).addStudent(new Student(CharacterColor.RED));
        board.getProfessorByColor(CharacterColor.RED).setOwner(0);
        board.getSchoolByOwnerId(1).addDiningRoomStudent(new Student(CharacterColor.GREEN));
        board.getIslands().get(6).addStudent(new Student(CharacterColor.GREEN));
        board.getProfessorByColor(CharacterColor.GREEN).setOwner(1);
        actionMessage.setAction(Action.MOVE_MOTHER_NATURE);
        actionMessage.setData(6);
        controller.getActionController().moveMotherNature(actionMessage);
        assertEquals(1,board.getIslands().get(6).getTowers().get(0).getOwner());



    }
    private BoardExpert chooseLumberjack(ActionMessage actionMessage) {
        //USO LUMBERJACK - AGGIUNGO 2 MONETE AL PALYER CORRENTE
        setUp();
        set(controller, actionMessage);
        BoardExpert board = (BoardExpert) gameModel.getBoard();
        board.createThreeCharacterCards(4);
        board.addCointoPlayer(1);
        board.addCointoPlayer(1);
        actionMessage.setAction(Action.CHOOSE_CHARACTER_CARD);
        actionMessage.setCharacterCardName("LUMBERJACK");
        assertNull(controller.nextAction(actionMessage));
        assertTrue(controller.getActionController() instanceof Lumberjack);
        assertEquals(Action.USE_CHARACTER_CARD, controller.getPhase());
        actionMessage.setAction(Action.USE_CHARACTER_CARD);
        actionMessage.setParameter("RED");
        assertNull(controller.nextAction(actionMessage));
        assertEquals(Action.DEFAULT_MOVEMENTS, controller.getPhase());
        return board;
    }

    @Test
    void testCentaur(){
        ActionMessage actionMessage = new ActionMessage();
        //USO IL CENTAURO QUANDO NON CI SONO TORRI E UNO STUDENTE AVVERSARIO
        BoardExpert board=chooseCentaur(actionMessage);
        board.getSchoolByOwnerId(0).addDiningRoomStudent(new Student(CharacterColor.RED));
        board.getIslands().get(6).addStudent(new Student(CharacterColor.RED));
        board.getProfessorByColor(CharacterColor.RED).setOwner(0);
        actionMessage.setAction(Action.MOVE_MOTHER_NATURE);
        actionMessage.setData(6);
        controller.getActionController().moveMotherNature(actionMessage);
        assertEquals(0,board.getIslands().get(6).getTowers().get(0).getOwner());

        //USO IL CENTAURA CON 1 TORRE AVVERSARIA E UNO STUDENTE PROPRIO
        board=chooseCentaur(actionMessage);
        board.getSchoolByOwnerId(1).addDiningRoomStudent(new Student(CharacterColor.RED));
        board.getIslands().get(6).addStudent(new Student(CharacterColor.RED));
        board.getProfessorByColor(CharacterColor.RED).setOwner(1);
        board.moveTower(0,6,"island");
        actionMessage.setAction(Action.MOVE_MOTHER_NATURE);
        actionMessage.setData(6);
        controller.getActionController().moveMotherNature(actionMessage);
        assertEquals(1,board.getIslands().get(6).getTowers().get(0).getOwner());

        //USO IL CENTAURO CON UNA TORRE AVVERSARIA + 1 STUDENTE AVVERSARIO E UNO STUDENTE PROPRIO
        board=chooseCentaur(actionMessage);
        board.getSchoolByOwnerId(1).addDiningRoomStudent(new Student(CharacterColor.RED));
        board.getIslands().get(6).addStudent(new Student(CharacterColor.RED));
        board.getProfessorByColor(CharacterColor.RED).setOwner(1);
        board.getSchoolByOwnerId(0).addDiningRoomStudent(new Student(CharacterColor.GREEN));
        board.getIslands().get(6).addStudent(new Student(CharacterColor.GREEN));
        board.getProfessorByColor(CharacterColor.GREEN).setOwner(0);
        board.moveTower(0,6,"island");
        actionMessage.setAction(Action.MOVE_MOTHER_NATURE);
        actionMessage.setData(6);
        controller.getActionController().moveMotherNature(actionMessage);
        assertEquals(0,board.getIslands().get(6).getTowers().get(0).getOwner());

    }
    private BoardExpert chooseCentaur(ActionMessage actionMessage){
        //USO CENTAUR - AGGIUNGO 2 MONETE AL PLAYER CORRENTE
        setUp();
        set(controller, actionMessage);
        BoardExpert board = (BoardExpert) gameModel.getBoard();
        board.createThreeCharacterCards(3);
        board.addCointoPlayer(1);
        board.addCointoPlayer(1);
        actionMessage.setAction(Action.CHOOSE_CHARACTER_CARD);
        actionMessage.setCharacterCardName("CENTAUR");
        assertNull(controller.nextAction(actionMessage));
        assertTrue(controller.getActionController() instanceof Centaur);
        assertEquals(Action.DEFAULT_MOVEMENTS, controller.getPhase());
        return board;
    }

    @Test
    void testDiner(){
        ActionMessage actionMessage = new ActionMessage();
        //USO IL DINER IN SITUAZIONE DI PAREGGIO
        BoardExpert board=chooseDiner(actionMessage);
        board.getSchoolByOwnerId(0).addDiningRoomStudent(new Student(CharacterColor.RED));
        board.getProfessorByColor(CharacterColor.RED).setOwner(0);
        assertNull(controller.nextAction(actionMessage));
        assertEquals(0,board.getCoins()[1]);
        assertTrue(controller.getActionController() instanceof Diner);
        assertEquals(Action.DEFAULT_MOVEMENTS, controller.getPhase());
        board.getSchoolByOwnerId(1).getEntrance().add(new Student(CharacterColor.RED));
        controller.getActionController().moveStudent("RED");
        assertEquals(1,board.getProfessorByColor(CharacterColor.RED).getOwner());

        //USO IL DINER IN UNA SITUAZIONE DI MINORANZA

        board=chooseDiner(actionMessage);
        board.getSchoolByOwnerId(0).addDiningRoomStudent(new Student(CharacterColor.RED));
        board.getSchoolByOwnerId(0).addDiningRoomStudent(new Student(CharacterColor.RED));
        board.getProfessorByColor(CharacterColor.RED).setOwner(0);
        assertNull(controller.nextAction(actionMessage));
        assertEquals(0,board.getCoins()[1]);
        assertTrue(controller.getActionController() instanceof Diner);
        assertEquals(Action.DEFAULT_MOVEMENTS, controller.getPhase());
        board.getSchoolByOwnerId(1).getEntrance().add(new Student(CharacterColor.RED));
        controller.getActionController().moveStudent("RED");
        assertEquals(0,board.getProfessorByColor(CharacterColor.RED).getOwner());


    }

    private BoardExpert chooseDiner(ActionMessage actionMessage){
        //USO DINER - AGGIUNGO 1 MONETA AL PALYER CORRENTE
        setUp();
        set(controller, actionMessage);
        BoardExpert board = (BoardExpert) gameModel.getBoard();
        board.createThreeCharacterCards(4);
        board.addCointoPlayer(1);
        actionMessage.setAction(Action.CHOOSE_CHARACTER_CARD);
        actionMessage.setCharacterCardName("DINER");
        return board;
    }

    @Test
    void testPriest(){
        ActionMessage actionMessage = new ActionMessage();
        setUp();
        set(controller, actionMessage);
        BoardExpert board = (BoardExpert) gameModel.getBoard();
        board.createThreeCharacterCards(1);
        actionMessage.setAction(Action.CHOOSE_CHARACTER_CARD);
        actionMessage.setCharacterCardName("PRIEST");
        assertNull(controller.nextAction(actionMessage));
        actionMessage.setAction(Action.USE_CHARACTER_CARD);
        CharacterColor color= ((CharacterCardwithStudents)board.getCharacterCardbyName("PRIEST")).getStudents().get(0).getColor();
        actionMessage.setParameter(color.toString());
        actionMessage.setData(0);
        assertEquals(4,((CharacterCardwithStudents)board.getCharacterCardbyName("PRIEST")).getStudents().size());
        assertNull(controller.nextAction(actionMessage));
        assertEquals(1,board.getIslands().get(0).getStudents().get(color).size());
        assertEquals(4,((CharacterCardwithStudents)board.getCharacterCardbyName("PRIEST")).getStudents().size());
    }

    @Test
    void testKnight(){
        //USO IL KNIGHT IN UNA SITUAZIONE DI PAREGGIO
        ActionMessage actionMessage = new ActionMessage();
        BoardExpert board =chooseKnight(actionMessage);
        board.moveTower(0,6,"Island");
        board.moveTower(0,6,"Island");
        actionMessage.setAction(Action.MOVE_MOTHER_NATURE);
        actionMessage.setData(6);
        controller.getActionController().moveMotherNature(actionMessage);
        assertEquals(0,board.getIslands().get(6).getTowers().get(0).getOwner());
        assertEquals(2,board.getIslands().get(6).getTowers().size());
        assertEquals(0,board.getCoins()[1]);

        //USO IL KNIGHT CONQUISTANDO L'ISOLA GRAZIE AI DUE PUNTI IN PIU'
        board = chooseKnight(actionMessage);
        board.moveTower(0,6,"Island");
        board.moveTower(0,6,"Island");
        board.getSchoolByOwnerId(1).addDiningRoomStudent(new Student(CharacterColor.RED));
        board.getIslands().get(6).addStudent(new Student(CharacterColor.RED));
        board.getProfessorByColor(CharacterColor.RED).setOwner(1);
        board.moveTower(1,0,"Island");
        actionMessage.setAction(Action.MOVE_MOTHER_NATURE);
        actionMessage.setData(6);
        controller.getActionController().moveMotherNature(actionMessage);
        assertEquals(1,board.getIslands().get(6).getTowers().get(0).getOwner());
        assertEquals(2,board.getIslands().get(6).getTowers().size());
        assertEquals(0,board.getCoins()[1]);
    }

    private BoardExpert chooseKnight(ActionMessage actionMessage){
        //USO KNIGHT - AGGIUNGO 2 MONETE AL PALYER CORRENTE
        setUp();
        set(controller, actionMessage);
        BoardExpert board = (BoardExpert) gameModel.getBoard();
        board.createThreeCharacterCards(2);
        board.addCointoPlayer(1);
        actionMessage.setAction(Action.CHOOSE_CHARACTER_CARD);
        actionMessage.setCharacterCardName("KNIGHT");
        assertNull(controller.nextAction(actionMessage));
        assertTrue(controller.getActionController() instanceof Knight);
        assertEquals(Action.DEFAULT_MOVEMENTS, controller.getPhase());
        return board;
    }
    @Test
    void testPerformer(){
        ActionMessage actionMessage=new ActionMessage();
        //USO PERFORMER
        setUp();
        set(controller, actionMessage);
        actionMessage.getParameters().clear();
        BoardExpert board = (BoardExpert) gameModel.getBoard();
        board.createThreeCharacterCards(4);
        School school = board.getSchoolByOwnerId(1);
        CharacterColor color1 = school.getEntrance().get(0).getColor();
        CharacterColor color2 = school.getEntrance().get(1).getColor();
        school.fromEntrancetoDiningRoom(color1);
        school.fromEntrancetoDiningRoom(color2);
        assertEquals(2, school.getNumDiningRoomStudents());
        actionMessage.setAction(Action.CHOOSE_CHARACTER_CARD);
        actionMessage.setCharacterCardName("PERFORMER");
        assertNull(controller.nextAction(actionMessage));
        assertEquals(Action.USE_CHARACTER_CARD, controller.getPhase());
        actionMessage.setAction(Action.USE_CHARACTER_CARD);
        actionMessage.setData(2);
        assertNull(controller.nextAction(actionMessage));
        actionMessage.setParameter(school.getEntrance().get(3).getColor().toString());
        actionMessage.setParameter(color1.toString());
        assertNull(controller.nextAction(actionMessage));
        actionMessage.getParameters().clear();
        actionMessage.setParameter(school.getEntrance().get(4).getColor().toString());
        actionMessage.setParameter(color2.toString());
        assertNull(controller.nextAction(actionMessage));
        assertEquals(Action.DEFAULT_MOVEMENTS, controller.getPhase());
        assertEquals(0,board.getCoins()[1]);
    }

    @Test
    void testThief(){
        //USO THIEF [CLIENT 0 - 4 STUDENTI] [CLIENT 1 - 2 STUDENTI]
        ActionMessage actionMessage=new ActionMessage();
        Student student = new Student(CharacterColor.PINK);
        setUp();
        set(controller, actionMessage);
        BoardExpert board = (BoardExpert) gameModel.getBoard();
        board.createThreeCharacterCards(1);
        board.addCointoPlayer(1);
        board.addCointoPlayer(1);

        board.getSchoolByOwnerId(0).addDiningRoomStudent(student);
        board.getSchoolByOwnerId(0).addDiningRoomStudent(student);
        board.getSchoolByOwnerId(0).addDiningRoomStudent(student);
        board.getSchoolByOwnerId(0).addDiningRoomStudent(student);
        board.getSchoolByOwnerId(1).addDiningRoomStudent(student);
        board.getSchoolByOwnerId(1).addDiningRoomStudent(student);
        board.getProfessorByColor(CharacterColor.PINK).setOwner(0);
        board.getSchoolByOwnerId(0).addProfessor(board.getProfessorByColor(CharacterColor.PINK));

        actionMessage.setAction(Action.CHOOSE_CHARACTER_CARD);
        actionMessage.setCharacterCardName("THIEF");
        assertNull(controller.nextAction(actionMessage));
        assertEquals(0,board.getCoins()[1]);
        actionMessage.setAction(Action.USE_CHARACTER_CARD);
        actionMessage.setParameter("PINK");
        assertNull(controller.nextAction(actionMessage));
        assertEquals(1,board.getSchoolByOwnerId(0).getDiningRoom().get(CharacterColor.PINK).size());
        assertEquals(0,board.getSchoolByOwnerId(1).getDiningRoom().get(CharacterColor.PINK).size());
        assertEquals(0,board.getProfessorByColor(CharacterColor.PINK).getOwner());

    }
    @Test
    void testQueen() {
        ActionMessage actionMessage=new ActionMessage();
        setUp();
        set(controller, actionMessage);
        BoardExpert board = (BoardExpert) gameModel.getBoard();
        board.createThreeCharacterCards(1);
        board.addCointoPlayer(1);
        actionMessage.setAction(Action.CHOOSE_CHARACTER_CARD);
        actionMessage.setCharacterCardName("QUEEN");
        assertNull(controller.nextAction(actionMessage));
        actionMessage.setAction(Action.USE_CHARACTER_CARD);
        assertEquals(4,((CharacterCardwithStudents)board.getCharacterCardbyName("QUEEN")).getStudents().size());
        CharacterColor color = ((CharacterCardwithStudents)board.getCharacterCardbyName("QUEEN")).getStudents().get(0).getColor();
        actionMessage.setParameter(color.toString());
        assertNull(controller.nextAction(actionMessage));
        assertEquals(1,board.getSchoolByOwnerId(1).getDiningRoom().get(color).size());
        assertEquals(1,board.getSchoolByOwnerId(1).getNumDiningRoomStudents());
        assertEquals(4,((CharacterCardwithStudents)board.getCharacterCardbyName("QUEEN")).getStudents().size());

    }

    @Test
    void testClown(){
        ActionMessage actionMessage=new ActionMessage();
        setUp();
        set(controller, actionMessage);
        actionMessage.getParameters().clear();
        BoardExpert board = (BoardExpert) gameModel.getBoard();
        board.createThreeCharacterCards(2);
        School school = board.getSchoolByOwnerId(1);
        CharacterColor color1 = school.getEntrance().get(0).getColor();
        CharacterColor color2 = school.getEntrance().get(1).getColor();
        CharacterColor color3 = ((CharacterCardwithStudents)board.getCharacterCardbyName("CLOWN")).getStudents().get(0).getColor();
        CharacterColor color4 = ((CharacterCardwithStudents)board.getCharacterCardbyName("CLOWN")).getStudents().get(1).getColor();
        actionMessage.setAction(Action.CHOOSE_CHARACTER_CARD);
        actionMessage.setCharacterCardName("CLOWN");
        assertNull(controller.nextAction(actionMessage));
        assertEquals(Action.USE_CHARACTER_CARD, controller.getPhase());
        actionMessage.setAction(Action.USE_CHARACTER_CARD);
        actionMessage.setData(2);
        assertNull(controller.nextAction(actionMessage));
        actionMessage.setParameter(color3.toString());
        actionMessage.setParameter(color1.toString());
        assertNull(controller.nextAction(actionMessage));
        assertEquals(6,((CharacterCardwithStudents)board.getCharacterCardbyName("CLOWN")).getStudents().size());
        assertEquals(7,board.getSchoolByOwnerId(1).getEntrance().size());
        actionMessage.getParameters().clear();
        actionMessage.setParameter(color4.toString());
        actionMessage.setParameter(color2.toString());
        assertEquals(6,((CharacterCardwithStudents)board.getCharacterCardbyName("CLOWN")).getStudents().size());
        assertEquals(7,board.getSchoolByOwnerId(1).getEntrance().size());



    }

    @Test
    void testDiplomat() {
        ActionMessage actionMessage = new ActionMessage();
        BoardExpert board = chooseDiplomat(actionMessage,0);
        assertNull(controller.nextAction(actionMessage));
        assertEquals(Action.USE_CHARACTER_CARD, controller.getPhase());
        actionMessage.setAction(Action.USE_CHARACTER_CARD);

        //USO IL DIPLOMAT SU UNA ISOLA SENZA STUDENTI E SENZA TORRI
        actionMessage.setData(6);
        assertNull(controller.nextAction(actionMessage));
        assertEquals(Action.DEFAULT_MOVEMENTS, controller.getPhase());
        assertEquals(0, board.getMotherNaturePosition());

        //USO IL DIPLOMAT SULL'ISOLA 11 - MN[0] + TORRE
        actionMessage = new ActionMessage();
        board = chooseDiplomat(actionMessage,0);
        assertNull(controller.nextAction(actionMessage));
        actionMessage.setAction(Action.USE_CHARACTER_CARD);
        actionMessage.setData(11);
        board.getSchoolByOwnerId(1).addDiningRoomStudent(new Student(CharacterColor.RED));
        board.getIslands().get(11).addStudent(new Student(CharacterColor.RED));
        board.getProfessorByColor(CharacterColor.RED).setOwner(1);
        board.moveTower(1,0,"Island");
        assertNull(controller.nextAction(actionMessage));
        assertEquals(Action.DEFAULT_MOVEMENTS, controller.getPhase());
        assertEquals(10, board.getMotherNaturePosition());
        assertEquals(PlayerColor.BLACK,board.getIslands().get(10).getTowers().get(0).getColor());
        assertEquals(2,board.getIslands().get(10).getTowers().size());
        assertEquals(11,board.getIslands().size());

        //USO IL DIPLOMAT SULL'ISOLA 1 - MN[5] - TORRI IN [0,2]
        actionMessage = new ActionMessage();
        board = chooseDiplomat(actionMessage,5);
        assertNull(controller.nextAction(actionMessage));
        actionMessage.setAction(Action.USE_CHARACTER_CARD);
        actionMessage.setData(1);
        board.getSchoolByOwnerId(1).addDiningRoomStudent(new Student(CharacterColor.RED));
        board.getIslands().get(1).addStudent(new Student(CharacterColor.RED));
        board.getProfessorByColor(CharacterColor.RED).setOwner(1);
        board.moveTower(1,0,"Island");
        board.moveTower(1,2,"Island");
        assertNull(controller.nextAction(actionMessage));
        assertEquals(Action.DEFAULT_MOVEMENTS, controller.getPhase());
        assertEquals(3, board.getMotherNaturePosition());
        assertEquals(PlayerColor.BLACK,board.getIslands().get(0).getTowers().get(0).getColor());
        assertEquals(3,board.getIslands().get(0).getTowers().size());
        assertEquals(10,board.getIslands().size());

        //USO IL DIPLOMAT SULL'ISOLA 8 - MN[5] - TORRI IN [7]
        actionMessage = new ActionMessage();
        board = chooseDiplomat(actionMessage,5);
        assertNull(controller.nextAction(actionMessage));
        actionMessage.setAction(Action.USE_CHARACTER_CARD);
        actionMessage.setData(8);
        board.getSchoolByOwnerId(1).addDiningRoomStudent(new Student(CharacterColor.RED));
        board.getIslands().get(8).addStudent(new Student(CharacterColor.RED));
        board.getProfessorByColor(CharacterColor.RED).setOwner(1);
        board.moveTower(1,7,"Island");
        assertNull(controller.nextAction(actionMessage));
        assertEquals(Action.DEFAULT_MOVEMENTS, controller.getPhase());
        assertEquals(5, board.getMotherNaturePosition());
        assertEquals(PlayerColor.BLACK,board.getIslands().get(7).getTowers().get(0).getColor());
        assertEquals(2,board.getIslands().get(7).getTowers().size());
        assertEquals(11,board.getIslands().size());

        //USO IL DIPLOMAT SULL'ISOLA 0 - MN[11] - TORRI IN [11]
        actionMessage = new ActionMessage();
        board = chooseDiplomat(actionMessage,11);
        assertNull(controller.nextAction(actionMessage));
        actionMessage.setAction(Action.USE_CHARACTER_CARD);
        actionMessage.setData(0);
        board.getSchoolByOwnerId(1).addDiningRoomStudent(new Student(CharacterColor.RED));
        board.getIslands().get(0).addStudent(new Student(CharacterColor.RED));
        board.getProfessorByColor(CharacterColor.RED).setOwner(1);
        board.moveTower(1,11,"Island");
        assertNull(controller.nextAction(actionMessage));
        assertEquals(Action.DEFAULT_MOVEMENTS, controller.getPhase());
        assertEquals(0, board.getMotherNaturePosition());
        assertEquals(PlayerColor.BLACK,board.getIslands().get(0).getTowers().get(0).getColor());
        assertEquals(2,board.getIslands().get(0).getTowers().size());
        assertEquals(11,board.getIslands().size());

        //USO IL DIPLOMAT SULL'ISOLA 11 - MN[11] - TORRI IN [0,11]
        actionMessage = new ActionMessage();
        board = chooseDiplomat(actionMessage,11);
        assertNull(controller.nextAction(actionMessage));
        actionMessage.setAction(Action.USE_CHARACTER_CARD);
        actionMessage.setData(11);
        board.getSchoolByOwnerId(1).addDiningRoomStudent(new Student(CharacterColor.RED));
        board.getIslands().get(11).addStudent(new Student(CharacterColor.RED));
        board.getProfessorByColor(CharacterColor.RED).setOwner(1);
        board.moveTower(1,0,"Island");
        board.moveTower(1,10,"Island");
        assertNull(controller.nextAction(actionMessage));
        assertEquals(Action.DEFAULT_MOVEMENTS, controller.getPhase());
        assertEquals(9, board.getMotherNaturePosition());
        assertEquals(PlayerColor.BLACK,board.getIslands().get(9).getTowers().get(0).getColor());
        assertEquals(3,board.getIslands().get(9).getTowers().size());
        assertEquals(10,board.getIslands().size());

        //USO IL DIPLOMAT SULL'ISOLA 8 - MN[5] - TORRI IN [7] - OLDOWNER[8]
        actionMessage = new ActionMessage();
        board = chooseDiplomat(actionMessage,5);
        assertNull(controller.nextAction(actionMessage));
        actionMessage.setAction(Action.USE_CHARACTER_CARD);
        actionMessage.setData(8);
        board.getSchoolByOwnerId(1).addDiningRoomStudent(new Student(CharacterColor.RED));
        board.getIslands().get(8).addStudent(new Student(CharacterColor.RED));
        board.getIslands().get(8).addStudent(new Student(CharacterColor.RED));
        board.moveTower(0,8,"Island");
        board.getProfessorByColor(CharacterColor.RED).setOwner(1);
        board.moveTower(1,7,"Island");
        assertNull(controller.nextAction(actionMessage));
        assertEquals(Action.DEFAULT_MOVEMENTS, controller.getPhase());
        assertEquals(5, board.getMotherNaturePosition());
        assertEquals(PlayerColor.BLACK,board.getIslands().get(7).getTowers().get(0).getColor());
        assertEquals(2,board.getIslands().get(7).getTowers().size());
        assertEquals(11,board.getIslands().size());
    }

    private BoardExpert chooseDiplomat(ActionMessage actionMessage, int motherNPosition){
        setUp();
        set(controller, actionMessage);
        BoardExpert board = (BoardExpert) gameModel.getBoard();
        board.createThreeCharacterCards(2);
        board.addCointoPlayer(1);
        board.addCointoPlayer(1);
        actionMessage.setAction(Action.MOVE_MOTHER_NATURE);
        actionMessage.setData(motherNPosition);
        controller.getActionController().moveMotherNature(actionMessage);
        actionMessage.setAction(Action.CHOOSE_CHARACTER_CARD);
        actionMessage.setCharacterCardName("DIPLOMAT");
        return board;
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