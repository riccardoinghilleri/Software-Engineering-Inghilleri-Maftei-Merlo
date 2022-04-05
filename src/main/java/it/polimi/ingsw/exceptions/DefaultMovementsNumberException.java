package it.polimi.ingsw.exceptions;

public class DefaultMovementsNumberException extends Exception {

    public String getMessage() {
        return "You have finished your default movements!";
    }
}
