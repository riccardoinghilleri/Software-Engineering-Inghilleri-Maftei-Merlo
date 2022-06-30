package it.polimi.ingsw.server.ConnectionMessage;

import it.polimi.ingsw.client.View;
/**
 *  The only parameter of the class is a boolean: alreadyAsked.
 *  Based on this parameter, the view changes the request to the player.
 */


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
