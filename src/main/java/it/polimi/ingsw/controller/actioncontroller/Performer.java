package it.polimi.ingsw.controller.actioncontroller;

import it.polimi.ingsw.controller.Message;
import model.GameModel;
import model.Student;
import model.board.BoardExpert;
import model.enums.CharacterColor;

public class Performer implements CharacterCardStrategy {

    GameModel gameModel;
    BoardExpert board;

    public Performer(GameModel gameModel) {
        this.gameModel = gameModel;
        board = (BoardExpert) gameModel.getBoard();
    }


    @Override
    public void useEffect(Message message) {
        Student s1= board.getSchoolByOwner(gameModel.getCurrentPlayer().getNickname()).removeEntranceStudent(CharacterColor.valueOf(message.getFirstParameter()));
        Student s2= board.getSchoolByOwner(gameModel.getCurrentPlayer().getNickname()).removeDiningRoomStudent(CharacterColor.valueOf(message.getSecondParameter()));
        board.getSchoolByOwner(gameModel.getCurrentPlayer().getNickname()).addEntranceStudent(s2);
        board.getSchoolByOwner(gameModel.getCurrentPlayer().getNickname()).addDiningRoomStudent(s1);
    }
}