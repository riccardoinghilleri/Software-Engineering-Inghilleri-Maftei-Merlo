package it.polimi.ingsw.model;

import it.polimi.ingsw.model.enums.CharacterColor;
import it.polimi.ingsw.model.enums.PlayerColor;

import java.util.*;
import java.lang.*;

public class Island {
    private boolean hasNatureMother;
    private Map<CharacterColor,List<Student>> students;
    private List<Tower> towers;
    private boolean isLocked;

    public Island(boolean hasNatureMother) {

    }

    public Island(Student firstStudent) {

    }

    //---GETTER---//

    private boolean hasNatureMother() {
        return hasNatureMother;
    }

    public Map<CharacterColor, List<Student>> getStudents() {
        return students;
    }

    public List<Tower> getTowers() {
        return towers;
    }

    public boolean isLocked() {
        return isLocked;
    }

    public PlayerColor getColorTower() {
        if(!towers.isEmpty()) return towers.get(0).getColor();
        return null;
    }

    //---SETTER---//

    public void setNatureMother(boolean hasNatureMother) {
        this.hasNatureMother = hasNatureMother;
    }

    public void setLock(boolean locked) {
        isLocked = locked;
    }

    public void addTower(Tower tower) {

    }

    public void addTowers(List<Tower> towers) {

    }

    public void addStudent(Student student) {
        if(!(students.containsKey(student.getColor())))
        {
            this.students.put(student.getColor(), new ArrayList<Student>());
        }
        students.get(student.getColor()).add(student);
    }

    public void addStudents(List<Student> students) { //TODO il parametro in teoria è una collection perchè la values() ritorna una collection

    }

    public List<Tower> removeTowers() {
        List<Tower> tempTowers = new ArrayList<>(towers);
        towers.clear();
        return tempTowers;
    }
}
