package it.polimi.ingsw.server.ConnectionMessage;

public class InfoMessage implements Message{
    private final String string;

    public InfoMessage(String string) {
        this.string = string;
    }

    public String getString() {
        return this.string;
    }
}
