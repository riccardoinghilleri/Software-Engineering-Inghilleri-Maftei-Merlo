package it.polimi.ingsw.server.model;

import it.polimi.ingsw.constants.Constants;
import it.polimi.ingsw.enums.CharacterColor;
import it.polimi.ingsw.enums.PlayerColor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


import static org.junit.jupiter.api.Assertions.*;

class BoardTest {
    GameModel gameModel;
    GameModel gameModel3;
    GameModel gameModel4;

    Board board;
    Board board3;
    Board board4;

    @BeforeEach
    void setUp() {
        gameModel = new GameModel(false);
        gameModel.createPlayer("manu", 0);
        gameModel.createPlayer("ricky", 1);
        gameModel.getPlayerById(0).setColor("WHITE");
        gameModel.getPlayerById(1).setColor("BLACK");
        board = new Board(gameModel);


        gameModel3 = new GameModel(false);
        gameModel3.createPlayer("manu", 0);
        gameModel3.createPlayer("ricky", 1);
        gameModel3.createPlayer("dani", 2);
        gameModel3.getPlayerById(0).setColor("WHITE");
        gameModel3.getPlayerById(1).setColor("BLACK");
        gameModel3.getPlayerById(2).setColor("GREY");
        board3 = new Board(gameModel3);


        gameModel4 = new GameModel(false);
        gameModel4.createPlayer("manu", 0);
        gameModel4.createPlayer("ricky", 1);
        gameModel4.createPlayer("dani", 2);
        gameModel4.createPlayer("teo", 3);
        gameModel4.getPlayerById(0).setColor("WHITE");
        gameModel4.getPlayerById(1).setColor("BLACK");
        gameModel4.getPlayerById(2).setColor("BLACK");
        gameModel4.getPlayerById(3).setColor("WHITE");
        board4 = new Board(gameModel4);


    }

    @Test
    public void Board() {
        assertEquals(2, board.getClouds().length);
        assertEquals(2, board.getSchools().length);
        assertEquals(3, board3.getClouds().length);
        assertEquals(3, board3.getSchools().length);
        assertEquals(4, board4.getClouds().length);
        assertEquals(4, board4.getSchools().length);
        assertEquals(board.getSchools()[0], board.getSchoolByOwnerId(0));
        assertEquals(board.getSchools()[1], board.getSchoolByOwnerId(1));
        assertEquals(12, board.getIslands().size());
        for (int i = 0; i < 12; i++) {
            int numStudents = 0;
            for (CharacterColor c : CharacterColor.values()) {
                numStudents += board.getIslands().get(i).getStudents().get(c).size();
            }
            if (i == 0 || i == 6)
                assertEquals(0, numStudents);
            else assertEquals(1, numStudents);
        }
        for (CharacterColor c : CharacterColor.values()) {
            assertNotNull(board.getProfessorByColor(c));
            assertSame(c, board.getProfessorByColor(c).getColor());
        }
        assertEquals(board4.getSchools()[0].getTowersNumber(), 8);
        assertEquals(board4.getSchools()[0].getTowerColor(), PlayerColor.WHITE);
        assertEquals(board4.getSchools()[1].getTowersNumber(), 8);
        assertEquals(board4.getSchools()[1].getTowerColor(), PlayerColor.BLACK);
        assertEquals(board4.getSchools()[2].getTowersNumber(), 0);
        assertEquals(board4.getSchools()[2].getTowersNumber(), 0);
    }

    @Test
    public void testGetGameModel() {
        assertEquals(gameModel, board.getGameModel());
        assertEquals(gameModel3, board3.getGameModel());
        assertEquals(gameModel4, board4.getGameModel());
    }

    @Test
    public void testGetStudentsSize() {
        assertEquals(130 - 10 - 7 * 2, board.getStudentsSize());
        assertEquals(130 - 10 - 9 * 3, board3.getStudentsSize());
        assertEquals(130 - 10 - 7 * 4, board4.getStudentsSize());
    }

