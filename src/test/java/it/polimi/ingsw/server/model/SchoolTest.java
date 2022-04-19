package it.polimi.ingsw.server.model;

import it.polimi.ingsw.server.model.enums.CharacterColor;
import it.polimi.ingsw.server.model.enums.PlayerColor;
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
        school.addEntranceStudent(new Student(CharacterColor.PINK));
        school.addEntranceStudent(new Student(CharacterColor.GREEN));
        school.addDiningRoomStudent(new Student(CharacterColor.RED));
        school.addDiningRoomStudent(new Student(CharacterColor.YELLOW));
        school.addDiningRoomStudent(new Student(CharacterColor.BLUE));
        String result = "Owner: Ricky" +
                        "\nTowers: 8 WHITE" +
                        "\nEntrance: RED PINK GREEN " +
                        "\nDiningRoom:" +
                        "\nRED: 1" +
                        "\nBLUE: 1" +
                        "\nYElLOW: 1"+
                        "\nPINK: 0" +
                        "\nGREEN: 0";
        assertEquals(result,school.toString());
    }

}