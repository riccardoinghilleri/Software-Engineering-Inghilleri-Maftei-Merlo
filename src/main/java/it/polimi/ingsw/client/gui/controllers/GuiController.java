package it.polimi.ingsw.client.gui.controllers;

import it.polimi.ingsw.client.gui.Gui;

/**
 * This class is an interface which represents a single GUI controller, which is different from phase to phase.
 *
 */
public interface GuiController {
    /**
     *  Method setGui sets the Gui reference to the local controller
     * @param gui of type Gui- the main Gui class
     */
    void setGui(Gui gui);
}