    @Test
    public void testGetPlayersNumber() {
        assertEquals(2, board.getPlayersNumber());
        assertEquals(3, board3.getPlayersNumber());
        assertEquals(4, board4.getPlayersNumber());
    }

    @Test
    public void testGetMotherNaturePosition() {
        assertTrue(board.getIslands().get(0).hasMotherNature());
        assertEquals(0, board.getMotherNaturePosition());
        board.moveMotherNature(3);
        assertTrue(board.getIslands().get(3).hasMotherNature());
        assertFalse(board.getIslands().get(0).hasMotherNature());
        assertEquals(3, board.getMotherNaturePosition());
        board.moveMotherNature(10);
        assertTrue(board.getIslands().get(1).hasMotherNature());
        assertFalse(board.getIslands().get(3).hasMotherNature());
        assertEquals(1, board.getMotherNaturePosition());
    }

    @Test
    public void testInitialEntrance() {
        assertEquals(7, board.getSchoolByOwnerId(0).getEntrance().size());
        assertEquals(7, board.getSchoolByOwnerId(1).getEntrance().size());
        assertEquals(9, board3.getSchoolByOwnerId(0).getEntrance().size());
        assertEquals(9, board3.getSchoolByOwnerId(1).getEntrance().size());
        assertEquals(9, board3.getSchoolByOwnerId(2).getEntrance().size());
        assertEquals(7, board4.getSchoolByOwnerId(0).getEntrance().size());
        assertEquals(7, board4.getSchoolByOwnerId(1).getEntrance().size());
        assertEquals(7, board4.getSchoolByOwnerId(2).getEntrance().size());
        assertEquals(7, board4.getSchoolByOwnerId(3).getEntrance().size());

    }

    @Test
    public void testSetStudentsOnClouds() {
        board.setStudentsonClouds();
        assertEquals(3, board.getClouds()[0].getStudents().size());
        assertEquals(3, board.getClouds()[1].getStudents().size());
        board3.setStudentsonClouds();
        assertEquals(4, board3.getClouds()[0].getStudents().size());
        assertEquals(4, board3.getClouds()[1].getStudents().size());
        assertEquals(4, board3.getClouds()[2].getStudents().size());
    }

    @Test
    public void testMoveStudentFromCloud() {
        board.setStudentsonClouds();
        board.moveStudent(0, 0);
        assertTrue(board.getClouds()[0].getStudents().isEmpty());
        assertEquals(10, board.getSchoolByOwnerId(0).getEntrance().size());
        board3.setStudentsonClouds();
        board3.moveStudent(0, 0);
        assertTrue(board3.getClouds()[0].getStudents().isEmpty());
        assertEquals(13, board3.getSchoolByOwnerId(0).getEntrance().size());
    }

    @Test
    public void testMoveStudentFromSchool() {
        CharacterColor color = CharacterColor.GREEN;
        assertEquals(7, board.getSchoolByOwnerId(0).getEntrance().size());
        for (CharacterColor c : CharacterColor.values())
            assertTrue(board.getIslands().get(6).getStudents().get(c).isEmpty());

        for (CharacterColor c : CharacterColor.values()) {
            if (board.getSchoolByOwnerId(0).hasEntranceStudentColor(c.toString())) {
                color = c;
                break;
            }
        }
        board.moveStudent(0, 6, color.toString());
        assertEquals(6, board.getSchoolByOwnerId(0).getEntrance().size());
        assertEquals(1, board.getIslands().get(6).getStudents().get(color).size());
        assertEquals(color, board.getIslands().get(6).getStudents().get(color).get(0).getColor());
    }

