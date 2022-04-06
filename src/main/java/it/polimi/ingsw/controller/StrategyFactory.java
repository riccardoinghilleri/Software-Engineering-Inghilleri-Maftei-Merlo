package it.polimi.ingsw.controller;

import it.polimi.ingsw.controller.actioncontroller.CharacterCardStrategy;
import it.polimi.ingsw.controller.actioncontroller.*;
import model.GameModel;

public class StrategyFactory {

    public static CharacterCardStrategy strategyFactory(String characterCardName, GameModel gameModel) {
        CharacterCardStrategy strategy = null; //TODO non so se è giusto inizializzarla a null
        switch (characterCardName){
            case "CLOWN" :
                strategy= new Clown(gameModel);
                break;
            case "DIPLOMAT":
                strategy = new Diplomat(gameModel);
                break;
            case "HERBOLARIA":
                strategy= new Herbolaria(gameModel);
                break;
            case "PERFORMER":
                strategy= new Performer(gameModel);
                break;
            case "PRIEST":
                strategy= new Priest(gameModel);
                break;
            case "QUEEN":
                strategy= new Queen(gameModel);
                break;
            case "THIEF":
                strategy= new Thief(gameModel);
            break;
        }
        return  strategy;
    }
}
