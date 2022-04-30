package it.polimi.ingsw.server.ConnectionMessage;

import it.polimi.ingsw.client.View;

public class NicknameMessage implements Message,ServerMessage{
    private final boolean alreadyAsked;

    public NicknameMessage(boolean alreadyAsked) {
        this.alreadyAsked = alreadyAsked;
    }
    public boolean getAlreadyAsked() {
        return alreadyAsked;
    }

    @Override
    public void forward(View view) {
        view.setupNickname(this);
    }
}
