package it.polimi.ingsw.model;

import it.polimi.ingsw.model.enums.CharacterColor;
import it.polimi.ingsw.model.enums.PlayerColor;

import java.util.*;
import java.lang.*;

public abstract class Island {
    private boolean hasNatureMother;
    private Map<CharacterColor,List<Student>> students;
    private boolean isLocked;

    //---GETTER---//

    private boolean hasNatureMother() {
        return hasNatureMother;
    }

    public Map<CharacterColor, List<Student>> getStudents() {
        return students;
    }

    public boolean isLocked() {
        return isLocked;
    }

    public abstract PlayerColor getColorTower();

    //---SETTER---//

    public void setNatureMother(boolean hasNatureMother) {
        this.hasNatureMother = hasNatureMother;
    }

    public void setLock(boolean locked) {
        isLocked = locked;
    }

    public void addStudent(Student student) {
        if(!(students.containsKey(student.getColor())))
        {
            this.students.put(student.getColor(), new ArrayList<Student>());
        }
        students.get(student.getColor()).add(student);
    }
    /*
    public String getInfluence(boolean useTower, CharacterColor excludedColor) {

    }*/
}
