package it.polimi.ingsw.server.ConnectionMessage;

public class NicknameMessage implements Message{
    private final String message;

    public NicknameMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
