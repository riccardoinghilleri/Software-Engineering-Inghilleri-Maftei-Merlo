package it.polimi.ingsw.controller;

import it.polimi.ingsw.controller.actioncontroller.CharacterCardStrategy;
import it.polimi.ingsw.controller.actioncontroller.*;
import it.polimi.ingsw.server.model.GameModel;

/**
 * This class creates strategically  all the character card with different 'powers' except
 * diner, lumberjack, centaur and knight, which extends directly the action controller.
 */
public class StrategyFactory {
    /**
     * This constructor, according to the name given, creates the card.
     * @param characterCardName the name of the card to be created
     * @param gameModel an instance of game model
     * @return a character card strategy
     */
    public static CharacterCardStrategy strategyFactory(String characterCardName, GameModel gameModel) {
        CharacterCardStrategy strategy = null;
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
