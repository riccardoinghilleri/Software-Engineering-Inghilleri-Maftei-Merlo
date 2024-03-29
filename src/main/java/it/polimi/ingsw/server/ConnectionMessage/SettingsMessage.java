package it.polimi.ingsw.server.ConnectionMessage;
/**
 * This class represents the message sent at the begging of the game by the client.
 * Every player is asked if he wants to play in expert mode and how many
 * competitors wants in the game.
 */

public class SettingsMessage implements Message{
    private final int playersNumber;
    private final boolean expertMode;

    /**
     * The constructor of the class
     */
    public SettingsMessage(int playersNumber,boolean expertMode){
        this.playersNumber=playersNumber;
        this.expertMode=expertMode;
    }

    /**
     * @return number of players in the game
     */
    public int getPlayersNumber() {
        return playersNumber;
    }

    /**
     * @return boolean whose value is true if the client wants to play in expert Mode
     */
    public boolean isExpertMode() {
        return expertMode;
    }
}

