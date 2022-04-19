package it.polimi.ingsw.controller.actioncontroller;

import it.polimi.ingsw.server.ConnectionMessage.ActionMessage;
import it.polimi.ingsw.server.model.GameModel;
import it.polimi.ingsw.server.model.CharacterCardwithStudents;
import it.polimi.ingsw.server.model.Student;
import it.polimi.ingsw.server.model.BoardExpert;
import it.polimi.ingsw.server.model.enums.CharacterColor;

public class Clown implements CharacterCardStrategy{

    GameModel gameModel;
    BoardExpert board;
    public Clown(GameModel gameModel) {
        this.gameModel=gameModel;
        board=(BoardExpert) gameModel.getBoard();
    }

    @Override
    public void useEffect(ActionMessage actionMessage) {
        String player = gameModel.getCurrentPlayer().getNickname();
        Student s1 = board.getSchoolByOwner(player).removeEntranceStudent(CharacterColor.valueOf(actionMessage.getFirstParameter()));
        Student s2 = ((CharacterCardwithStudents)board.getCharacterCardbyName("PEFORMER")).removeStudent(CharacterColor.valueOf(actionMessage.getSecondParameter()));
        board.getSchoolByOwner(player).getEntrance().add(s2);
        ((CharacterCardwithStudents)board.getCharacterCardbyName("PEFORMER")).addStudent(s1);

    }
}

