package it.polimi.ingsw.server.ConnectionMessage;
import it.polimi.ingsw.client.View;
/**
 * This interface is a subclass of the Message interface.
 * The function of the forward method is to call the correct method of the View interface.
 * All the messages received on the client side implements the forward method.
 */

public interface ServerMessage extends Message{
    void forward(View view);
}

