package it.polimi.ingsw.server.model;

import it.polimi.ingsw.server.model.enums.Wizard;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class GameModel {
    int playersNumber;
    boolean isExpertGame;
    Board board = null;
    List<Player> players;
    String winner;
    Player currentPlayer;

    public GameModel(boolean isExpertGame)
    {
        playersNumber = 0;
        this.isExpertGame = isExpertGame;
        players = new ArrayList<>();
        winner = null;
    }
    //---CREAZIONE OGGETTI---//
    public void createBoard(){
        if (!isExpertGame)
        board = new Board(players,this);
        else board = new BoardExpert(players,this);
        //currentPlayer=players.get(0); //mi serve nel gamehandler il primissimo turno quando non ho settato ancora le assistant cards
    }

    public void createPlayer(String nickname, int clientID){
        players.add(new Player(nickname,clientID));
    }

    //---GETTER---//

    public boolean isExpertGame() {
        return isExpertGame;
    }

    public int getPlayersNumber() {
        return playersNumber;
    }

    public Board getBoard() { return board; }

    public List<Player> getPlayers() { return players; }

    public Player getCurrentPlayer() {return currentPlayer; }

    public void setCurrentPlayer(int position) {
        currentPlayer=players.get(position);
    }

    public Player getPlayerByNickname(String nickname) {
        Player result = null;
        for(Player p:players)
        {
            if(p.getNickname().equals(nickname))
                result = p;
        }
        return result;
    }

    public Player getPlayerById(int id) {
        Player result = null;
        for(Player p:players)
        {
            if(p.getClientID()==id)
                result = p;
        }
        return result;
    }

    public String getWinner() {
        return winner;
    }

    //---SETTER---//

    public void setPlayersOrder(){
        Collections.sort(players,new PlayerComparator());

    }

    public void setPlayerDeck( String nickname, String wizard){
        getPlayerByNickname(nickname).getDeck().setWizard(wizard);

    }

    public void setWinner(String winner){
        this.winner = winner;
    }
    //Metodo che controlla se c'è un vincitore e ritorna il player
    public boolean checkEndGame() {
        //Caso finiscono i 10 turni gh
        //Caso 3 isole rimaste gh
        //caso finiscono le torri su una scuola gh
        //Caso finiscono i player dalla board gh
        return false;
    }

    public Player endGame() {
        Player winner = null;
        int towers_min = (getBoard().getSchools())[0].getTowersNumber(); //Mi salvo il numero di torri sella prima scuola
        for(int i=1; i<getBoard().getSchools().length;i++) {
            if((getBoard().getSchools())[i].getTowersNumber()<towers_min) {
                towers_min = (getBoard().getSchools())[i].getTowersNumber();
            }
        }
        return winner;
    }
}

class PlayerComparator implements Comparator<Player> {
    @Override
    public int compare(Player p1,Player p2){
        return Integer.compare(p1.getChosenAssistantCard().getPriority(),p2.getChosenAssistantCard().getPriority());
        /*if(p1.getChoosenAssistantCard().getPriority()<p2.getChoosenAssistantCard().getPriority())
            return -1;
        else if (p1.getChoosenAssistantCard().getPriority()>p2.getChoosenAssistantCard().getPriority())
            return 1;
        else return 0;*/
    }
}

