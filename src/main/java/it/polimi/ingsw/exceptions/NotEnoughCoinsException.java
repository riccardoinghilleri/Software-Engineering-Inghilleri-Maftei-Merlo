package it.polimi.ingsw.exceptions;

/**
 * Exception thrown when a player wants to use a character Card whose cost is higher than the coins that he owns.
 */
public class NotEnoughCoinsException extends Exception {

    public String getMessage() {
        return "You can not use this Special Card. You have not got enough coins!";
    }
}
