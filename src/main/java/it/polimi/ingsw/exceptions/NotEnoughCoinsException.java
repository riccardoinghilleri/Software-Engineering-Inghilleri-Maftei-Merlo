package it.polimi.ingsw.exceptions;

public class NotEnoughCoinsException extends Exception {

    public String getMessage() {
        return "You can not use this Special Card. You have not got enough coins!";
    }
}
