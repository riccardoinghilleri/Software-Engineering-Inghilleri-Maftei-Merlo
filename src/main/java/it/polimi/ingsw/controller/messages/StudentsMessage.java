package it.polimi.ingsw.controller.messages;

import it.polimi.ingsw.controller.Action;

import java.util.ArrayList;
import java.util.List;

public class StudentsMessage implements Message {
    private final Action action;
    private int islandPosition;
    private List<String> studentsColor;
    private String schoolOwner;

    public StudentsMessage(Action action) {
        this.action = action;
        studentsColor = new ArrayList<String>();
    }

    @Override
    public Action getAction() {
        return action;
    }

    public int getIslandPosition() {
        return islandPosition;
    }

    public void setIslandPosition(int islandPosition) {
        this.islandPosition = islandPosition;
    }

    public List<String> getStudentsColor() {
        return studentsColor;
    }

    public void setStudentsColor(List<String> studentsColor) {
        this.studentsColor = studentsColor;
    }

    public String getSchoolOwner() {
        return schoolOwner;
    }

    public void setSchoolOwner(String schoolOwner) {
        this.schoolOwner = schoolOwner;
    }
}
