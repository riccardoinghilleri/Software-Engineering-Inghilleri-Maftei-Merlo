package it.polimi.ingsw.controller.actioncontroller;

import it.polimi.ingsw.controller.Message;
import it.polimi.ingsw.model.GameModel;
import it.polimi.ingsw.model.SpecialCardwithProhibitions;
import it.polimi.ingsw.model.board.BoardHard;

public class Herbolaria implements CharacterCardStrategy {

    GameModel gameModel;
    BoardHard board;
    public Herbolaria(GameModel gameModel) {
        this.gameModel=gameModel;
        board=(BoardHard) gameModel.getBoard();
    }

    @Override
    public void useEffect(Message message) {
        ((SpecialCardwithProhibitions)board.getSpecialCardbyName("HERBOLARIA")).subProhibitionCard();
        board.getIslands().get(message.getData()).setLock(true);

    }
}
