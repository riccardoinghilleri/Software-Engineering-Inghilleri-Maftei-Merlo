package it.polimi.ingsw.controller.actioncontroller;

import it.polimi.ingsw.controller.Message;
import it.polimi.ingsw.model.GameModel;
import it.polimi.ingsw.model.CharacterCardwithStudents;
import it.polimi.ingsw.model.Student;
import it.polimi.ingsw.model.BoardExpert;
import it.polimi.ingsw.model.enums.CharacterColor;

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

