package it.polimi.ingsw.server.ConnectionMessage;

/**
 * Class that represents the message sent to a particular client to set the clientId.
 */
public class ConnectionIdMessage implements Message {

    private final int id;
    private final Boolean lastPlayer;

    /**
     * Constructor of the class
     * @param id index of the connection.
     */
    public ConnectionIdMessage(int id) {
        this.id = id;
        this.lastPlayer=null;
    }

    /**
     * Constructor of the class
     * @param id index of the connection
     * @param lastPlayer boolean to check if it is the last connected player.
     */
    public ConnectionIdMessage(int id, Boolean lastPlayer) {
        this.id = id;
        this.lastPlayer=lastPlayer;
    }

    /**
     * @return the connection Id
     */
    public int getId() {
        return id;
    }

    /**
     * @return a boolean whose value is true if the connection to which the
     * message was sent was the last to connect
     */
    public Boolean isLastPlayer(){
        return this.lastPlayer;
    }


}
