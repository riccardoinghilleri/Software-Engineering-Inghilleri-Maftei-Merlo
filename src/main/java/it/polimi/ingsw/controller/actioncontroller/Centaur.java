package it.polimi.ingsw.controller.actioncontroller;

import it.polimi.ingsw.server.ConnectionMessage.ActionMessage;
import it.polimi.ingsw.server.model.GameModel;
import it.polimi.ingsw.server.model.Player;
import it.polimi.ingsw.server.model.enums.CharacterColor;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class Centaur extends ActionController {

    public Centaur(GameModel gameModel) {
        super(gameModel);
    }

    //metodo che calcola l'inlfuenza senza tenere conto delle torri
    @Override
    public String getInfluence(ActionMessage actionMessage) { //da aggiungere un getModel in actionController
        Map<String,Integer> owners = new HashMap<>();
        for(Player player : getGameModel().getPlayers()) {
            owners.put(player.getNickname(),0);
        }
        owners = getGameModel().getBoard().getStudentInfluence(actionMessage.getData(),owners, Arrays.asList(CharacterColor.values()));
        return getGameModel().getBoard().getMaxInfluence(owners);
    }
}
