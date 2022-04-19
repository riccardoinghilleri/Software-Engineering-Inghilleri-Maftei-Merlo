package it.polimi.ingsw.controller.actioncontroller;

import it.polimi.ingsw.server.ConnectionMessage.ActionMessage;
import it.polimi.ingsw.server.model.GameModel;
import it.polimi.ingsw.server.model.Student;
import it.polimi.ingsw.server.model.BoardExpert;
import it.polimi.ingsw.server.model.enums.CharacterColor;

public class Performer implements CharacterCardStrategy {

    GameModel gameModel;
    BoardExpert board;

    public Performer(GameModel gameModel) {
        this.gameModel = gameModel;
        board = (BoardExpert) gameModel.getBoard();
    }


    @Override
    public void useEffect(ActionMessage actionMessage) {
        Student s1= board.getSchoolByOwner(gameModel.getCurrentPlayer().getNickname()).removeEntranceStudent(CharacterColor.valueOf(actionMessage.getFirstParameter()));
        Student s2= board.getSchoolByOwner(gameModel.getCurrentPlayer().getNickname()).removeDiningRoomStudent(CharacterColor.valueOf(actionMessage.getSecondParameter()));
        board.getSchoolByOwner(gameModel.getCurrentPlayer().getNickname()).addEntranceStudent(s2);
        board.getSchoolByOwner(gameModel.getCurrentPlayer().getNickname()).addDiningRoomStudent(s1);
    }
}