package it.polimi.ingsw.server.ConnectionMessage;

import it.polimi.ingsw.client.View;
/**
 * This class represents the message sent by the server to be displayed by the view.
 */


public class InfoMessage implements Message,ServerMessage{
    private final String string;

    public InfoMessage(String string) {
        this.string = string;
    }

    public String getString() {
        return this.string;
    }


    @Override
    public void forward(View view) {
        view.displayInfo(this);
    }
}
