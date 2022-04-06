package it.polimi.ingsw.controller.actioncontroller;

import it.polimi.ingsw.controller.Message;
import it.polimi.ingsw.model.GameModel;
import it.polimi.ingsw.model.CharacterCardwithProhibitions;
import it.polimi.ingsw.model.BoardExpert;

public class Herbolaria implements CharacterCardStrategy {

    GameModel gameModel;
    BoardExpert board;
    public Herbolaria(GameModel gameModel) {
        this.gameModel=gameModel;
        board=(BoardExpert) gameModel.getBoard();
    }

    @Override
    public void useEffect(Message message) {
        ((CharacterCardwithProhibitions)board.getCharacterCardbyName("HERBOLARIA")).subProhibitionCard();
        board.getIslands().get(message.getData()).setNoEntryTile(true);

    }
}
