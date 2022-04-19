package it.polimi.ingsw.controller.actioncontroller;

import it.polimi.ingsw.server.ConnectionMessage.ActionMessage;
import it.polimi.ingsw.server.model.GameModel;
import it.polimi.ingsw.server.model.Player;
import it.polimi.ingsw.server.model.enums.CharacterColor;

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
    public String getInfluence(ActionMessage actionMessage) {
        Map<String,Integer> owners = new HashMap<>();
        for(Player player : getGameModel().getPlayers()) {
            owners.put(player.getNickname(),0);
        }
        owners = getGameModel().getBoard().getStudentInfluence(actionMessage.getData(),owners, Arrays.asList(CharacterColor.values()));
        owners = getGameModel().getBoard().getTowersInfluence(actionMessage.getData(),owners);
        owners.replace(getGameModel().getCurrentPlayer().getNickname(),owners.get(getGameModel().getCurrentPlayer().getNickname())+2);
        return getGameModel().getBoard().getMaxInfluence(owners);
    }
}
