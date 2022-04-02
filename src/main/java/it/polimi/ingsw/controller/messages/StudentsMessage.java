package it.polimi.ingsw.controller.messages;

import it.polimi.ingsw.controller.Action;

public class StudentsMessage implements Message {
    private final Action action;
    private int islandPosition;
    private String[] studentsColor;
    private String schoolOwner;

    public StudentsMessage(Action action) {
        this.action = action;
        //TODO Credo che per i colori mi serva la lista perchè non so se l'utente ne inserisce 1 o 2
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

    public String[] getStudentsColor() {
        return studentsColor;
    }

    public void setStudentsColor(String[] studentsColor) {
        this.studentsColor = studentsColor;
    }

    public String getSchoolOwner() {
        return schoolOwner;
    }

    public void setSchoolOwner(String schoolOwner) {
        this.schoolOwner = schoolOwner;
    }
}
