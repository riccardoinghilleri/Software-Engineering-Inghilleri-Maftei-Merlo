package it.polimi.ingsw.client.gui.controllers;

import it.polimi.ingsw.client.gui.Gui;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;

public class WaitingController implements GuiController {
    private Gui gui;

    @FXML
    Label infoText;
    @Override
    public void setGui(Gui gui) {
        this.gui=gui;
    }
    public void setInfoText(String text){
        infoText.setText(text);
    }

}
