package it.polimi.ingsw.model;

import java.util.ArrayList;
import java.util.List;

public class GameModel {
    int playersNumber;
    boolean isHardcore;
    Board board;
    List<Player> players;

    public GameModel(boolean isHardcore)
    {
        playersNumber=0;
        this.isHardcore=isHardcore;
        players=new ArrayList<>();
    }
    public int getPlayersNumber() {
        return playersNumber;
    }

    public Board getGameBoard(){ return board; }

    public List<Player> getPlayers(){ return players; }

    public Player getCurrentPlayer(){return players.get(0); }

    public Player getPlayerByNickname(String nickname) {
        Player result = null;
        for(Player p:players)
        {
            if(p.getNickname().equals(nickname))
                result=p;
        }
        return result;
    }


}
