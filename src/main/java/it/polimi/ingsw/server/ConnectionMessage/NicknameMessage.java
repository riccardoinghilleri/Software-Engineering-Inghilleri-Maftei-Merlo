package it.polimi.ingsw.server.ConnectionMessage;

public class NicknameMessage implements Message{
    String message;

    public String getMessage() {
        return message;
    }

    public NicknameMessage(String message) {
        this.message = message;
    }
}
