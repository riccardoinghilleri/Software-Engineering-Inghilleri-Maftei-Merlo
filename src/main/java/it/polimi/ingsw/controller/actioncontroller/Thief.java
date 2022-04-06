package it.polimi.ingsw.controller.actioncontroller;

import it.polimi.ingsw.controller.Message;
import it.polimi.ingsw.model.GameModel;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.BoardExpert;
import it.polimi.ingsw.model.enums.CharacterColor;

public class Thief implements CharacterCardStrategy{

    GameModel gameModel;
    BoardExpert board;

    public Thief(GameModel gameModel) {
        this.gameModel = gameModel;
        board = (BoardExpert) gameModel.getBoard();
    }
    @Override
    public void useEffect(Message message) {
        CharacterColor color=CharacterColor.valueOf(message.getFirstParameter());
        for(Player p: gameModel.getPlayers()){
            for(int i=0;i<3;i++){
                if(board.getSchoolByOwner(p.getNickname()).getDiningRoom().get(color).size()>0)
                    board.getSchoolByOwner(p.getNickname()).removeDiningRoomStudent(color);
            }

        }
    }
}



