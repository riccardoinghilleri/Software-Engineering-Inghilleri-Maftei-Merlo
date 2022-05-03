package it.polimi.ingsw.server.model;

import it.polimi.ingsw.enums.CharacterColor;
import it.polimi.ingsw.enums.PlayerColor;

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
