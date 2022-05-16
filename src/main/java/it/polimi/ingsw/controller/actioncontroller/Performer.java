package it.polimi.ingsw.controller.actioncontroller;

import it.polimi.ingsw.server.ConnectionMessage.ActionMessage;
import it.polimi.ingsw.server.model.GameModel;
import it.polimi.ingsw.server.model.Student;
import it.polimi.ingsw.server.model.BoardExpert;
import it.polimi.ingsw.enums.CharacterColor;
/**
 * This class represents the Performer card and contains the specific method of the card.
 */
public class Performer implements CharacterCardStrategy {

    GameModel gameModel;
    BoardExpert board;
    /**
     * The constructor creates a Performer instance
     */
    public Performer(GameModel gameModel) {
        this.gameModel = gameModel;
        board = (BoardExpert) gameModel.getBoard();
    }
    /**
     * This method implements the 'super-power' of the card.
     * It is an override of the useEffect method of characterCardStrategy, using the specific parameters given throw the actionMessage.
     * Go to "model->boardExpert" to see the description
     * @param actionMessage message with all the necessary parameters for this type of card
     */

    @Override
    public void useEffect(ActionMessage actionMessage) {
        Student s1 = board.getSchoolByOwnerId(gameModel.getCurrentPlayer().getClientID()).removeDiningRoomStudent(CharacterColor.valueOf(actionMessage.getParameters().get(0)));
        Student s2 = board.getSchoolByOwnerId(gameModel.getCurrentPlayer().getClientID()).removeEntranceStudent(CharacterColor.valueOf(actionMessage.getParameters().get(1)));
        board.getSchoolByOwnerId(gameModel.getCurrentPlayer().getClientID()).addDiningRoomStudent(s2);
        board.getSchoolByOwnerId(gameModel.getCurrentPlayer().getClientID()).addEntranceStudent(s1);

    }
}