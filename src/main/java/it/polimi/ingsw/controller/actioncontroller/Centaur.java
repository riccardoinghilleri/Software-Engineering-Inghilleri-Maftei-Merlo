package it.polimi.ingsw.controller.actioncontroller;

import it.polimi.ingsw.server.ConnectionMessage.ActionMessage;
import it.polimi.ingsw.server.model.GameModel;
import it.polimi.ingsw.server.model.Player;
import it.polimi.ingsw.enums.CharacterColor;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class Centaur extends ActionController {

    public Centaur(GameModel gameModel) {
        super(gameModel);
    }

    //metodo che calcola l'inlfuenza senza tenere conto delle torri
    @Override
    public int getInfluence(int index) { //da aggiungere un getModel in actionController
        int[] influence = new int[getGameModel().getPlayers().size()];
        for(Player player : getGameModel().getPlayers()) {
            influence[player.getClientID()]=0;
        }
        influence = getGameModel().getBoard().getStudentInfluence(index,influence, Arrays.asList(CharacterColor.values()));
        return getGameModel().getBoard().getMaxInfluence(influence);
    }
}
