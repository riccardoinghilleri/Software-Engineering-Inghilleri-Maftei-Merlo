package it.polimi.ingsw.controller.actioncontroller;

import it.polimi.ingsw.controller.Message;
import it.polimi.ingsw.model.GameModel;
import it.polimi.ingsw.model.CharacterCardwithStudents;
import it.polimi.ingsw.model.Student;
import it.polimi.ingsw.model.BoardExpert;
import it.polimi.ingsw.model.enums.CharacterColor;

public class Priest implements CharacterCardStrategy {

    GameModel gameModel;
    BoardExpert board;
    public Priest(GameModel gameModel) {
        this.gameModel=gameModel;
        board=(BoardExpert) gameModel.getBoard();
    }

    @Override
    public void useEffect(Message message) {
        CharacterCardwithStudents character = (CharacterCardwithStudents)board.getCharacterCardbyName("PRIEST");
        Student s = character.removeStudent(CharacterColor.valueOf(message.getFirstParameter()));
        board.getIslands().get(message.getData()).addStudent(s);
        character.addStudent(board.removeRandomStudent());
    }
}