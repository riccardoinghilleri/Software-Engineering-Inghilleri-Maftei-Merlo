package it.polimi.ingsw.exceptions;

/**
 * Exception thrown when a player wants to move a student of a color that he doesn't have in his entrance.
 */

public class DefaultMovementsColorException extends Exception {

    public String getMessage() {
        return "You have not this color in your school's entrance";
    }
}
