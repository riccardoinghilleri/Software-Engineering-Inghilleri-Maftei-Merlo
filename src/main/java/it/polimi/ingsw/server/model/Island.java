package it.polimi.ingsw.server.model;

import it.polimi.ingsw.enums.CharacterColor;
import it.polimi.ingsw.enums.PlayerColor;
import it.polimi.ingsw.constants.Constants;

import java.io.Serializable;
import java.util.*;
import java.lang.*;

/**
 * This class represents the element 'island' on the board.
 * It can contain students, towers or motherNature.
 */
public class Island implements Serializable {
    private boolean hasMotherNature;
    private Map<CharacterColor, List<Student>> students;
    private List<Tower> towers;
    private int noEntryTile; // prima c'era isLocked

    /**
     This is the constructor of the first and sixth island,
     which respectively is initialized with only MotherNature and is empty.
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
     * This is the constructor of the other islands, which are initialized with one random student,
     * specified in the parameter firstStudent.
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

    //---GETTER---//

    /**
     * This method return true is there is the MotherNature on the island , false vice-versa
     */
    public boolean hasMotherNature() {
        return hasMotherNature;
    }

    /**
     * This method returns the map of students
     */
    public Map<CharacterColor, List<Student>> getStudents() {
        return students;
    }
    /**
     * This method returns the list of towers
     */
    public List<Tower> getTowers() {
        return towers;
    }
    /**
     * This method returns true if there the island is marked as locked, false vice-versa
     */
    public boolean hasNoEntryTile() {  //forse da cambiare nome al metodo
        if (noEntryTile == 0) return false;
        else return true;
    } //prima era isLocked

    /**
     * This method return the tower color, which is a instance of the PlayerColor.
     * (Player and tower have the same color)
     */
    public PlayerColor getColorTower() {
        if (!towers.isEmpty()) return towers.get(0).getColor();
        return null;
    }

    //---SETTER---//

    /** This method sets the boolean hasMotherNature*/
    public void setMotherNature(boolean hasMotherNature) {
        this.hasMotherNature = hasMotherNature;
    }
    /** This method sets the boolean locked*/
    public void setNoEntryTile(boolean locked) {
        if(locked)
            noEntryTile++;
        else noEntryTile--;
    } //forse da cambiare nome al metodo

    public int getNoEntryTile(){
        return noEntryTile;
    }
    /** This method adds a single tower to the list of towers*/
    public void addTower(Tower tower) {
        towers.add(tower);
    }
    /** This method adds a tower to the previous list of towers, which already exists*/
    public void addTowers(List<Tower> towers) {
        this.towers.addAll(towers);
    }

    /** This method adds a single tower to the list of towers*/
    public void addStudent(Student student) {
        this.students.get(student.getColor()).add(student);
    }
    /** This method adds a list student to the previous list of students, which already exists*/
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
/*
    public StringBuilder draw(int x, int y, int pos) {
        StringBuilder island = new StringBuilder();
        island.append(Constants.cursorUp(y));
        int towers_index = 0;
        String horizontal_wall = "═══════════════";
        String to_right_wall = "/";
        String to_left_wall = "\\";
        for (int i = 0; i < 6; i++) {
            island.append(Constants.cursorRight(x));
            for (int j = 0; j < 21; j++) {
                if (i == 0 || i == 5) {
                    if (j == 3) {
                        island.append(horizontal_wall);
                        j = 17;
                    } else island.append(" ");
                } else if ((i == 1 && j == 1) || (i == 2 && j == 0) || (i == 3 && j == 20) || (i == 4 && j == 19)) {
                    island.append(to_right_wall);
                } else if ((i == 1 && j == 19) || (i == 2 && j == 20) || (i == 3 && j == 0) || (i == 4 && j == 1)) {
                    island.append(to_left_wall);
                } else if (i == 1 && j == 3) {
                    island.append("#");
                    if (pos < 10) {
                        island.append(pos);
                        island.append(" ");
                    } else {
                        island.append(pos / 10);
                        island.append(pos % 10);
                    }
                    j = 5;
                } else if (i == 1) {
                    if (j == 8) {
                        island.append(Constants.getAnsi(CharacterColor.BLUE));
                        island.append(students.get(CharacterColor.BLUE).size());
                        island.append(Constants.ANSI_RESET);
                    } else if (j == 12) {
                        island.append(Constants.getAnsi(CharacterColor.PINK));
                        island.append(students.get(CharacterColor.PINK).size());
                        island.append(Constants.ANSI_RESET);
                    } else island.append(" ");
                } else if (i == 2) {
                    if (j == 6) {
                        island.append(Constants.getAnsi(CharacterColor.GREEN));
                        island.append(students.get(CharacterColor.GREEN).size());
                        island.append(Constants.ANSI_RESET);
                    } else if (j == 10) {
                        island.append(Constants.getAnsi(CharacterColor.YELLOW));
                        island.append(students.get(CharacterColor.YELLOW).size());
                        island.append(Constants.ANSI_RESET);
                    } else if (j == 14) {
                        island.append(Constants.getAnsi(CharacterColor.RED));
                        island.append(students.get(CharacterColor.RED).size());
                        island.append(Constants.ANSI_RESET);
                    } else island.append(" ");
                } else if (i == 3) {
                    if (j > 4 && ((j % 2 != 0 && ((21 - towers.size() * 2 - 1) / 2) % 2 == 0)
                            || (j % 2 == 0 && ((21 - towers.size() * 2 - 1) / 2) % 2 != 0))
                            && j > (21 - towers.size() * 2 - 1) / 2 && towers_index < towers.size()) {
                        island.append(towers.get(towers_index));
                        towers_index++;
                    } else island.append(" ");
                } else if (i == 4) {
                    if (j == 8 && hasMotherNature) island.append("M");
                    else if (j == 11 && hasNoEntryTile()) {
                        island.append(noEntryTile+"X");
                        j=13;
                    }
                    else island.append(" ");
                }
            }
            island.append("\n");
        }
        return island;
    }*/


    @Override
    public String toString() {
        String result;
        if (towers.isEmpty()) {
            result = "Towers: NONE\nStudents: ";
        } else {
            result = "Towers: " + towers.size() + " " + towers.get(0).getColor() + "\nStudents: ";
        }
        for (CharacterColor key : students.keySet()) {
            for (Student student : students.get(key)) {
                result = result.concat(student.toString() + "\t");
            }
        }
        if (hasNoEntryTile()) result = result.concat("\nNo Entry Tile!");
        return result +
                "\nMotherNature: " + hasMotherNature();
    }
}