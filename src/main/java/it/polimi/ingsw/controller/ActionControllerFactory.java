package it.polimi.ingsw.controller;

import it.polimi.ingsw.controller.actioncontroller.*;
import it.polimi.ingsw.model.GameModel;
import it.polimi.ingsw.model.SpecialCard;
import it.polimi.ingsw.model.SpecialCardwithProhibitions;
import it.polimi.ingsw.model.SpecialCardwithStudents;
import it.polimi.ingsw.model.enums.SpecialCardName;

public class ActionControllerFactory {

    public static ActionController factory(String specialCardName, GameModel gameModel, String player) {
        ActionController actionController;
        switch (specialCardName){
            case "Centaur" :
                actionController= new Centaur(gameModel, player);
                break;
            case "Clown" :
                actionController= new Clown(gameModel, player);
                break;
            case "Diner":
                actionController= new Diner(gameModel,player);
                break;
            case "Herbolaria":
                actionController= new Herbolaria(gameModel, player);
                break;
            case "Knight":
                actionController= new Knight(gameModel, player);
                break;
            case "Lumberjack":
                actionController= new Lumberjack(gameModel, player);
                break;
            case "Perfomer":
                actionController= new Performer(gameModel, player);
                break;
            case "Priest":
                actionController= new Priest(gameModel, player);
                break;
            case "Queen":
                actionController= new Queen(gameModel, player);
                break;
            case "Thief":
                actionController= new Thief(gameModel, player);

        }
        return  actionController;
    }
}
