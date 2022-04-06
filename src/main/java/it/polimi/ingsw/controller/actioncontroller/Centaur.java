package it.polimi.ingsw.controller.actioncontroller;

import it.polimi.ingsw.controller.Message;
import model.GameModel;
import model.Player;
import model.enums.CharacterColor;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class Centaur extends ActionController {

    public Centaur(GameModel gameModel) {
        super(gameModel);
    }

    //metodo che calcola l'inlfuenza senza tenere conto delle torri
    @Override
    public String getInfluence(Message message) { //da aggiungere un getModel in actionController
        Map<String,Integer> owners = new HashMap<>();
        for(Player player : getGameModel().getPlayers()) {
            owners.put(player.getNickname(),0);
        }
        owners = getGameModel().getBoard().getStudentInfluence(message.getData(),owners, Arrays.asList(CharacterColor.values()));
        return getGameModel().getBoard().getMaxInfluence(owners);
    }
}
