package it.polimi.ingsw.server.ConnectionMessage;
import it.polimi.ingsw.client.View;

public interface ServerMessage extends Message{
    void forward(View view);
}

