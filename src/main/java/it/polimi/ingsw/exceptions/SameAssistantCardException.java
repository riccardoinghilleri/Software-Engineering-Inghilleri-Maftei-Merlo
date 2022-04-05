package it.polimi.ingsw.exceptions;

public class SameAssistantCardException extends Exception{

    public String getMessage() {
        return "You can not choose this assistant card! Someone else has already chosen it!";
    }
}
