package it.polimi.ingsw.controller.actioncontroller;

import it.polimi.ingsw.controller.Message;
import it.polimi.ingsw.model.GameModel;
import it.polimi.ingsw.model.SpecialCardwithStudents;
import it.polimi.ingsw.model.Student;
import it.polimi.ingsw.model.board.BoardHard;
import it.polimi.ingsw.model.enums.CharacterColor;

public class Queen implements CharacterCardStrategy {

    GameModel gameModel;
    BoardHard board;

    public Queen(GameModel gameModel) {
        this.gameModel = gameModel;
        board = (BoardHard) gameModel.getBoard();
    }

    @Override
    public void useEffect(Message message) {
        SpecialCardwithStudents character = (SpecialCardwithStudents)board.getSpecialCardbyName("QUEEN");
        Student s = character.removeStudent(CharacterColor.valueOf(message.getFirstParameter()));
        board.getSchoolByOwner(gameModel.getCurrentPlayer().getNickname()).addClassroomStudent(s);
        character.addStudent(board.removeRandomStudent());
    }
}