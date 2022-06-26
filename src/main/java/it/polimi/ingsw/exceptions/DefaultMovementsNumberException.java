package it.polimi.ingsw.exceptions;

/**
 * Exception thrown when a player wants to do another move, but has already finished all the default movements available
 */
public class DefaultMovementsNumberException extends Exception {

    public String getMessage() {
        return "You have finished your default movements!";
    }
}
