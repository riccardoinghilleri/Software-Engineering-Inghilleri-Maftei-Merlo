package it.polimi.ingsw.controller.actioncontroller;

import it.polimi.ingsw.server.ConnectionMessage.ActionMessage;
import it.polimi.ingsw.server.model.GameModel;
import it.polimi.ingsw.server.model.Player;
import it.polimi.ingsw.enums.CharacterColor;

import java.util.*;
/**
 * This class represents the Lumberjack card and contains the specific method of the card.
 * It has a parameter string color, since the player while using this card can require the influence without involving this color.
 */
public class Lumberjack extends ActionController {

    private CharacterColor color;

    /**
     * The constructor creates a Lumberjack instance
     */
    public Lumberjack(GameModel gameModel) {
        super(gameModel);
    }

    public void setColor(String color) {
        this.color = CharacterColor.valueOf(color);
    }

    /**
     * This method implements the 'super-power' of the card, overriding the method getInfluence from action controller.
     * Go to "model->boardExpert" to see the description
     * @param actionMessage message with all the necessary parameter
     * @return the player with the highest influence.
     */
    @Override
    public int getInfluence(int index) {
        List<CharacterColor> colors = new ArrayList<>(Arrays.asList(CharacterColor.values()));
        colors.remove(color.ordinal());
        int[] influence = new int[getGameModel().getPlayers().size()];
        for(Player player : getGameModel().getPlayers()) {
            influence[player.getClientID()]=0;
        }
        influence = getGameModel().getBoard().getStudentInfluence(index,influence, colors);
        influence = getGameModel().getBoard().getTowersInfluence(index,influence);

        return getGameModel().getBoard().getMaxInfluence(influence);
    }
}
