package it.polimi.ingsw.server.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * This class is the main class of the model.
 *  It creates the Board and the Players.
 */

public class GameModel implements Serializable {
    private int playersNumber;
    private final boolean isExpertGame;
    private Board board = null;
    private final List<Player> players;
    private Player winner;
    private Player currentPlayer;

    /** Constructor GameModel creates a new GameModel instance.
     *Initializes the players number, the array of players and the winner.
     * @param isExpertGame specifies if the game needs to be normal or expert.
     */
    public GameModel(boolean isExpertGame) {
        playersNumber = 0;
        this.isExpertGame = isExpertGame;
        players = new ArrayList<>();
        winner = null;
    }

    /**
     * Method createBoard creates a Board, according to the value of the parameter
     * isExpertGame.
     */
    //---CREAZIONE OGGETTI---//
    public void createBoard() {
        if (!isExpertGame)
            board = new Board(this);
        else board = new BoardExpert(this);
        //currentPlayer=players.get(0); //mi serve nel gamehandler il primissimo turno quando non ho settato ancora le assistant cards
    }

    /**
     * It adds a player to the list of players.
     * @param nickname nickname that the player has chosen.
     * @param clientID id that the game uniquely assigned to a player.
     */
    public void createPlayer(String nickname, int clientID) {
        players.add(new Player(nickname, clientID));
        playersNumber++;
    }

    //---GETTER---//

    /**
     * @return the value of the boolean isExpertGame.
     */
    public boolean isExpertGame() {
        return isExpertGame;
    }

    /**
     * @return the number of the players involved in the current game.
     */
    public int getPlayersNumber() {
        return playersNumber;
    }

    /**
     * @return the Board previously created.
     */
    public Board getBoard() {
        return board;
    }

    /**
     * @return the list of the players.
     */
    public List<Player> getPlayers() {
        return players;
    }

    /**
     * @return the player who is playing.
     */
    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    /**
     * Method setCurrentPlayer sets the current player by taking it from the array
     * of all players in the specified position.
     * @param position current player's position in the array of all players.
     */
    public void setCurrentPlayer(int position) {
        currentPlayer = players.get(position);
    }

    /**
     * Return the player with the specified Id.
     * @param id associated with a player.
     * @return the player with the specified ID.
     */
    public Player getPlayerById(int id) {
        Player result = null;
        for (Player p : players) {
            if (p.getClientID() == id)
                result = p;
        }
        return result;
    }

    /**
     * @return the winner.
     */
    public Player getWinner() {
        return winner;
    }

    //---SETTER---//

    /**
     Method setPlayerOrder sets the order of the players according to the chosen
     AssistantCard priority.
     * It uses the list of players and the method playerComparator.
     */

    public void setPlayersOrder() {
        Collections.sort(players, new PlayerComparator());

    }

    /**
     * Method SetPlayerDeck is called when the player chooses the wizard.
     * @param clientId id the of player who has chosen the wizard.
     * @param wizard the name of the chosen wizard.
     */
    public void setPlayerDeck(int clientId, String wizard) {
        getPlayerById(clientId).getDeck().setWizard(wizard);

    }

    /**
     * Method setWinner sets the winner of game.
     * @param winner the player who won the match.
     */
    public void setWinner(Player winner) {
        this.winner = winner;
    }

}
/**
 * Private class that allows the gameModel to sort the players according to the
 * priority of the chosen assistant cards.
 */
class PlayerComparator implements Comparator<Player> {
    @Override
    public int compare(Player p1, Player p2) {
        return Integer.compare(p1.getChosenAssistantCard().getPriority(), p2.getChosenAssistantCard().getPriority());
    }
}

