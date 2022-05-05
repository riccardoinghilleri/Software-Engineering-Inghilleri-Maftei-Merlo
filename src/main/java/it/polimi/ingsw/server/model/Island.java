package it.polimi.ingsw.server.model;

import it.polimi.ingsw.enums.CharacterColor;
import it.polimi.ingsw.enums.PlayerColor;
import it.polimi.ingsw.constants.Constants;

import java.io.Serializable;
import java.util.*;
import java.lang.*;

public class Island implements Serializable {
    private boolean hasMotherNature;
    private Map<CharacterColor, List<Student>> students;
    private List<Tower> towers;
    private boolean NoEntryTile; // prima c'era isLocked

    public Island(boolean hasMotherNature) {
        this.hasMotherNature = hasMotherNature;
        NoEntryTile = false;
        towers = new ArrayList<>();
        students = new HashMap<>();
        for (CharacterColor c : CharacterColor.values()) {
            this.students.put(c, new ArrayList<>());
        }
    }


    public Island(Student firstStudent) {
        this.hasMotherNature = false;
        NoEntryTile = false;
        towers = new ArrayList<>();
        students = new HashMap<>();
        for (CharacterColor c : CharacterColor.values()) {
            this.students.put(c, new ArrayList<>());
        }
        students.put(firstStudent.getColor(), new ArrayList<>());
        students.get(firstStudent.getColor()).add(firstStudent);
    }

    //---GETTER---//

    public boolean hasMotherNature() {
        return hasMotherNature;
    }

    public Map<CharacterColor, List<Student>> getStudents() {
        return students;
    }

    public List<Tower> getTowers() {
        return towers;
    }

    public boolean hasNoEntryTile() {  //forse da cambiare nome al metodo
        return NoEntryTile;
    } //prima era isLocked

    public PlayerColor getColorTower() {
        if (!towers.isEmpty()) return towers.get(0).getColor();
        return null;
    }

    //---SETTER---//

    public void setMotherNature(boolean hasMotherNature) {
        this.hasMotherNature = hasMotherNature;
    }

    public void setNoEntryTile(boolean locked) {
        NoEntryTile = locked;
    } //forse da cambiare nome al metodo

    public void addTower(Tower tower) {
        towers.add(tower);
    }

    public void addTowers(List<Tower> towers) {
        this.towers.addAll(towers);
    }

    public void addStudent(Student student) {
        this.students.get(student.getColor()).add(student);
    }

    public void addStudents(List<Student> students) {
        for (Student s : students) {
            addStudent(s);
        }
    }

    public List<Tower> removeTowers() {
        List<Tower> tempTowers = new ArrayList<>(towers);
        towers.clear();
        return tempTowers;

    }

    public StringBuilder draw() {
        StringBuilder island = new StringBuilder();
        int towers_index = 0;
        String horizontal_wall = "═══════════════";
        String to_right_wall = "/";
        String to_left_wall = "\\";
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 21; j++) {
                if (i == 0 || i == 5) {
                    if (j == 3) island.append(horizontal_wall);
                    else island.append(" ");
                } else if ((i == 1 && j == 1) || (i == 2 && j == 0) || (i == 3 && j == 20) || (i == 4 && j == 19)) {
                    island.append(to_right_wall);
                } else if ((i == 1 && j == 19) || (i == 2 && j == 20) || (i == 3 && j == 0) || (i == 4 && j == 1)) {
                    island.append(to_left_wall);
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
                    if (j > 4 && j%2!=0 && j > (21 - towers.size()) / 2 && towers_index < towers.size()) {
                        island.append(towers.get(towers_index));
                        towers_index++;
                    } else island.append(" ");
                } else if (i == 4) {
                    if (j == 8 && hasMotherNature) island.append("M");
                    else if (j == 12 && hasNoEntryTile()) island.append("X");
                    else island.append(" ");
                }
            }
            island.append("\n");
        }
        return island;
    }


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