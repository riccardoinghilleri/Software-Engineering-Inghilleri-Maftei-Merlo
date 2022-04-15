package it.polimi.ingsw.model;

import it.polimi.ingsw.model.enums.CharacterColor;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class BoardTest {
    Board board;
    GameModel gameModel;
    List<Player> players;
    Board board3;

    @BeforeEach
    void setUp() {
        players=new ArrayList<>();
        players.add(new Player("manu",1));
        players.add(new Player("ricky",2));
        gameModel= new GameModel(false);
        board=new Board(players,gameModel);
        players.add(new Player("dani",3));
        board3 = new Board(players,gameModel);
    }

    @Test
    public void Board(){
        assertEquals(2,board.getClouds().length);
        assertEquals(2,board.getSchools().length);
        assertEquals(3,board3.getClouds().length);
        assertEquals(3,board3.getSchools().length);
        assertEquals(board.getSchools()[0],board.getSchoolByOwner("manu"));
        assertEquals(7,board.getSchoolByOwner("manu").getEntrance().size());
        assertEquals(7,board.getSchoolByOwner("ricky").getEntrance().size());
        assertEquals(board.getSchools()[1],board.getSchoolByOwner("ricky"));
        assertEquals(12,board.getIslands().size());
        for(CharacterColor c:CharacterColor.values()){
            assertNotNull(board.getProfessorByColor(c));
            assertSame(c,board.getProfessorByColor(c).getColor());
        }
    }

    @Test
    public void TestgetMotherNaturePosition(){
        assertTrue(board.getIslands().get(0).hasMotherNature());
        assertEquals(0,board.getMotherNaturePosition());
        board.moveMotherNature(3);
        assertTrue(board.getIslands().get(3).hasMotherNature());
        assertFalse(board.getIslands().get(0).hasMotherNature());
        assertEquals(3,board.getMotherNaturePosition());
        board.moveMotherNature(10);
        assertTrue(board.getIslands().get(1).hasMotherNature());
        assertFalse(board.getIslands().get(3).hasMotherNature());
        assertEquals(1,board.getMotherNaturePosition());
    }

    @Test
    public void TestInitialEntrance(){
        assertEquals(9,board3.getSchoolByOwner("manu").getEntrance().size());
        assertEquals(9,board3.getSchoolByOwner("ricky").getEntrance().size());
        assertEquals(9,board3.getSchoolByOwner("dani").getEntrance().size());
    }

    @Test
    public void TestsetStudentsonClouds(){
        board.setStudentsonClouds();
        assertEquals(3,board.getClouds()[0].getStudents().size());
        assertEquals(3,board.getClouds()[1].getStudents().size());
        board3.setStudentsonClouds();
        assertEquals(4,board3.getClouds()[0].getStudents().size());
        assertEquals(4,board3.getClouds()[1].getStudents().size());
        assertEquals(4,board3.getClouds()[2].getStudents().size());
    }

    @Test
    public void TestMoveStudentFromCloud(){
        board.setStudentsonClouds();
        board.moveStudent(0,"manu");
        assertTrue(board.getClouds()[0].getStudents().isEmpty());
        assertEquals(10,board.getSchoolByOwner("manu").getEntrance().size());
        board3.setStudentsonClouds();
        board3.moveStudent(0,"manu");
        assertTrue(board3.getClouds()[0].getStudents().isEmpty());
        assertEquals(13,board3.getSchoolByOwner("manu").getEntrance().size());
    }

    @Test
    public void TestMoveStudentFromSchool(){
        CharacterColor color=null;
        assertEquals(7,board.getSchoolByOwner("manu").getEntrance().size());
        assertTrue(board.getIslands().get(6).getStudents().isEmpty());
        for(CharacterColor c: CharacterColor.values())
        {
            if(board.getSchoolByOwner("manu").hasEntranceStudentColor(c.toString())){
            color=c;
            break;
            }
        }
        board.moveStudent("manu",6,color.toString());
        assertEquals(6,board.getSchoolByOwner("manu").getEntrance().size());
        assertEquals(1,board.getIslands().get(6).getStudents().get(color).size());
        assertEquals(color,board.getIslands().get(6).getStudents().get(color).get(0).getColor());
    }

    @Test
    public void TestUpdateProfessor(){
        for(CharacterColor c: CharacterColor.values())
            assertEquals("NONE",board.getProfessorByColor(c).getOwner());
        Student student=new Student(CharacterColor.valueOf("GREEN"));
        Student student1=new Student(CharacterColor.valueOf("PINK"));
        Student student2=new Student(CharacterColor.valueOf("YELLOW"));
        Student student3=new Student(CharacterColor.valueOf("YELLOW"));
        Student student4=new Student(CharacterColor.valueOf("RED"));

        board.getSchoolByOwner("manu").addDiningRoomStudent(student);
        board.getSchoolByOwner("manu").addDiningRoomStudent(student1);
        board.getSchoolByOwner("manu").addDiningRoomStudent(student2);
        board.updateProfessor(student.getColor());
        board.updateProfessor(student1.getColor());
        board.updateProfessor(student2.getColor());
        board.getSchoolByOwner("ricky").addDiningRoomStudent(student3);
        board.getSchoolByOwner("ricky").addDiningRoomStudent(student4);
        board.updateProfessor(student3.getColor());
        board.updateProfessor(student4.getColor());

        assertEquals("manu",board.getProfessorByColor(student.getColor()).getOwner());
        assertEquals("manu",board.getProfessorByColor(student1.getColor()).getOwner());
        assertEquals("manu",board.getProfessorByColor(student2.getColor()).getOwner());
        assertEquals("manu",board.getProfessorByColor(student3.getColor()).getOwner());
        assertEquals("ricky",board.getProfessorByColor(student4.getColor()).getOwner());

    }

    @AfterEach
    void tearDown() {
    }
}