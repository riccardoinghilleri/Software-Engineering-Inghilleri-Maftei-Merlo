package it.polimi.ingsw.client.gui.controllers;

import it.polimi.ingsw.client.gui.Gui;
import javafx.stage.Stage;

public class ShopController implements GuiController{

    private Stage stage;
    private Gui gui;
    public void setStage(Stage stage) {
        this.stage = stage;
    }

    @Override
    public void setGui(Gui gui) {
        this.gui=gui;
    }
}
