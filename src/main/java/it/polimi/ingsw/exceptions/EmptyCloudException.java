package it.polimi.ingsw.exceptions;

public class EmptyCloudException extends Exception {

    public String getMessage() {
        return "You can not choose this cloud. Someone else has already chosen it! ";
    }
}
