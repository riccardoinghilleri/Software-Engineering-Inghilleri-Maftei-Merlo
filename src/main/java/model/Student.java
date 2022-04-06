package model;

import model.enums.CharacterColor;

public class Student {
    private final CharacterColor color;

    public Student(CharacterColor color) {
        this.color = color;
    }

    public CharacterColor getColor() {
        return color;
    }
}
