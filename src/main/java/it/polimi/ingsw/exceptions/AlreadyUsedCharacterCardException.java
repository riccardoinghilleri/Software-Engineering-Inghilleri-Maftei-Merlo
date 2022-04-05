package it.polimi.ingsw.exceptions;

public class AlreadyUsedCharacterCardException extends Exception {

    public String getMessage() {
        return "You can not use a character card anymore in this turn!";
    }
}
