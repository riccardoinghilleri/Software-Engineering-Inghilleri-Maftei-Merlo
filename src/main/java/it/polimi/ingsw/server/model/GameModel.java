package it.polimi.ingsw.server.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * This class is the main class of the model.
 *  It creates the objects Board and Player.
 *  It also manages if there is a winner and returns the player which wins.
 */

public class GameModel implements Serializable {
    private int playersNumber;
    private boolean isExpertGame;
    private Board board = null;
    private List<Player> players;
    private Player winner;
    private Player currentPlayer;

    /** Constructor GameModel creates a new GameModel instance.
     *Initializes the players number, the array of players and the winner.
     * @param isExpertGame it specifies if the game needs to be basic or expert
     */
    public GameModel(boolean isExpertGame) {
        playersNumber = 0;
        this.isExpertGame = isExpertGame;
        players = new ArrayList<>();
        winner = null;
    }

    /**
     * Method createBoard creates a Bord, according to the value of the parameter
     * isExpertGame, passing the list of player and the gameModel itself
     */
    //---CREAZIONE OGGETTI---//
    public void createBoard() {
        if (!isExpertGame)
            board = new Board(players, this);
        else board = new BoardExpert(players, this);
        //currentPlayer=players.get(0); //mi serve nel gamehandler il primissimo turno quando non ho settato ancora le assistant cards
    }

    /**
     * It adds a player to the list of players, throw the use of nickname and clientId
     * @param nickname nickname that the player chooses when logins
     * @param clientID id that the game associates univocally to a player
     */
    public void createPlayer(String nickname, int clientID) {
        players.add(new Player(nickname, clientID));
        playersNumber++;
    }

    //---GETTER---//

    /**
     * @return the value of the boolean isExpertGame
     */
    public boolean isExpertGame() {
        return isExpertGame;
    }

    /**
     * @return the number of the players involved in the current game
     */
    public int getPlayersNumber() {
        return playersNumber;
    }

    /**
     * @return the Board previously crated.
     */
    public Board getBoard() {
        return board;
    }

    /**
     * @return the list of the players
     */
    public List<Player> getPlayers() {
        return players;
    }

    /**
     * @return the player who is playing his turn when the method getCurrentPlayer is called
     */
    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    /**
     * Method setCurrentPlayer sets the current player taking it from the array of all players throw his position
     * on the array
     * @param position current player's position on the array of all players
     */
    public void setCurrentPlayer(int position) {
        currentPlayer = players.get(position);
    }

    /**
     * Method getPlayerById allows the GameModel to refer to a player not only throw the nickname but also throw the Id
     * used in all the settings.
     * @param id associated with a player
     * @return the player with the specified ID
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
     * @return the player winner if there is one
     */
    public Player getWinner() {
        return winner;
    }

    //---SETTER---//

    /**
     * Method setPlayerOrder sets the order according to which
     * the players are allowed to play.
     * It uses the list of players and the method playerComparator.
     */

    public void setPlayersOrder() {
        Collections.sort(players, new PlayerComparator());

    }

    /**
     * MethodSetPlayerDeck is called when the player chooses the wizard that want to play
     * It sets the deck to the specified player, throw clientId
     * @param clientId The unique id the of player who has chosen the wizard
     * @param wizard the wizard chosen
     */
    public void setPlayerDeck(int clientId, String wizard) {
        getPlayerById(clientId).getDeck().setWizard(wizard);

    }

    /**
     * Method setWinner sets the winner of the round passing the winner player as a parameter.
     */
    public void setWinner(Player winner) {
        this.winner = winner;
    }
    //Metodo che controlla se c'è un vincitore e ritorna il player

}

/**
 * As the name suggests the method PlayerComparator has in input 2 players and compare if the priorities of the 2 are equal,
 * the first major or minor than the second .
 */
class PlayerComparator implements Comparator<Player> {
    @Override
    public int compare(Player p1, Player p2) {
        return Integer.compare(p1.getChosenAssistantCard().getPriority(), p2.getChosenAssistantCard().getPriority());
    }
}

