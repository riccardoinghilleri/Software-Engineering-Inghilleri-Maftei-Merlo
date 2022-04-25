package it.polimi.ingsw.server.ConnectionMessage;

public class SettingsMessage implements Message{
    private int playersNumber;
    private boolean expertMode;

    public SettingsMessage(int playersNumber,boolean expertMode){
        this.playersNumber=playersNumber;
        this.expertMode=expertMode;
    }

    public int getPlayersNumber() {
        return playersNumber;
    }

    public boolean isExpertMode() {
        return expertMode;
    }
}

