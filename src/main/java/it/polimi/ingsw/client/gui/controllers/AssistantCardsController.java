package it.polimi.ingsw.client.gui.controllers;

import it.polimi.ingsw.client.gui.Gui;

public class AssistantCardsController implements GuiController{

    private Gui gui;

    @Override
    public void setGui(Gui gui) {
        this.gui=gui;
    }
}
