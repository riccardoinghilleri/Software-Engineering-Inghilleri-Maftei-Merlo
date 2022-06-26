package it.polimi.ingsw.exceptions;

/**
 * Exception thrown when a player chooses an assistant card already chosen by another player
 */
public class SameAssistantCardException extends Exception{

    public String getMessage() {
        return "You can not choose this assistant card! Someone else has already chosen it!";
    }
}