    @Test
    public void testUpdateProfessor() {
        for (CharacterColor c : CharacterColor.values())
            assertEquals(-1, board.getProfessorByColor(c).getOwner());
        Student student = new Student(CharacterColor.valueOf("GREEN"));
        Student student0 = new Student(CharacterColor.valueOf("PINK"));
        Student student1 = new Student(CharacterColor.valueOf("YELLOW"));
        Student student2 = new Student(CharacterColor.valueOf("RED"));

        assertEquals(Constants.getAnsi(CharacterColor.GREEN)+"●"+Constants.ANSI_RESET,student.toString());

        // [GREEN - PINK - YELLOW] [--]
        board.getSchoolByOwnerId(0).addDiningRoomStudent(student);
        board.getSchoolByOwnerId(0).addDiningRoomStudent(student0);
        board.getSchoolByOwnerId(0).addDiningRoomStudent(student1);
        board.updateProfessor(student.getColor());
        board.updateProfessor(student0.getColor());

        // [GREEN - PINK - YELLOW] [YELLOW - RED]
        board.getSchoolByOwnerId(1).addDiningRoomStudent(student1);
        board.getSchoolByOwnerId(1).addDiningRoomStudent(student2);
        board.updateProfessor(student1.getColor());
        board.updateProfessor(student2.getColor());

        assertEquals(0, board.getProfessorByColor(student.getColor()).getOwner());  //[GREEN - 0]
        assertEquals(0, board.getProfessorByColor(student0.getColor()).getOwner()); //[PINK - 0]
        assertEquals(0, board.getProfessorByColor(student1.getColor()).getOwner()); //[YELLOW - 0]
        assertEquals(1, board.getProfessorByColor(student2.getColor()).getOwner()); //[RED - 1]

        //[GREEN - PINK - YELLOW] [YELLOW - RED - GREEN - GREEN - PINK - PINK]
        board.getSchoolByOwnerId(1).addDiningRoomStudent(student);
        board.getSchoolByOwnerId(1).addDiningRoomStudent(student);
        board.getSchoolByOwnerId(1).addDiningRoomStudent(student0);
        board.getSchoolByOwnerId(1).addDiningRoomStudent(student0);
        board.updateProfessor(student.getColor());
        board.updateProfessor(student0.getColor());
        assertEquals(1, board.getProfessorByColor(student.getColor()).getOwner()); //[GREEN - 1]
        assertEquals(1, board.getProfessorByColor(student0.getColor()).getOwner()); //[PINK - 1]


    }

    @Test
    public void testGetTotalInfluence() {
        Student student1 = new Student(CharacterColor.valueOf("GREEN"));
        Student student2 = new Student(CharacterColor.valueOf("PINK"));
        Student student3 = new Student(CharacterColor.valueOf("YELLOW"));
        Student student4 = new Student(CharacterColor.valueOf("RED"));
        board.getIslands().get(6).addStudent(student1);
        board.getIslands().get(6).addStudent(student2);
        board.getIslands().get(6).addStudent(student3);
        board.getSchoolByOwnerId(0).addDiningRoomStudent(student1);
        board.updateProfessor(student1.getColor());
        assertEquals(0, board.getProfessorByColor(CharacterColor.valueOf("GREEN")).getOwner());
        assertEquals(0, board.getTotalInfluence(6));
        board.getSchoolByOwnerId(1).addDiningRoomStudent(student3);
        board.updateProfessor(CharacterColor.valueOf("YELLOW"));
        assertEquals(1, board.getProfessorByColor(student3.getColor()).getOwner());
        assertEquals(-1, board.getTotalInfluence(6));
        board.moveTower(1, 6, "island");
        assertEquals(1, board.getTotalInfluence(6));

        board4.getIslands().get(6).addStudent(student1);
        board4.getIslands().get(6).addStudent(student2);
        board4.getIslands().get(6).addStudent(student3);
        board4.getIslands().get(6).addStudent(student4);
        //la squadra bianca ha influenza sui colori verde e giallo
        //la squadra nera ha influenza sul rosa
        board4.getSchoolByOwnerId(0).addDiningRoomStudent(student1);
        board4.updateProfessor(student1.getColor());
        board4.getSchoolByOwnerId(2).addDiningRoomStudent(student3);
        board4.updateProfessor(student3.getColor());
        board4.getSchoolByOwnerId(1).addDiningRoomStudent(student2);
        board4.updateProfessor(student2.getColor());
        assertEquals(0, board4.getProfessorByColor(student1.getColor()).getOwner());
        assertEquals(2, board4.getProfessorByColor(student3.getColor()).getOwner());
        assertEquals(1, board4.getProfessorByColor(student2.getColor()).getOwner());
        assertEquals(-1, board4.getProfessorByColor(CharacterColor.RED).getOwner());
        assertEquals(0, board4.getTotalInfluence(6));
        board4.moveTower(1, 6, "island");
        assertEquals(-1, board4.getTotalInfluence(6));
        board4.getSchoolByOwnerId(3).addDiningRoomStudent(student4);
        board4.updateProfessor(student4.getColor());
        assertEquals(1, board4.getTotalInfluence(6));
    }

