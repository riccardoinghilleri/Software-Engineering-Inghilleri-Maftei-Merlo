package it.polimi.ingsw.exceptions;

/**
 * Exception thrown when a player a characterCard which has been already used by another player
 */

public class AlreadyUsedCharacterCardException extends Exception {

    public String getMessage() {
        return "You can not use a character card anymore in this turn!";
    }
}
