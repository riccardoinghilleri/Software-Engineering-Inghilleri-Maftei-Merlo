package it.polimi.ingsw.client.gui.controllers;

import it.polimi.ingsw.client.gui.Gui;
import it.polimi.ingsw.server.ConnectionMessage.NicknameMessage;
import it.polimi.ingsw.server.ConnectionMessage.SetupMessage;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;

public class SetupPlayerController {

    private Gui gui;
    private String color;
    private String magician;
    @FXML
    private RadioButton black,white,grey,fairy,gandalf,king,wise;
    @FXML
    private Label nicknameNotAvailable, nickname;

    public void checkNickname() {
        gui.getConnection().send(new SetupMessage(nickname.getText()));
    }

    public void setNicknameNotAvailable(boolean b){
        nicknameNotAvailable.setVisible(b);
    }

    public void play(){

    }

    public void getPlayerColor(ActionEvent event) {
        if(black.isSelected()){
            color="BLACK";
        } else if(white.isSelected()){
            color="WHITE";
        } else if(grey.isSelected()){
            color="GREY";
        }
    }
    public void getMagician(ActionEvent event) {
        if(fairy.isSelected()){
            magician="FAIRY";
        } else if(gandalf.isSelected()){
            magician="GANDALF";
        } else if(king.isSelected()){
            magician="KING";
        } else if(wise.isSelected()){
            magician="WISE";
        }
    }
}
