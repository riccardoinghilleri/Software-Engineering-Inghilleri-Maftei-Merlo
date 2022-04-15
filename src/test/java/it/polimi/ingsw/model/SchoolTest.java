package it.polimi.ingsw.model;

import it.polimi.ingsw.model.enums.CharacterColor;
import it.polimi.ingsw.model.enums.PlayerColor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SchoolTest {
    School school;

    @BeforeEach
    public void setUp() {
        school = new School("Ricky", PlayerColor.WHITE,2);
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
    }

    @Test
    void testToString() {
        school.addEntranceStudent(new Student(CharacterColor.RED));
        school.addEntranceStudent(new Student(CharacterColor.YELLOW));
        school.addEntranceStudent(new Student(CharacterColor.BLUE));
        school.toString();
    }

}