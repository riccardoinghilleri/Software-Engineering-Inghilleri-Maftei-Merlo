package it.polimi.ingsw.controller.actioncontroller;


import it.polimi.ingsw.server.model.GameModel;
import it.polimi.ingsw.server.model.Player;
import it.polimi.ingsw.enums.CharacterColor;
import java.util.Arrays;

/**
 * This class represents the Centaur card and contains the specific method of the card.
 */
public class Centaur extends ActionController {
    /**
     * The constructor creates a Centaur instance
     * @param gameModel of type GameHandler - GameHandler reference.
     */
    public Centaur(GameModel gameModel) {
        super(gameModel);
    }

    /**
     * This method is an override of the general method getInfluence.
     * It, in fact, gets the influence without counting the towers on the island.
     * Go to "model->boardExpert" to see the complete description
     * @param index id of the island.
     * @return the player with the highest influence.
     */
    //metodo che calcola l'influenza senza tenere conto delle torri
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
