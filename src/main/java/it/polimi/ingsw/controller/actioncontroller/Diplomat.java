package it.polimi.ingsw.controller.actioncontroller;

import it.polimi.ingsw.server.ConnectionMessage.ActionMessage;
import it.polimi.ingsw.server.model.GameModel;
import it.polimi.ingsw.server.model.BoardExpert;

public class Diplomat implements CharacterCardStrategy {

    GameModel gameModel;
    BoardExpert board;

    public Diplomat(GameModel gameModel) {
        this.gameModel = gameModel;
        board = (BoardExpert) gameModel.getBoard();
    }

    @Override
    public void useEffect(ActionMessage actionMessage) {
        int newOwner = board.getTotalInfluence(actionMessage.getData());
        int oldOwner=-1;
        if (!gameModel.getBoard().getIslands().get(actionMessage.getData()).getTowers().isEmpty())
            oldOwner = gameModel.getBoard().getIslands().get(actionMessage.getData()).getTowers().get(0).getOwner();
        //se nessuno controlla isola non faccio cambiamenti
        if (newOwner != -1) {
            //se l'isola non contiene torri sposto le torri dalla scuola all'isola
            if (gameModel.getBoard().getIslands().get(actionMessage.getData()).getTowers().isEmpty()) {
                gameModel.getBoard().moveTower(newOwner, actionMessage.getData(), "island");
            }
            //altrimenti tolgo le tower presenti e metto quelle del nuovo owner
            else if (newOwner != oldOwner) {
                gameModel.getBoard().moveTower(oldOwner, actionMessage.getData(), "school");
                gameModel.getBoard().moveTower(newOwner, actionMessage.getData(), "island");
            }
            gameModel.getBoard().checkNearIsland(actionMessage.getData());
            if (gameModel.getBoard().getIslands().size() == 3) {
                //TODO controllare se c'è un vincitore
            }
        }
    }
}
