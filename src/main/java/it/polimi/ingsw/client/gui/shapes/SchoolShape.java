package it.polimi.ingsw.client.gui.shapes;

import it.polimi.ingsw.server.model.School;
import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;

public class SchoolShape {

    @FXML
    AnchorPane mainPane;
    @FXML
    GridPane Entrance, diningRoom;

    public void SchoolShape(School school){
        mainPane.setId(String.valueOf(school.getOwner().getClientID()));
    }
}
