package it.polimi.ingsw.server.ConnectionMessage;

public class SettingsMessage extends ClientMessage{
    private int playersNumber;
    private int gameMode;

    public int getPlayersNumber() {
        return playersNumber;
    }

    public void setPlayersNumber(int playersNumber) {
        this.playersNumber = playersNumber;
    }

    public int getGameMode() {
        return gameMode;
    }

    public void setGameMode(int gameMode) {
        this.gameMode = gameMode;
    }
}
