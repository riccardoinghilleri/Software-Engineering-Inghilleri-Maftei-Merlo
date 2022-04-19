package it.polimi.ingsw.controller.actioncontroller;

import it.polimi.ingsw.server.ConnectionMessage.ActionMessage;
import it.polimi.ingsw.server.model.GameModel;
import it.polimi.ingsw.server.model.Player;
import it.polimi.ingsw.server.model.enums.CharacterColor;

import java.util.*;

public class Lumberjack extends ActionController {

    String color;
    public Lumberjack(GameModel gameModel, String color) {
        super(gameModel);
        this.color=color;
    }

    @Override
    public String getInfluence(ActionMessage actionMessage) {
        List<CharacterColor> colors = Arrays.asList(CharacterColor.values());
        colors.remove(CharacterColor.valueOf(this.color));
        Map<String,Integer> owners = new HashMap<>();
        for(Player player : getGameModel().getPlayers()) {
            owners.put(player.getNickname(),0);
        }
        owners = getGameModel().getBoard().getStudentInfluence(actionMessage.getData(),owners, colors);
        owners = getGameModel().getBoard().getTowersInfluence(actionMessage.getData(),owners);

        return getGameModel().getBoard().getMaxInfluence(owners);
    }
}
