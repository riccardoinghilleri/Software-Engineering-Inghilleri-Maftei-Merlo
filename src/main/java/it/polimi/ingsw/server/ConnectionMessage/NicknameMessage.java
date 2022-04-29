package it.polimi.ingsw.server.ConnectionMessage;

public class NicknameMessage implements Message{
    private final boolean alreadyAsked;

    public NicknameMessage(boolean alreadyAsked) {
        this.alreadyAsked = alreadyAsked;
    }

    public boolean getAlreadyAsked() {
        return alreadyAsked;
    }
}
