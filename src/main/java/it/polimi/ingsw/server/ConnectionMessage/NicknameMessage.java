package it.polimi.ingsw.server.ConnectionMessage;

import it.polimi.ingsw.client.View;

import java.util.NoSuchElementException;

/**
 *  The only parameter of the class is a boolean: alreadyAsked.
 *  Based on this parameter, the view changes the request to the player.
 */

public class NicknameMessage implements Message,ServerMessage{
    private final boolean alreadyAsked;

    /**
     * Constructor of the class
     */
    public NicknameMessage(boolean alreadyAsked) {
        this.alreadyAsked = alreadyAsked;
    }

    /**
     * @return a boolean whose value is true if the gameHandler is asking
     * the nickname again
     */
    public boolean getAlreadyAsked() {
        return alreadyAsked;
    }

    /**
     * This method calls the correct view method to handle the message.
     * @param view view that has to handle the message.
     */
    @Override
    public void forward(View view) {
        try {
            view.setupNickname(this);
        } catch (NoSuchElementException ignored) {
        }
    }
}
