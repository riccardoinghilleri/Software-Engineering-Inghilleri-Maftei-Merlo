package it.polimi.ingsw.server.ConnectionMessage;
/**
 * This message,from the client, contains a string-type attribute to which can be assigned a nickname,a color and a magician
 * during the setup phase.
 */

public class SetupMessage implements Message{
    private final String string;

    /**
     * Constructor of the class
     */
    public SetupMessage(String string) {
        this.string = string;
    }

    public String getString() {
        return this.string;
    }
}