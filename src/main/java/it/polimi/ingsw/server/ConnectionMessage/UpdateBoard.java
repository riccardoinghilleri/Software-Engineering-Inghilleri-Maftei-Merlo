package it.polimi.ingsw.server.ConnectionMessage;

import it.polimi.ingsw.client.View;
import it.polimi.ingsw.constants.Constants;

public class UpdateBoard implements Message,ServerMessage{
    private final String string;

    public UpdateBoard(String string) {
        this.string = string;
    }

    public String getString() {
        return this.string;
    }


    @Override
    public void forward(View view) {
        Constants.clearScreen();
        view.displayInfo(new InfoMessage(string));
    }
}
