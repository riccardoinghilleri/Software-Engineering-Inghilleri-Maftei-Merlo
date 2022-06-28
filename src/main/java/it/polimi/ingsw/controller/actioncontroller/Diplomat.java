package it.polimi.ingsw.controller.actioncontroller;

import it.polimi.ingsw.server.ConnectionMessage.ActionMessage;
import it.polimi.ingsw.server.model.GameModel;
import it.polimi.ingsw.server.model.BoardExpert;

/**
 * This class represents the Diplomat card and contains the specific method of the card.
 */
public class Diplomat implements CharacterCardStrategy {

    GameModel gameModel;
    BoardExpert board;
    /**
     * The constructor creates a Diplomat instance
     * @param gameModel of type GameHandler - GameHandler reference.
     */
    public Diplomat(GameModel gameModel) {
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
        int newOwner = board.getTotalInfluence(actionMessage.getData());
        int oldOwner=-1;
        if (!gameModel.getBoard().getIslands().get(actionMessage.getData()).getTowers().isEmpty())
            oldOwner = gameModel.getBoard().getIslands().get(actionMessage.getData()).getTowers().get(0).getOwner();
        //se nessuno controlla isola non faccio cambiamenti
        if (newOwner != -1) {
            //se l'isola non contiene torri sposto le torri dalla scuola all'isola
            if (oldOwner==-1) {
                gameModel.getBoard().moveTower(newOwner, actionMessage.getData(), "island");
            }
            //altrimenti tolgo le tower presenti e metto quelle del nuovo owner
            else if (newOwner != oldOwner) {
                int towers_number = gameModel.getBoard().getIslands().get(actionMessage.getData()).getTowers().size();
                int available_towers = gameModel.getBoard().getSchoolByOwnerId(newOwner).getTowersNumber();
                gameModel.getBoard().moveTower(oldOwner, actionMessage.getData(), "school");
                for (int i = 0; i < towers_number && i < available_towers; i++)
                    gameModel.getBoard().moveTower(newOwner, actionMessage.getData(), "island");
            }
            gameModel.getBoard().checkNearIsland(actionMessage.getData(),true);
        }
    }
}
