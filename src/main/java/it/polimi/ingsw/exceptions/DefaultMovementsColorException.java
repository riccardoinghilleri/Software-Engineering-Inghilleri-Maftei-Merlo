package it.polimi.ingsw.exceptions;

public class DefaultMovementsColorException extends Exception {

    public String getMessage() {
        return "You have not this color in your school's entrance";
    }
}
