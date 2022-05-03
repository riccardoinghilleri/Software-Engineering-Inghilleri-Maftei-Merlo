package it.polimi.ingsw.controller.actioncontroller;

import it.polimi.ingsw.server.ConnectionMessage.ActionMessage;
import it.polimi.ingsw.server.model.GameModel;
import it.polimi.ingsw.server.model.CharacterCardwithStudents;
import it.polimi.ingsw.server.model.Student;
import it.polimi.ingsw.server.model.BoardExpert;
import it.polimi.ingsw.enums.CharacterColor;

public class Priest implements CharacterCardStrategy {

    GameModel gameModel;
    BoardExpert board;
    public Priest(GameModel gameModel) {
        this.gameModel=gameModel;
        board=(BoardExpert) gameModel.getBoard();
    }

    @Override
    public void useEffect(ActionMessage actionMessage) {
        CharacterCardwithStudents character = (CharacterCardwithStudents)board.getCharacterCardbyName("PRIEST");
        Student s = character.removeStudent(CharacterColor.valueOf(actionMessage.getParameters().get(0)));
        board.getIslands().get(actionMessage.getData()).addStudent(s);
        character.addStudent(board.removeRandomStudent());
    }
}