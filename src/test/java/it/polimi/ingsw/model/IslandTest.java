package it.polimi.ingsw.model;

import it.polimi.ingsw.model.enums.CharacterColor;
import it.polimi.ingsw.model.enums.PlayerColor;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

class IslandTest {
    /*
      ricordiamoci che c'è anche un metodo privato e non posso testare che l isola con madre natura
      inizialmente sia creata in modo corretto
     */


    /**
     * testiamo creazione corretta dell'isola senza nè madre natura nè uno studente
     */
    @Test
    public void testIslandWithoutStudent() {
        boolean hasNatureMother = false;
        Island island = new Island(hasNatureMother);
        assertFalse(island.hasNoEntryTile());
        assertTrue(island.getTowers().isEmpty());
        for(CharacterColor c : island.getStudents().keySet())
            assertTrue(island.getStudents().get(c).isEmpty());
    }

    @Test
    void testIsland() {
        CharacterColor color = CharacterColor.values()[new Random().nextInt(CharacterColor.values().length)];
        Student firstStudent = new Student(color);
        Island island = new Island(firstStudent);
        assertFalse(island.hasNoEntryTile());
        assertTrue(island.getTowers().isEmpty());
        assertEquals(firstStudent, island.getStudents().get(color).get(0));

    }

    @Test
    void testingCorrectTowerColor() {
        CharacterColor color = CharacterColor.values()[new Random().nextInt(CharacterColor.values().length)];
        Student firstStudent = new Student(color);
        PlayerColor playerColor = PlayerColor.values()[new Random().nextInt(PlayerColor.values().length)];
        Island island = new Island(firstStudent);
        assertNull(island.getColorTower());
        Tower tower = new Tower("randomPlayer", playerColor);
        assertTrue(island.getTowers().isEmpty());
        island.addTower(tower);
        assertEquals(1, island.getTowers().size());
        assertSame(tower, island.getTowers().get(0));
        assertSame(playerColor, island.getColorTower());


    }

    /**
     * Metodo che testa se sono aggiunte correttamente una torre
     */
    @Test
    void testAddTowers() {
        CharacterColor color = CharacterColor.values()[new Random().nextInt(CharacterColor.values().length)];
        Student firstStudent = new Student(color);
        PlayerColor playerColor = PlayerColor.values()[new Random().nextInt(PlayerColor.values().length)];
        Island island = new Island(firstStudent);
        List<Tower> towersToAdd = new ArrayList<>();
        towersToAdd.add(new Tower("player1", PlayerColor.GREY));
        towersToAdd.add(new Tower("player2", PlayerColor.GREY));
        towersToAdd.add(new Tower("player3", PlayerColor.GREY));
        island.addTowers(towersToAdd);
        assertFalse(island.getTowers().isEmpty());
        island.removeTowers();
        assertTrue(island.getTowers().isEmpty());
    }

    @Test
    void testAddStudents() {
        boolean hasNatureMother = false;
        Island island = new Island(hasNatureMother);
        //CharacterColor color = CharacterColor.values()[new Random().nextInt(CharacterColor.values().length)];
        for(CharacterColor c : island.getStudents().keySet())
            assertTrue(island.getStudents().get(c).isEmpty());
        Student firstStudent = new Student(CharacterColor.YELLOW);
        island.addStudent(firstStudent);
        assertEquals(1,island.getStudents().get(CharacterColor.YELLOW).size());
        assertSame(firstStudent, island.getStudents().get(CharacterColor.YELLOW).get(0));
        assertFalse(island.getStudents().isEmpty());
        List<Student> studentsToAdd = new ArrayList<>();
        studentsToAdd.add(new Student(CharacterColor.PINK));
        studentsToAdd.add(new Student(CharacterColor.PINK));
        studentsToAdd.add(new Student(CharacterColor.PINK));
        studentsToAdd.add(new Student(CharacterColor.RED));
        studentsToAdd.add(new Student(CharacterColor.RED));
        studentsToAdd.add(new Student(CharacterColor.GREEN));
        island.addStudents(studentsToAdd);
        assertFalse(island.getStudents().isEmpty());
        assertEquals(3,island.getStudents().get(CharacterColor.PINK).size());
        assertEquals(1,island.getStudents().get(CharacterColor.YELLOW).size());
        assertEquals(1,island.getStudents().get(CharacterColor.GREEN).size());
        assertEquals(2,island.getStudents().get(CharacterColor.RED).size());
    }

    @Test
    public void testToString() {
        Island island = new Island(true);
        island.addStudent(new Student(CharacterColor.RED));
        island.addStudent(new Student(CharacterColor.RED));
        island.addStudent(new Student(CharacterColor.GREEN));
        island.addStudent(new Student(CharacterColor.BLUE));
        island.toString();
        island.addTower(new Tower("Ricky",PlayerColor.WHITE));
        island.toString();
    }
}



