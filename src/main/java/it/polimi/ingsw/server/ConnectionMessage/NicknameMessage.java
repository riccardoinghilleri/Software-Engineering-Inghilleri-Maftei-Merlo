package it.polimi.ingsw.server.ConnectionMessage;

import it.polimi.ingsw.client.View;
/**
 * Message used in the particular case when a player logins
 * with a name already used by another player.
 * The server informs the client that the name can't be used.
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
