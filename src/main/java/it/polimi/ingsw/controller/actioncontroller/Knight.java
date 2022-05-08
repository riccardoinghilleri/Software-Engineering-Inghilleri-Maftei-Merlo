package it.polimi.ingsw.controller.actioncontroller;

import it.polimi.ingsw.server.ConnectionMessage.ActionMessage;
import it.polimi.ingsw.server.model.GameModel;
import it.polimi.ingsw.server.model.Player;
import it.polimi.ingsw.enums.CharacterColor;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class Knight extends ActionController {

    public Knight(GameModel gameModel) {
        super(gameModel);
        //TODO da finire
    }
    //metodo che calcola l'influenza aggiungenddo due punti addizionali al currentPlayer
    @Override
    public int getInfluence(ActionMessage actionMessage) {
        int[] influence = new int[getGameModel().getPlayers().size()];
        for(Player player : getGameModel().getPlayers()) {
            influence[player.getClientID()]=0;
        }
        influence = getGameModel().getBoard().getStudentInfluence(actionMessage.getData(),influence, Arrays.asList(CharacterColor.values()));
        influence = getGameModel().getBoard().getTowersInfluence(actionMessage.getData(),influence);
        influence[getGameModel().getCurrentPlayer().getClientID()]+=2;
        return getGameModel().getBoard().getMaxInfluence(influence);
    }
}
