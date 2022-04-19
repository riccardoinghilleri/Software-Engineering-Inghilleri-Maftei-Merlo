package it.polimi.ingsw.controller.actioncontroller;

import it.polimi.ingsw.server.ConnectionMessage.ActionMessage;

public interface CharacterCardStrategy {
    void useEffect(ActionMessage actionMessage);
}
