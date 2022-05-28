package it.polimi.ingsw.controller.actioncontroller;

import it.polimi.ingsw.server.ConnectionMessage.ActionMessage;
import it.polimi.ingsw.server.model.GameModel;
import it.polimi.ingsw.server.model.Player;
import it.polimi.ingsw.enums.CharacterColor;

import java.util.*;

public class Lumberjack extends ActionController {

    CharacterColor color;
    public Lumberjack(GameModel gameModel) {
        super(gameModel);
    }

    public void setColor(String color) {
        this.color = CharacterColor.valueOf(color);
    }

    @Override
    public int getInfluence(ActionMessage actionMessage) {
        List<CharacterColor> colors = new ArrayList<>(Arrays.asList(CharacterColor.values()));
        colors.remove(color.ordinal());
        int[] influence = new int[getGameModel().getPlayers().size()];
        for(Player player : getGameModel().getPlayers()) {
            influence[player.getClientID()]=0;
        }
        influence = getGameModel().getBoard().getStudentInfluence(actionMessage.getData(),influence, colors);
        influence = getGameModel().getBoard().getTowersInfluence(actionMessage.getData(),influence);

        return getGameModel().getBoard().getMaxInfluence(influence);
    }
}
