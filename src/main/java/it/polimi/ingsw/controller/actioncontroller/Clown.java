package it.polimi.ingsw.controller.actioncontroller;

import it.polimi.ingsw.controller.Message;
import model.GameModel;
import model.CharacterCardwithStudents;
import model.Student;
import model.board.BoardExpert;
import model.enums.CharacterColor;

public class Clown implements CharacterCardStrategy{

    GameModel gameModel;
    BoardExpert board;
    public Clown(GameModel gameModel) {
        this.gameModel=gameModel;
        board=(BoardExpert) gameModel.getBoard();
    }

    @Override
    public void useEffect(Message message) {
        String player = gameModel.getCurrentPlayer().getNickname();
        Student s1 = board.getSchoolByOwner(player).removeEntranceStudent(CharacterColor.valueOf(message.getFirstParameter()));
        Student s2 = ((CharacterCardwithStudents)board.getCharacterCardbyName("PEFORMER")).removeStudent(CharacterColor.valueOf(message.getSecondParameter()));
        board.getSchoolByOwner(player).getEntrance().add(s2);
        ((CharacterCardwithStudents)board.getCharacterCardbyName("PEFORMER")).addStudent(s1);

    }
}

