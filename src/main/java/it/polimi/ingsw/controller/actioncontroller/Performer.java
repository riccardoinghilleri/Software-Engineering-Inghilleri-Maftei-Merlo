package it.polimi.ingsw.controller.actioncontroller;

import it.polimi.ingsw.controller.Message;
import it.polimi.ingsw.model.GameModel;
import it.polimi.ingsw.model.SpecialCardwithProhibitions;
import it.polimi.ingsw.model.SpecialCardwithStudents;
import it.polimi.ingsw.model.Student;
import it.polimi.ingsw.model.board.BoardHard;
import it.polimi.ingsw.model.enums.CharacterColor;

public class Performer implements CharacterCardStrategy {

    GameModel gameModel;
    BoardHard board;

    public Performer(GameModel gameModel) {
        this.gameModel = gameModel;
        board = (BoardHard) gameModel.getBoard();
    }


    @Override
    public void useEffect(Message message) {
        Student s1= board.getSchoolByOwner(gameModel.getCurrentPlayer().getNickname()).removeHallStudent(CharacterColor.valueOf(message.getFirstParameter()));
        Student s2= board.getSchoolByOwner(gameModel.getCurrentPlayer().getNickname()).removeClassroomStudent(CharacterColor.valueOf(message.getSecondParameter()));
        board.getSchoolByOwner(gameModel.getCurrentPlayer().getNickname()).addHallStudent(s2);
        board.getSchoolByOwner(gameModel.getCurrentPlayer().getNickname()).addClassroomStudent(s1);
    }
}