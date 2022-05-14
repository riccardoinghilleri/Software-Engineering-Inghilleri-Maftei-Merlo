package it.polimi.ingsw.exceptions;

/**
 * Exception thrown when a player tries to do a move which doesn't correspond to the phase.
 * (e.g. wanting to move a student when he is supposed to chose a cloud)
 */

public class IncorrectPhaseException extends Exception{

    public String getMessage() {
        return "You are not in this phase!";
    }
}
