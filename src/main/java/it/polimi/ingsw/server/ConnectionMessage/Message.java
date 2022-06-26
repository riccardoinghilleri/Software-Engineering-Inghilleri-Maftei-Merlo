package it.polimi.ingsw.server.ConnectionMessage;

import java.io.Serializable;
/**
 * This interface, which implements serializable,
 * represents the Message which is streamed between the server side and client side,
 * throw the connection established, to share all the information.
 * Different types of message implements this interface
 */


public interface Message extends Serializable{

}