package it.polimi.ingsw.controller.actioncontroller;

import it.polimi.ingsw.controller.Message;
import it.polimi.ingsw.model.GameModel;
import it.polimi.ingsw.model.SpecialCardwithStudents;
import it.polimi.ingsw.model.Student;
import it.polimi.ingsw.model.board.BoardHard;
import it.polimi.ingsw.model.enums.CharacterColor;

public class Priest implements CharacterCardStrategy {

    GameModel gameModel;
    BoardHard board;
    public Priest(GameModel gameModel) {
        this.gameModel=gameModel;
        board=(BoardHard) gameModel.getBoard();
    }

    @Override
    public void useEffect(Message message) {
        SpecialCardwithStudents character = (SpecialCardwithStudents)board.getSpecialCardbyName("PRIEST");
        Student s = character.removeStudent(CharacterColor.valueOf(message.getFirstParameter()));
        board.getIslands().get(message.getData()).addStudent(s);
        character.addStudent(board.removeRandomStudent());
    }
}