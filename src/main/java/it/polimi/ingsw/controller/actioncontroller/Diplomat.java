package it.polimi.ingsw.controller.actioncontroller;

import it.polimi.ingsw.server.ConnectionMessage.ActionMessage;
import it.polimi.ingsw.server.model.GameModel;
import it.polimi.ingsw.server.model.BoardExpert;

public class Diplomat implements CharacterCardStrategy{

    GameModel gameModel;
    BoardExpert board;
    public Diplomat(GameModel gameModel) {
        this.gameModel=gameModel;
        board=(BoardExpert) gameModel.getBoard();
    }

    @Override
    public void useEffect(ActionMessage actionMessage) {
        String newOwner = board.getTotalInfluence(actionMessage.getData());
        String oldOwner = gameModel.getBoard().getIslands().get(actionMessage.getData()).getTowers().get(0).getOwner();
        //se nessuno controlla isola non faccio cambiamenti
        if (!newOwner.equalsIgnoreCase("NONE")) {
            //se l'isola non contiene torri sposto le torri dalla scuola all'isola
            if (gameModel.getBoard().getIslands().get(actionMessage.getData()).getTowers().isEmpty()) {
                gameModel.getBoard().moveTower(gameModel.getPlayerByNickname(newOwner).getNickname(), actionMessage.getData());
            }
            //altrimenti tolgo le tower presenti e metto quelle del nuovo owner
            else if(newOwner.equalsIgnoreCase(oldOwner)){
                gameModel.getBoard().moveTower(actionMessage.getData(), oldOwner);
                gameModel.getBoard().moveTower(newOwner, actionMessage.getData());
            }
            gameModel.getBoard().checkNearIsland(actionMessage.getData());
        }


    }
}
