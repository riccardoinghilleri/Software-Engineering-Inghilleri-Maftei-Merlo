package it.polimi.ingsw.controller.actioncontroller;

import it.polimi.ingsw.server.ConnectionMessage.ActionMessage;
import it.polimi.ingsw.server.model.GameModel;
import it.polimi.ingsw.server.model.CharacterCardwithStudents;
import it.polimi.ingsw.server.model.Student;
import it.polimi.ingsw.server.model.BoardExpert;
import it.polimi.ingsw.server.model.enums.CharacterColor;

public class Queen implements CharacterCardStrategy {

    GameModel gameModel;
    BoardExpert board;

    public Queen(GameModel gameModel) {
        this.gameModel = gameModel;
        board = (BoardExpert) gameModel.getBoard();
    }

    @Override
    public void useEffect(ActionMessage actionMessage) {
        CharacterCardwithStudents character = (CharacterCardwithStudents)board.getCharacterCardbyName("QUEEN");
        Student s = character.removeStudent(CharacterColor.valueOf(actionMessage.getFirstParameter()));
        board.getSchoolByOwner(gameModel.getCurrentPlayer().getNickname()).addDiningRoomStudent(s);
        character.addStudent(board.removeRandomStudent());
    }
}