package it.polimi.ingsw.controller.actioncontroller;

import it.polimi.ingsw.controller.Message;
import it.polimi.ingsw.model.GameModel;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.Student;
import it.polimi.ingsw.model.enums.CharacterColor;

import java.util.*;

public class Lumberjack extends ActionController {

    String color;
    public Lumberjack(GameModel gameModel, String color) {
        super(gameModel);
        this.color=color;
    }

    @Override
    public String getInfluence(Message message) {
        List<CharacterColor> colors = Arrays.asList(CharacterColor.values());
        colors.remove(CharacterColor.valueOf(this.color));
        Map<String,Integer> owners = new HashMap<>();
        for(Player player : getGameModel().getPlayers()) {
            owners.put(player.getNickname(),0);
        }
        owners = getGameModel().getBoard().getStudentInfluence(message.getData(),owners, colors);
        owners = getGameModel().getBoard().getTowersInfluence(message.getData(),owners);

        return getGameModel().getBoard().getMaxInfluence(owners);
    }
}
