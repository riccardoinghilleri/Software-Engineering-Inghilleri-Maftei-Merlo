package it.polimi.ingsw.controller.actioncontroller;

import it.polimi.ingsw.controller.Message;
import it.polimi.ingsw.model.GameModel;
import it.polimi.ingsw.model.SpecialCardwithStudents;
import it.polimi.ingsw.model.Student;
import it.polimi.ingsw.model.board.BoardHard;
import it.polimi.ingsw.model.enums.CharacterColor;

public class Clown implements CharacterCardStrategy{

    GameModel gameModel;
    BoardHard board;
    public Clown(GameModel gameModel) {
        this.gameModel=gameModel;
        board=(BoardHard) gameModel.getBoard();
    }

    @Override
    public void useEffect(Message message) {
        String player = gameModel.getCurrentPlayer().getNickname();
        Student s1 = board.getSchoolByOwner(player).removeHallStudent(CharacterColor.valueOf(message.getFirstParameter()));
        Student s2 = ((SpecialCardwithStudents)board.getSpecialCardbyName("PEFORMER")).removeStudent(CharacterColor.valueOf(message.getSecondParameter()));
        board.getSchoolByOwner(player).getHall().add(s2);
        ((SpecialCardwithStudents)board.getSpecialCardbyName("PEFORMER")).addStudent(s1);

    }
}

