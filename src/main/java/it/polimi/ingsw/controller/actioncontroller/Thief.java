package it.polimi.ingsw.controller.actioncontroller;

import it.polimi.ingsw.server.ConnectionMessage.ActionMessage;
import it.polimi.ingsw.server.model.GameModel;
import it.polimi.ingsw.server.model.Player;
import it.polimi.ingsw.server.model.BoardExpert;
import it.polimi.ingsw.enums.CharacterColor;

public class Thief implements CharacterCardStrategy{

    GameModel gameModel;
    BoardExpert board;

    public Thief(GameModel gameModel) {
        this.gameModel = gameModel;
        board = (BoardExpert) gameModel.getBoard();
    }
    @Override
    public void useEffect(ActionMessage actionMessage) {
        CharacterColor color=CharacterColor.valueOf(actionMessage.getParameters().get(0));
        for(Player p: gameModel.getPlayers()){
            for(int i=0;i<3;i++){
                if(board.getSchoolByOwnerId(p.getClientID()).getDiningRoom().get(color).size()>0)
                    board.getSchoolByOwnerId(p.getClientID()).removeDiningRoomStudent(color);
            }

        }
    }
}



