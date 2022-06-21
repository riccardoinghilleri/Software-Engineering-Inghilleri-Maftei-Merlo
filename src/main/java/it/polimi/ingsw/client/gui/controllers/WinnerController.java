package it.polimi.ingsw.client.gui.controllers;

import it.polimi.ingsw.client.gui.Gui;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.util.Objects;

public class WinnerController implements GuiController{

    private Gui gui;

    private Stage stage;

    @FXML
    Label nickname_label;
    @FXML
    Button quit;
    @FXML
    ImageView winner_image;

    public void setNickname(String nickname){
        nickname_label.setText(nickname);
    }
    public void setDrawImage(){
        winner_image.setImage(new Image(Objects.requireNonNull(getClass()
                .getResourceAsStream("/graphics/no_winners.png"))));
    }

    @Override
    public void setGui(Gui gui) {
        this.gui=gui;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void close(){
        this.stage.close();
        gui.close();
    }
    public void pressButton(MouseEvent event) {
        ((Button) event.getSource()).getStyleClass().add("buttonPressed");

    }

    public void releaseButton(MouseEvent event) {
        ((Button) event.getSource()).getStyleClass().clear();
        ((Button) event.getSource()).getStyleClass().add("button");
    }
}
