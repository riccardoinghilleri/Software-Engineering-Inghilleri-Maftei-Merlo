package it.polimi.ingsw.server.model;

import it.polimi.ingsw.enums.CharacterColor;
import it.polimi.ingsw.enums.PlayerColor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SchoolTest {
    School school;
    Player ricky=new Player("Ricky",1);

    @BeforeEach
    public void setUp() {
        school = new School(ricky, PlayerColor.WHITE,2);
    }

    @Test
    void testGetTowerColor() {
        assertEquals(PlayerColor.WHITE,school.getTowerColor());
    }

    @Test
    void testHasEntranceStudents() {
        assertFalse(school.hasEntranceStudentColor("RED"));
    }

    @Test
    void testRemoveDiningRoomStudent() {
        Student student = new Student(CharacterColor.RED);
        school.addDiningRoomStudent(student);
        assertEquals(student,school.removeDiningRoomStudent(CharacterColor.RED));
    }

    @Test
    void testFromEntranceToDiningRoom() {
        school.addEntranceStudent(new Student(CharacterColor.RED));
        for(CharacterColor c : CharacterColor.values()) {
            assertTrue(school.getDiningRoom().get(c).isEmpty());
        }
        school.fromEntrancetoDiningRoom(CharacterColor.RED);
        assertEquals(1,school.getDiningRoom().get(CharacterColor.RED).size());
        school.addEntranceStudent(new Student(CharacterColor.BLUE));
        school.fromEntrancetoDiningRoom(CharacterColor.BLUE);
        assertEquals(1,school.getDiningRoom().get(CharacterColor.BLUE).size());
        assertEquals(1,school.getDiningRoom().get(CharacterColor.RED).size());
    }

    @Test
    public void testGetProfessorByColor(){
        Professor prof =new Professor(CharacterColor.RED);
        school.addProfessor(prof);
        assertEquals(prof,school.getProfessorByColor(CharacterColor.RED));
        assertNull(school.getProfessorByColor(CharacterColor.PINK));
    }

    @Test
    public void testRemoveProfessor(){
        assertNull(school.removeProfessor(CharacterColor.RED));
    }

    @Test
    public void testGetNumDiningRoomStudents(){
        assertEquals(0,school.getNumDiningRoomStudents());
        Student student =new Student(CharacterColor.PINK);
        school.addDiningRoomStudent(student);
        assertEquals(1,school.getNumDiningRoomStudents());
        school.addDiningRoomStudent(student);
        assertEquals(2,school.getNumDiningRoomStudents());
    }

    @Test
    public void testGetOwner(){
        assertEquals(ricky,school.getOwner());
    }

    @Test
    public void testGetTowers(){
        assertEquals(8,school.getTowers().size());
        assertEquals(PlayerColor.WHITE,school.getTowers().get(0).getColor());
    }

    @Test
    public void testGetProfessors(){
        assertTrue(school.getProfessors().isEmpty());
        Professor prof =new Professor(CharacterColor.RED);
        school.addProfessor(prof);
        assertEquals(1,school.getProfessors().size());
        assertEquals(prof,school.getProfessors().get(0));
    }

    @Test
    public void testSchool(){
        Player manu=new Player("manu",0);
        School school1= new School(manu,PlayerColor.BLACK,2);
        School school2= new School(school1);
        assertEquals(manu,school2.getOwner());
        assertEquals(school1.getTowers(),school2.getTowers());
        assertEquals(school1.getProfessors(),school2.getProfessors());
        assertEquals(school1.getNumDiningRoomStudents(),school2.getNumDiningRoomStudents());
        assertEquals(school1.getEntrance(),school2.getEntrance());
        assertEquals(school1.getDiningRoom(),school2.getDiningRoom());
    }
}