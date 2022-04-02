package it.polimi.ingsw.controller;

import it.polimi.ingsw.controller.actioncontroller.ActionController;
import it.polimi.ingsw.model.GameModel;

public class ActionControllerFactory {

    public static ActionController factory(String specialCardName, GameModel gameModel) {
        return new ActionController(gameModel); //TODO da fare, return solo per evitare errori
    }
}
