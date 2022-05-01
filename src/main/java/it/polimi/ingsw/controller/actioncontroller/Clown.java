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
        for(int i=0; i<actionMessage.getParameters().size(); i++) {
            Student s1 = ((CharacterCardwithStudents) board.getCharacterCardbyName("PEFORMER")).removeStudent(CharacterColor.valueOf(actionMessage.getParameters().get(i)));
            i++;
            Student s2 = board.getSchoolByOwner(player).removeEntranceStudent(CharacterColor.valueOf(actionMessage.getParameters().get(i)));
            ((CharacterCardwithStudents) board.getCharacterCardbyName("PEFORMER")).addStudent(s2);
            board.getSchoolByOwner(player).getEntrance().add(s1);
        }
    }
}

