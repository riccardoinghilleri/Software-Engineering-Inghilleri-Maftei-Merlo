package it.polimi.ingsw.server.model;

import it.polimi.ingsw.constants.Constants;
import it.polimi.ingsw.enums.CharacterColor;

import java.io.Serializable;

/**
 * This class represents the element 'student'.
 * It has a color, and it will be displayed as a colorful circle on the screen(ClI version).
 */
public class Student implements Serializable {
    private final CharacterColor color;

    /**
     * Constructor of the class.
     * @param color color of the student.
     */
    public Student(CharacterColor color) {
        this.color = color;
    }

    /** Getter of the color*/
    public CharacterColor getColor() {
        return color;
    }

    /**
     Override of the toString method: the student will be presented as a
     colorful circle on the screen.
     */
    @Override
    public String toString(){
        return Constants.getAnsi(color)+"●"+Constants.ANSI_RESET;
    }
}
