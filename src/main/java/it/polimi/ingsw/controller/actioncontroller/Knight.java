package it.polimi.ingsw.controller.actioncontroller;

import it.polimi.ingsw.model.GameModel;

public class Knight extends ActionController {

    public Knight(GameModel gameModel, String player) {
        super(gameModel, player);
        //TODO da finire
    }

    @Override
    public String getInfluence(Message influenceMessage) {
        return "a"; //TODO da fare
    }
}
