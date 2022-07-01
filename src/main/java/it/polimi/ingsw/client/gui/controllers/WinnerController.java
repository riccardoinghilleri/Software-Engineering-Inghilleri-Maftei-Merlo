package it.polimi.ingsw.client.gui.controllers;

import it.polimi.ingsw.client.gui.Gui;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.util.Objects;

/**
 * WinnerController displays the winner at the end of the game.
 */
public class WinnerController implements GuiController {
    private Gui gui;
    private Stage stage;
    @FXML
    Label nickname_label;
    @FXML
    Button quit;
    @FXML
    ImageView winner_image;

    /**
     * This method sets the nickname of the winner to the nickname label
     *
     * @param nickname string of winner's nickname
     */
    public void setNickname(String nickname) {
        nickname_label.setText(nickname);
    }

    /**
     * This method sets a particular image if there is a draw
     */
    public void setDrawImage() {
        winner_image.setImage(new Image(Objects.requireNonNull(getClass()
                .getResourceAsStream("/graphics/no_winners.png"))));
    }

    /**
     * @see GuiController
     */
    @Override
    public void setGui(Gui gui) {
        this.gui = gui;
    }

    /**
     * Setter of the stage
     */
    public void setStage(Stage stage) {
        this.stage = stage;
    }

    /**
     * This method closes the stage of the winner and close the gui
     */
    public void close() {
        this.stage.close();
        gui.close();
    }

    /**
     * Method pressButton detects when a button is pressed
     *
     * @param event of type Mouse Event
     */
    public void pressButton(MouseEvent event) {
        ((Button) event.getSource()).getStyleClass().add("buttonPressed");
    }

    /**
     * Method pressButton detects when a button is released
     *
     * @param event of type Mouse Event
     */
    public void releaseButton(MouseEvent event) {
        ((Button) event.getSource()).getStyleClass().clear();
        ((Button) event.getSource()).getStyleClass().add("button");
    }
}
