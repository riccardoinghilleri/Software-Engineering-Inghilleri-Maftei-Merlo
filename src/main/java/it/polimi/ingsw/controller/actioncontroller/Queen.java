package it.polimi.ingsw.controller.actioncontroller;

import it.polimi.ingsw.controller.Message;
import it.polimi.ingsw.model.GameModel;
import it.polimi.ingsw.model.CharacterCardwithStudents;
import it.polimi.ingsw.model.Student;
import it.polimi.ingsw.model.BoardExpert;
import it.polimi.ingsw.model.enums.CharacterColor;

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