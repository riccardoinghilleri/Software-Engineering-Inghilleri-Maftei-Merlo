package it.polimi.ingsw.exceptions;

public class IncorrectPhaseException extends Exception{

    public String getMessage() {
        return "You are not in this phase!";
    }
}
