package it.polimi.ingsw.controller.actioncontroller;

import it.polimi.ingsw.server.ConnectionMessage.ActionMessage;
import it.polimi.ingsw.server.model.GameModel;
import it.polimi.ingsw.server.model.Player;
import it.polimi.ingsw.server.model.BoardExpert;
import it.polimi.ingsw.enums.CharacterColor;
/**
 * This class represents the Thief card and contains the specific method of the card.
 */
public class Thief implements CharacterCardStrategy{

    GameModel gameModel;
    BoardExpert board;
    /**
     * The constructor creates a Thief instance
     */
    public Thief(GameModel gameModel) {
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
        CharacterColor color=CharacterColor.valueOf(actionMessage.getParameters().get(0));
        for(Player p: gameModel.getPlayers()){
            for(int i=0;i<3;i++){
                if(board.getSchoolByOwnerId(p.getClientID()).getDiningRoom().get(color).size()>0)
                    board.getSchoolByOwnerId(p.getClientID()).removeDiningRoomStudent(color);
            }

        }
    }
}



