package it.polimi.ingsw.exceptions;

/**
 * Exception thrown when a player chooses motherNature steps higher than these allowed by the assistant Card
 */
public class InvalidChosenStepsException extends Exception {

    public String getMessage() {
        return "Invalid chosen steps!";
    }
}
