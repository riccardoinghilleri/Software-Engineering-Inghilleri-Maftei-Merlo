package it.polimi.ingsw.controller.actioncontroller;

import it.polimi.ingsw.server.ConnectionMessage.ActionMessage;
import it.polimi.ingsw.server.model.GameModel;
import it.polimi.ingsw.server.model.Student;
import it.polimi.ingsw.server.model.BoardExpert;
import it.polimi.ingsw.enums.CharacterColor;

public class Performer implements CharacterCardStrategy {

    GameModel gameModel;
    BoardExpert board;

    public Performer(GameModel gameModel) {
        this.gameModel = gameModel;
        board = (BoardExpert) gameModel.getBoard();
    }


    @Override
    public void useEffect(ActionMessage actionMessage) {
        Student s1 = board.getSchoolByOwnerId(gameModel.getCurrentPlayer().getClientID()).removeEntranceStudent(CharacterColor.valueOf(actionMessage.getParameters().get(0)));
        Student s2 = board.getSchoolByOwnerId(gameModel.getCurrentPlayer().getClientID()).removeDiningRoomStudent(CharacterColor.valueOf(actionMessage.getParameters().get(1)));
        board.getSchoolByOwnerId(gameModel.getCurrentPlayer().getClientID()).addDiningRoomStudent(s1);
        board.getSchoolByOwnerId(gameModel.getCurrentPlayer().getClientID()).addEntranceStudent(s2);

    }
}