    @Test
    public void testMoveTowerFromIsland() {
        board.moveTower(1, 6, "island");
        assertEquals(7, board.getSchoolByOwnerId(1).getTowersNumber());
        assertEquals(1, board.getIslands().get(6).getTowers().size());
        board.moveTower(1, 6, "school");
        assertEquals(8, board.getSchoolByOwnerId(1).getTowersNumber());
        assertEquals(0, board.getIslands().get(6).getTowers().size());
    }

    @Test
    public void testCheckNearIsland() {
        int num_students = 0;
        assertEquals(12, board.getIslands().size());
        board.moveTower(0, 0, "island");
        board.moveTower(0, 1, "island");
        board.moveTower(0, 2, "island");
        assertEquals(5, board.getSchoolByOwnerId(0).getTowersNumber());
        board.checkNearIsland(1,false);
        assertEquals(10, board.getIslands().size());
        assertEquals(3, board.getIslands().get(0).getTowers().size());
        for (CharacterColor c : CharacterColor.values()) {
            num_students += board.getIslands().get(0).getStudents().get(c).size();
        }
        assertEquals(2, num_students);
        num_students = 0;
        board.moveTower(0, 1, "island");
        board.checkNearIsland(0,false);
        assertEquals(9, board.getIslands().size());
        assertEquals(4, board.getIslands().get(0).getTowers().size());
        for (CharacterColor c : CharacterColor.values()) {
            num_students += board.getIslands().get(0).getStudents().get(c).size();
        }
        assertEquals(3, num_students);


        board.moveTower(0, 7, "island");
        board.moveTower(0, 8, "island");
        assertEquals(1,board.getIslands().get(7).getTowers().size());
        assertEquals(1,board.getIslands().get(8).getTowers().size());

        board.checkNearIsland(8,false);
        assertEquals(7, board.getIslands().size());
        assertEquals(6, board.getIslands().get(6).getTowers().size());

        board.moveTower(0, 0, "island");
        board.checkNearIsland(0,false);
        assertEquals(6, board.getIslands().size());
        assertEquals(7, board.getIslands().get(0).getTowers().size());
        assertTrue(board.getIslands().get(0).hasMotherNature());

    }

    @Test
    public void testFindWinner() {
        board.moveTower(1, 0, "island");
        board.findWinner();
        assertEquals(gameModel.getPlayerById(1), gameModel.getWinner());
        board.moveTower(0, 1, "island");
        board.findWinner();
        assertNull(gameModel.getWinner());
        board.getProfessorByColor(CharacterColor.RED).setOwner(1);
        board.findWinner();
        assertEquals(gameModel.getPlayerById(1), gameModel.getWinner());
        board.getProfessorByColor(CharacterColor.GREEN).setOwner(0);
        board.findWinner();
        assertNull(gameModel.getWinner());
    }

}