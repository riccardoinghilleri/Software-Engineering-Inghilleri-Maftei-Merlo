package it.polimi.ingsw.model;

import it.polimi.ingsw.model.board.Board;
import it.polimi.ingsw.model.enums.Magician;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class GameModel {
    int playersNumber;
    boolean isHardcore;
    Board board;
    List<Player> players;
    List<String> winners;

    public GameModel(boolean isHardcore)
    {
        playersNumber=0;
        this.isHardcore=isHardcore;
        players=new ArrayList<>();
        winners=new ArrayList<>();
    }
    //---CREAZIONE OGGETTI---//
    //TODO FARE BOARD
    public void createBoard(){
        if (!isHardcore)
        board=new Board(players.size());
        //else board=new BoardHardImpl(players.size());
    };

    public void createPlayer(String nickname, int clientID){
        players.add(new Player(nickname,clientID));
    }

    //---GETTER---//

    public int getPlayersNumber() {
        return playersNumber;
    }

    public Board getBoard(){ return board; }

    public List<Player> getPlayers(){ return players; }

    //public Player getCurrentPlayer(){ return players.get(0); }

    public Player getPlayerByNickname(String nickname) {
        Player result = null;
        for(Player p:players)
        {
            if(p.getNickname().equals(nickname))
                result=p;
        }
        return result;
    }

    public List<String> getWinners() {
        return winners;
    }

    public int getPossibleNatureMotherSteps(String nicknamePlayer){
        return getPlayerByNickname(nicknamePlayer).getChoosenAssistantCard().getNatureMotherSteps();
    }

    //---SETTER---//

    public void setPlayersOrder(){
        Collections.sort(players,new PlayerComparator());
    }

    public void setPlayersDeck( String nickname, String magician){
        getPlayerByNickname(nickname).getDeck().setMagician(Magician.valueOf(magician));

    }

    public void setWinner(String winner){
        winners.add(winner);

    }

}

class PlayerComparator implements Comparator<Player> {
    @Override
    public int compare(Player p1,Player p2){
        if(p1.getChoosenAssistantCard().getPriority()<p2.getChoosenAssistantCard().getPriority())
            return -1;
        else if (p1.getChoosenAssistantCard().getPriority()>p2.getChoosenAssistantCard().getPriority())
            return 1;
        else return 0;
    }

}

