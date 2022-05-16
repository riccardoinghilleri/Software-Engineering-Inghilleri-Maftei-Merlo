package it.polimi.ingsw.client.gui.controllers;

import it.polimi.ingsw.client.gui.Gui;
import it.polimi.ingsw.server.ConnectionMessage.UpdateBoard;
import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;

public class MainSceneController implements GuiController {

    private Gui gui;
    @FXML
    private GridPane entranceGrid, greenStudentsGrid, redStudentsGrid,
            yellowStudentsGrid, pinkStudentsGrid, blueStudentsGrid,
            professorsGrid, towersGrid;
    @FXML
    private AnchorPane schoolPane;


    public GridPane getEntranceGrid() {
        return entranceGrid;
    }

    public GridPane getGreenStudentsGrid() {
        return greenStudentsGrid;
    }

    public GridPane getRedStudentsGrid() {
        return redStudentsGrid;
    }

    public GridPane getYellowStudentsGrid() {
        return yellowStudentsGrid;
    }

    public GridPane getPinkStudentsGrid() {
        return pinkStudentsGrid;
    }

    public GridPane getBlueStudentsGrid() {
        return blueStudentsGrid;
    }

    public GridPane getProfessorsGrid() {
        return professorsGrid;
    }

    public GridPane getTowersGrid() {
        return towersGrid;
    }

    public void viewInfoPlayer(){

    }

    public void update(UpdateBoard message){
        //TODO aggiornare tutta la grafica
    }

    @Override
    public void setGui(Gui gui) {
        this.gui=gui;
    }
}
