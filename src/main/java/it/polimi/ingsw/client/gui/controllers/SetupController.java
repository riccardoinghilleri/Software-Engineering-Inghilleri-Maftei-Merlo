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


    //bottone checkNickname
    public void sendChoice() {
        if (nickname_pane.isVisible())
            gui.getConnection().send(new SetupMessage(nickname_textField.getText()));
        else gui.getConnection().send(new SetupMessage(choice));
        if (wizard_pane.isVisible())
            gui.changeScene("waiting.fxml");
    }

    public AnchorPane getNickname() {
        return nickname_pane;
    }

    //attiva anchor pane per la scelta del colore
    public void enablePlayerColors(List<String> colors) {
        right.setDisable(true);
        nickname_pane.setVisible(false);
        nickname_rectangle.setOpacity(0.5);
        color_rectangle.setFill(Color.rgb(188,95,58));
        tower_pane.setVisible(true);
        if (colors.size() > 1) {
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

    public void select(MouseEvent event) {
        Object object = event.getSource();
        ((Node) object).setEffect(new Glow(0.8));
    }

    public void unselect(MouseEvent event) {
        Object object = event.getSource();
        ((Node) object).setEffect(null);
    }

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

    public void automaticChoiceAlert(String choice) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Automatic Choice");
        alert.setHeaderText(">The Game has chosen for you: " + choice);
        alert.showAndWait();
    }

    public void setNicknameNotAvailable(boolean visible) {
        error_label.setVisible(visible);
    }

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

    @Override
    public void setGui(Gui gui) {
        this.gui = gui;
    }

    public void pressButton(MouseEvent event) {
        ((Button) event.getSource()).getStyleClass().add("buttonPressed");

    }

    public void releaseButton(MouseEvent event) {
        ((Button) event.getSource()).getStyleClass().clear();
        ((Button) event.getSource()).getStyleClass().add("button");
    }
}

