package it.polimi.ingsw.client.gui.controllers;

import it.polimi.ingsw.client.gui.Gui;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
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

    public void pressButton(MouseEvent event){
        ((Button)event.getSource()).getStyleClass().add("buttonPressed");

    }
    public void releaseButton(MouseEvent event){
        ((Button)event.getSource()).getStyleClass().clear();
        ((Button)event.getSource()).getStyleClass().add("button");
    }
    public void buy(MouseEvent event){

    }

    public void changeCard(MouseEvent event){

    }

    public void close(){

    }
}
