package it.polimi.ingsw.controller.actioncontroller;

import it.polimi.ingsw.controller.Message;
import it.polimi.ingsw.model.GameModel;
import it.polimi.ingsw.model.BoardExpert;

public class Diplomat implements CharacterCardStrategy{

    GameModel gameModel;
    BoardExpert board;
    public Diplomat(GameModel gameModel) {
        this.gameModel=gameModel;
        board=(BoardExpert) gameModel.getBoard();
    }

    @Override
    public void useEffect(Message message) {
        String newOwner = board.getTotalInfluence(message.getData());
        String oldOwner = gameModel.getBoard().getIslands().get(message.getData()).getTowers().get(0).getOwner();
        //se nessuno controlla isola non faccio cambiamenti
        if (!newOwner.equalsIgnoreCase("NONE")) {
            //se l'isola non contiene torri sposto le torri dalla scuola all'isola
            if (gameModel.getBoard().getIslands().get(message.getData()).getTowers().isEmpty()) {
                gameModel.getBoard().moveTower(gameModel.getPlayerByNickname(newOwner).getNickname(),message.getData());
            }
            //altrimenti tolgo le tower presenti e metto quelle del nuovo owner
            else if(newOwner.equalsIgnoreCase(oldOwner)){
                gameModel.getBoard().moveTower(message.getData(), oldOwner);
                gameModel.getBoard().moveTower(newOwner,message.getData());
            }
            gameModel.getBoard().checkNearIsland(message.getData());
        }


    }
}
