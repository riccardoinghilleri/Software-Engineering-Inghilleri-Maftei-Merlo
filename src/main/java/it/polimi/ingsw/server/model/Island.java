package it.polimi.ingsw.server.model;

import it.polimi.ingsw.enums.CharacterColor;
import it.polimi.ingsw.enums.PlayerColor;

import java.io.Serializable;
import java.util.*;

/**
 * This class represents the element 'island' on the board.
 * It can contain students, towers, motherNature or noEntryTiles when the game
 * is in Expert mode.
 */
public class Island implements Serializable {
    private boolean hasMotherNature;
    private final Map<CharacterColor, List<Student>> students;
    private final List<Tower> towers;
    private int noEntryTile; // prima c'era isLocked

    /**
     This is the constructor of the first and sixth island,
     which respectively are initialized with only MotherNature and with nothing on it.
     * @param hasMotherNature boolean which specifies if on the island there is motherNature or not.
     */
    public Island(boolean hasMotherNature) {
        this.hasMotherNature = hasMotherNature;
        noEntryTile = 0;
        towers = new ArrayList<>();
        students = new HashMap<>();
        for (CharacterColor c : CharacterColor.values()) {
            this.students.put(c, new ArrayList<>());
        }
    }

    /**
     * Constructor of all island, except for 0 and 6.Each one is initialized with one random student,
     * specified in the parameter firstStudent.
     * @param firstStudent firstStudent to add to the island.
     */
    public Island(Student firstStudent) {
        this.hasMotherNature = false;
        noEntryTile = 0;
        towers = new ArrayList<>();
        students = new HashMap<>();
        for (CharacterColor c : CharacterColor.values()) {
            this.students.put(c, new ArrayList<>());
        }
        students.put(firstStudent.getColor(), new ArrayList<>());
        students.get(firstStudent.getColor()).add(firstStudent);
    }


    /**
     * This method return true is there is MotherNature on the island , false vice-versa.
     */
    public boolean hasMotherNature() {
        return hasMotherNature;
    }

    /**
     * This method returns the map of students on the island.
     */
    public Map<CharacterColor, List<Student>> getStudents() {
        return students;
    }

    /**
     * This method returns the list of towers on the island.
     */
    public List<Tower> getTowers() {
        return towers;
    }

    /**
     * This method returns true if the island is marked as locked, false vice-versa.
     */
    public boolean hasNoEntryTile() {
        return noEntryTile != 0;
    }

    /**
     * This method return the tower color, which is an instance of the PlayerColor.
     * (Player and tower have the same color)
     */
    public PlayerColor getColorTower() {
        if (!towers.isEmpty()) return towers.get(0).getColor();
        return null;
    }


    /** This method sets the boolean hasMotherNature
     * @param hasMotherNature specifies if motherNature is on the island or not.
     * */

    public void setMotherNature(boolean hasMotherNature) {
        this.hasMotherNature = hasMotherNature;
    }

    /** This method sets the boolean locked
     * @param locked specifies whether to add or remove a noEntryTile from the island.
     * */
    public void setNoEntryTile(boolean locked) {
        if(locked)
            noEntryTile++;
        else noEntryTile--;
    }

    /** This method return the number of noEntryTiles on the island*/
    public int getNoEntryTile(){
        return noEntryTile;
    }

    /** This method adds a single tower to the list of towers*/
    public void addTower(Tower tower) {
        towers.add(tower);
    }

    /** This method adds a list of towers to the previous list of towers.*/
    public void addTowers(List<Tower> towers) {
        this.towers.addAll(towers);
    }

    /** This method adds a single student to the list of students.
     * @param student student to add to the list.
     * */
    public void addStudent(Student student) {
        this.students.get(student.getColor()).add(student);
    }

    /** This method adds a list student to the previous list of students*/
    public void addStudents(List<Student> students) {
        for (Student s : students) {
            addStudent(s);
        }
    }

    /**
     * This method remove a list of towers from the island
     * (e.g. it is used when 3 islands are merged and all the elements from one island need to be moved to the other one'
     */
    public List<Tower> removeTowers() {
        List<Tower> tempTowers = new ArrayList<>(towers);
        towers.clear();
        return tempTowers;

    }
}