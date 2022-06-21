package it.polimi.ingsw.server.ConnectionMessage;

public class ConnectionIdMessage implements Message {

    private final int id;

    private final Boolean lastPlayer;

    public ConnectionIdMessage(int id) {

        this.id = id;
        this.lastPlayer=null;
    }
    public ConnectionIdMessage(int id, Boolean lastPlayer) {

        this.id = id;
        this.lastPlayer=lastPlayer;
    }

    public int getId() {
        return id;
    }

    public Boolean isLastPlayer(){
        return this.lastPlayer;
    }


}
