package it.polimi.ingsw.server.ConnectionMessage;

public class SetupMessage extends ClientMessage{
    private int date;

    public int getDate() {
        return date;
    }

    public void setDate(int date) {
        this.date = date;
    }
}
