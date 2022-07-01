package it.polimi.ingsw.server.ConnectionMessage;
import it.polimi.ingsw.client.View;
/**
 * This interface is a subclass of the Message interface.
 * The forward method is used to call the correct method of the View interface.
 * All the messages received by the Clients extends this class.
 */

public interface ServerMessage extends Message{
    void forward(View view);
}

