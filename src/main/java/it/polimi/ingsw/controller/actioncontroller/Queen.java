package it.polimi.ingsw.controller.actioncontroller;

import it.polimi.ingsw.controller.Message;
import model.GameModel;
import model.CharacterCardwithStudents;
import model.Student;
import model.board.BoardExpert;
import model.enums.CharacterColor;

public class Queen implements CharacterCardStrategy {

    GameModel gameModel;
    BoardExpert board;

    public Queen(GameModel gameModel) {
        this.gameModel = gameModel;
        board = (BoardExpert) gameModel.getBoard();
    }

    @Override
    public void useEffect(Message message) {
        CharacterCardwithStudents character = (CharacterCardwithStudents)board.getCharacterCardbyName("QUEEN");
        Student s = character.removeStudent(CharacterColor.valueOf(message.getFirstParameter()));
        board.getSchoolByOwner(gameModel.getCurrentPlayer().getNickname()).addDiningRoomStudent(s);
        character.addStudent(board.removeRandomStudent());
    }
}