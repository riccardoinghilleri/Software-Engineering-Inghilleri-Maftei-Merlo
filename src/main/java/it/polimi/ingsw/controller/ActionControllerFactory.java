package it.polimi.ingsw.controller;

import it.polimi.ingsw.controller.actioncontroller.ActionController;
import it.polimi.ingsw.model.GameModel;

public class ActionControllerFactory {

    public static ActionController factory(String specialCardName, GameModel gameModel, String player) {
        return new ActionController(gameModel, player); //TODO da fare, return solo per evitare errori
    }
}
