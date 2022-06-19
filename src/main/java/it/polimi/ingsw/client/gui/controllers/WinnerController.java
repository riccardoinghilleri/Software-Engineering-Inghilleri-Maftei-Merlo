package it.polimi.ingsw.client.gui.controllers;

import it.polimi.ingsw.client.gui.Gui;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;

public class WinnerController implements GuiController{

    private Gui gui;

    @FXML
    Label infoText,nickname_label;
    @FXML
    AnchorPane infoPanel;

    private void setNickname(String nickname){
        nickname_label.setText(nickname);
    }

    private void setInfoMessage(String message){
        infoPanel.setVisible(true);
        infoText.setText(message);
    }
    @Override
    public void setGui(Gui gui) {
        this.gui=gui;
    }
}
