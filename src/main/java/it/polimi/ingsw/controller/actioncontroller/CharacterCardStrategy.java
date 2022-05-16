package it.polimi.ingsw.controller.actioncontroller;

import it.polimi.ingsw.server.ConnectionMessage.ActionMessage;

/**
 * This interface is implemented by all the cards except lumberjack,centaur, diner and knight.
 * It provides a method to use the card's effect, differently for each card.
 */
public interface CharacterCardStrategy {
    void useEffect(ActionMessage actionMessage);
}
