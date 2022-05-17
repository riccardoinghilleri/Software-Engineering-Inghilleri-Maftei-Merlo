package it.polimi.ingsw.client.gui.controllers;

import it.polimi.ingsw.client.gui.Gui;
import it.polimi.ingsw.client.gui.shapes.StudentShape;
import it.polimi.ingsw.enums.CharacterColor;
import it.polimi.ingsw.server.ConnectionMessage.UpdateBoard;
import it.polimi.ingsw.server.model.Board;
import it.polimi.ingsw.server.model.Island;
import it.polimi.ingsw.server.model.Student;
import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;

public class MainSceneController implements GuiController {

    private Gui gui;
    @FXML
    private GridPane entranceGrid, greenStudentsGrid, redStudentsGrid,
            yellowStudentsGrid, pinkStudentsGrid, blueStudentsGrid,
            professorsGrid, towersGrid, clouds_islands_grid, gridisland1;
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


    public void viewInfoPlayer() {

    }

    public void update(UpdateBoard message) {
        //TODO aggiornare tutta la grafica
        Board board = message.getBoard();
        boolean added;
        for (Island island : board.getIslands()) {
            for (CharacterColor color : island.getStudents().keySet()) {
                for (Student student : island.getStudents().get(color)) {
                    added=false;
                    StudentShape studentShape = new StudentShape(student.getColor().toString().toLowerCase(), 1, 1, this);
                    for(int i=0;i<gridisland1.getRowCount() && !added;i++){
                        for(int j=0;j<gridisland1.getColumnCount() && !added;j++){
                            if(gridisland1.getCellBounds(i,j).isEmpty()){
                                gridisland1.add(studentShape,i,j);
                                added=true;
                            }
                        }
                    }
                }
            }
        }
    }

    @Override
    public void setGui(Gui gui) {
        this.gui = gui;
    }
}
