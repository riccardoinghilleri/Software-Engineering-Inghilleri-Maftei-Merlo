package it.polimi.ingsw.controller.actioncontroller;

import it.polimi.ingsw.server.ConnectionMessage.ActionMessage;
import it.polimi.ingsw.server.model.GameModel;
import it.polimi.ingsw.server.model.CharacterCardwithProhibitions;
import it.polimi.ingsw.server.model.BoardExpert;
/**
 * This class represents the Herbolaria card and contains the specific method of the card.
 */
public class Herbolaria implements CharacterCardStrategy {

    GameModel gameModel;
    BoardExpert board;
    /**
     * The constructor creates a Herbolaria instance
     */
    public Herbolaria(GameModel gameModel) {
        this.gameModel=gameModel;
        board=(BoardExpert) gameModel.getBoard();
    }
    /**
     * This method implements the 'super-power' of the card.
     * It is an override of the useEffect method of characterCardStrategy, using the specific parameters given throw the actionMessage.
     * Go to "model->boardExpert" to see the description
     * @param actionMessage message with all the necessary parameters for this type of card
     */
    @Override
    public void useEffect(ActionMessage actionMessage) {
        ((CharacterCardwithProhibitions)board.getCharacterCardbyName("HERBOLARIA")).subProhibitionCard();
        board.getIslands().get(actionMessage.getData()).setNoEntryTile(true);

    }
}
