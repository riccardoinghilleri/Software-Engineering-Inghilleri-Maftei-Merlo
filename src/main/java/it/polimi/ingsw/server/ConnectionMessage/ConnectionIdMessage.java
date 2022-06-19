package it.polimi.ingsw.server.ConnectionMessage;

public class ConnectionIdMessage implements Message {

    private final int id;

    public ConnectionIdMessage(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }
}
