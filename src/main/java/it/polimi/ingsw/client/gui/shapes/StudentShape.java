package it.polimi.ingsw.client.gui.shapes;

import it.polimi.ingsw.client.gui.controllers.MainSceneController;
import javafx.scene.image.Image;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Ellipse;

public class StudentShape extends Ellipse {

    private int row, column;
    private final MainSceneController controller;

    public StudentShape(String color, int row, int column, MainSceneController controller){
        this.row=row;
        this.column=column;
        this.controller=controller;
        setColor(color);
    }

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public int getColumn() {
        return column;
    }

    public void setColumn(int column) {
        this.column = column;
    }

    private void setColor(String color){
        String s =
                "/graphics/pieces/student_"
                        + color
                        + ".png";
        Image img = new Image(getClass().getResourceAsStream(s));
        setFill(new ImagePattern(img));
    }
}
