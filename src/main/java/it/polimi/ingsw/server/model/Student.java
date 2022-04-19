package it.polimi.ingsw.server.model;

import it.polimi.ingsw.server.model.enums.CharacterColor;

public class Student {
    private final CharacterColor color;

    public Student(CharacterColor color) {
        this.color = color;
    }

    public CharacterColor getColor() {
        return color;
    }

    @Override
    public String toString(){
        return color.toString();
    }
}
