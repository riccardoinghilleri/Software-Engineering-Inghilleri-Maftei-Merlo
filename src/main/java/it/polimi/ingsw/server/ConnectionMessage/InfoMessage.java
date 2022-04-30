package it.polimi.ingsw.server.ConnectionMessage;

import it.polimi.ingsw.client.View;

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
