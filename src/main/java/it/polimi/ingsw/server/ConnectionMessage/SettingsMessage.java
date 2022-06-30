package it.polimi.ingsw.server.ConnectionMessage;
/**
 * This class represents the message sent at the begging of the game by the client.
 * Every player is asked if he wants to play in expert mode and how many competitors wants in the game.
 */


public class SettingsMessage implements Message{
    private int playersNumber;
    private boolean expertMode;
    /**
     * The constructor of the class
     */

    public SettingsMessage(int playersNumber,boolean expertMode){
        this.playersNumber=playersNumber;
        this.expertMode=expertMode;
    }
    /**
     * getters of the 2 parameters
     */

    public int getPlayersNumber() {
        return playersNumber;
    }

    public boolean isExpertMode() {
        return expertMode;
    }
}

