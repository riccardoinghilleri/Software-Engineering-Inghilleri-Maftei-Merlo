package it.polimi.ingsw.controller.actioncontroller;

import it.polimi.ingsw.controller.Message;
import it.polimi.ingsw.model.GameModel;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.SpecialCardwithStudents;
import it.polimi.ingsw.model.Student;
import it.polimi.ingsw.model.board.BoardHard;
import it.polimi.ingsw.model.enums.CharacterColor;

public class Thief implements CharacterCardStrategy{

    GameModel gameModel;
    BoardHard board;

    public Thief(GameModel gameModel) {
        this.gameModel = gameModel;
        board = (BoardHard) gameModel.getBoard();
    }
    @Override
    public void useEffect(Message message) {
        CharacterColor color=CharacterColor.valueOf(message.getFirstParameter());
        for(Player p: gameModel.getPlayers()){
            for(int i=0;i<3;i++){
                if(board.getSchoolByOwner(p.getNickname()).getClassroom().get(color).size()>0)
                    board.getSchoolByOwner(p.getNickname()).removeClassroomStudent(color);
            }

        }
    }
}



