package it.polimi.ingsw.exceptions;

/**
 * Exception thrown when a player chooses a cloud empty, because it has been already chosen by another player
 */
public class EmptyCloudException extends Exception {

    public String getMessage() {
        return "You can not choose this cloud. Someone else has already chosen it! ";
    }
}
