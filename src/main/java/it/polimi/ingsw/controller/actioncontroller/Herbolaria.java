package it.polimi.ingsw.controller.actioncontroller;

import it.polimi.ingsw.server.ConnectionMessage.ActionMessage;
import it.polimi.ingsw.server.model.GameModel;
import it.polimi.ingsw.server.model.CharacterCardwithProhibitions;
import it.polimi.ingsw.server.model.BoardExpert;

public class Herbolaria implements CharacterCardStrategy {

    GameModel gameModel;
    BoardExpert board;
    public Herbolaria(GameModel gameModel) {
        this.gameModel=gameModel;
        board=(BoardExpert) gameModel.getBoard();
    }

    @Override
    public void useEffect(ActionMessage actionMessage) {
        ((CharacterCardwithProhibitions)board.getCharacterCardbyName("HERBOLARIA")).subProhibitionCard();
        board.getIslands().get(actionMessage.getData()).setNoEntryTile(true);

    }
}
