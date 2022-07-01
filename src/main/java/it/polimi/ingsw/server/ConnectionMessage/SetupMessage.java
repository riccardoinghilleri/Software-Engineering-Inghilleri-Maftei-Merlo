package it.polimi.ingsw.server.ConnectionMessage;
/**
 * This message is used by the client and contains an attribute of type string.
 * It is used to communicate the choice of nickname, color and wizard
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

    /**
     * @return the string with the value of choice.
     */
    public String getString() {
        return this.string;
    }
}