package it.polimi.ingsw.client.gui.controllers;

import com.sun.scenario.effect.Color4f;
import it.polimi.ingsw.client.gui.Gui;
import it.polimi.ingsw.server.ConnectionMessage.SetupMessage;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.effect.Glow;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.util.List;
import java.util.stream.Collectors;

/**
 * SetUpController displays the initial Stage, when the client has to choose nickname, color and wizard.
 */
public class SetupController implements GuiController {
    private Gui gui;
    @FXML
    Button right;
    @FXML
    AnchorPane nickname_pane, tower_pane, wizard_pane, gandalf_pane, king_pane, fairy_pane, wise_pane;
    @FXML
    TextField nickname_textField;

    @FXML
    Rectangle nickname_rectangle, color_rectangle, wizard_rectangle;

    @FXML
    Label error_label;

    private String choice;

    private Object selectNode = null;

    /**
     * This method sends throw the connection the chosen nickname or the choice of color and wizard, after the player puts in input
     */
    //bottone checkNickname
    public void sendChoice() {
        if (nickname_pane.isVisible()) {
            gui.getConnection().send(new SetupMessage(nickname_textField.getText()));
        }
        else gui.getConnection().send(new SetupMessage(choice));
        if (wizard_pane.isVisible())
            gui.changeScene("waiting.fxml");
    }

    public AnchorPane getNickname() {
        return nickname_pane;
    }

    /**
     * This method activates the anchor pane for the color choice(which corresponds to the tower).
     * @param colors list of string
     */
    public void enablePlayerColors(List<String> colors) {
        right.setDisable(true);
        nickname_pane.setVisible(false);
        nickname_rectangle.setOpacity(0.5);
        color_rectangle.setFill(Color.rgb(188,95,58));
        tower_pane.setVisible(true);
        if (colors.stream().distinct().count() > 1) {
            for (Node node : tower_pane.getChildren()) {
                ImageView tower = (ImageView) node;
                if (colors.contains(tower.getId().toUpperCase())) {
                    tower.setDisable(false);
                }
            }
        } else {
            automaticChoiceAlert(colors.get(0));
        }
    }
    /**
     * This method select the object when the mouse is on it, changing the glow of the node selected.
     * @param event of type MouseEvent
     */
    public void select(MouseEvent event) {
        Object object = event.getSource();
        ((Node) object).setEffect(new Glow(0.8));
    }
    /**
     * This method unselect the object when the mouse is on it, changing the glow of the node selected.
     * @param event of Type Mouse Event.
     */
    public void unselect(MouseEvent event) {
        Object object = event.getSource();
        ((Node) object).setEffect(null);
    }

    /**
     * This method activates the anchor pane for to wizard choice.
     * @param wizards list of string
     */
    public void enableWizards(List<String> wizards) {
        right.setDisable(true);
        selectNode = null;
        tower_pane.setVisible(false);
        color_rectangle.setOpacity(0.5);
        wizard_pane.setVisible(true);
        wizard_rectangle.setFill(Color.rgb(188,95,58));
        if (wizards.size() > 1) {
            for (Node node : wizard_pane.getChildren()) {
                AnchorPane wizard = (AnchorPane) node;
                if (wizards.contains(wizard.getId().toUpperCase().split("_")[0])) {
                    wizard.setDisable(false);
                }
            }
        } else {
            automaticChoiceAlert(wizards.get(0));
        }
    }

    /**
     * This method shows a scene with an automatic choice( wizard or color)
     * @param choice of type String
     */
    public void automaticChoiceAlert(String choice) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Automatic Choice");
        alert.setHeaderText(">The Game has chosen for you: " + choice);
        alert.showAndWait();
    }

    /**
     * This set visible the nickname error label
     * @param visible of type Boolean
     */
    public void setNicknameNotAvailable(boolean visible) {
        error_label.setVisible(visible);
    }

    /**
     * This method manages the selection of the image tower.
     * @param event of type MouseEvent
     */
    public void setChoiceTower(MouseEvent event) {
        right.setDisable(false);
        if (selectNode != null) {
            ((ImageView) selectNode).setEffect(null);
            ((ImageView) selectNode).setOnMouseExited(this::unselect);
        }
        selectNode = event.getSource();
        choice = ((ImageView) event.getSource()).getId().toUpperCase();
        ImageView image = (ImageView) event.getSource();
        image.setEffect(new Glow(0.8));
        image.setOnMouseExited(null);
    }

    /**
     * This method manages the selection of the tower.
     * @param event of ActionEvent type
     */
    public void setChoice(ActionEvent event) {
        right.setDisable(false);
        if (selectNode != null) {
            ((Button) selectNode).getStyleClass().clear();
            ((Button) selectNode).getStyleClass().add("button");
            ((Button) selectNode).setOnMouseExited(this::releaseButton);
        }
        selectNode = event.getSource();
        choice = ((Button) event.getSource()).getId().toUpperCase();
        ((Button) event.getSource()).getStyleClass().add("buttonPressed");
        ((Button) event.getSource()).setOnMouseExited(null);
    }

    /**
     * @see GuiController
     * @param gui of type Gui- the main Gui class
     */
    @Override
    public void setGui(Gui gui) {
        this.gui = gui;
    }
    /**
     * Method pressButton detects when  a button is pressed
     * @param event of type Mouse Event
     */
    public void pressButton(MouseEvent event) {
        ((Button) event.getSource()).getStyleClass().add("buttonPressed");

    }
    /**
     * Method pressButton detects when  a button  is released
     * @param event of type Mouse Event
     */
    public void releaseButton(MouseEvent event) {
        ((Button) event.getSource()).getStyleClass().clear();
        ((Button) event.getSource()).getStyleClass().add("button");
    }
}

