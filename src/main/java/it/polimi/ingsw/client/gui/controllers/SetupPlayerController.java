package it.polimi.ingsw.client.gui.controllers;

import it.polimi.ingsw.client.gui.Gui;

import it.polimi.ingsw.server.ConnectionMessage.SetupMessage;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.effect.Glow;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;

import java.util.List;


public class SetupPlayerController implements GuiController {

    private Gui gui;
    @FXML
    private Button check;
    @FXML
    private Label nicknameNotAvailable;
    @FXML
    private TextField nickname;
    @FXML
    private AnchorPane wizardsPane, playerColorsPane;

    //bottone checkNickname
    public void checkNickname() {
        gui.getConnection().send(new SetupMessage(nickname.getText()));
    }

    public Button getCheck() {
        return check;
    }

    public TextField getNickname() {
        return nickname;
    }

    //attiva anchor pane per la scelta del colore
    public void enablePlayerColors(List<String> colors) {
        check.setDisable(true);
        nickname.setDisable(true);
        if (colors.size() > 1) {
            for (Node node : playerColorsPane.getChildren()) {
                ImageView tower = (ImageView) node;
                if (colors.contains(tower.getId().toUpperCase())) {
                    tower.setDisable(false);
                    /*tower.addEventHandler(MouseEvent.MOUSE_ENTERED, event -> {
                        select(event);
                        event.consume();
                    });
                    tower.addEventHandler(MouseEvent.MOUSE_EXITED, event -> {
                        unselect(event);
                        event.consume();
                    });*/
                }
            }
        } else {
            automaticChoiceAlert(colors.get(0));
        }
    }

    public void select(MouseEvent event) {
        Object object =  event.getSource();
        ((Node)object).setEffect(new Glow(0.8));
    }

    public void unselect(MouseEvent event) {
        Object object =  event.getSource();
        ((Node)object).setEffect(null);
    }

    public void enableWizards(List<String> wizards) {
        playerColorsPane.setDisable(true);
        if (wizards.size() > 1) {
            for (Node node : wizardsPane.getChildren()) {
                AnchorPane wizard = (AnchorPane) node;
                if (wizards.contains(wizard.getId().toUpperCase())) {
                    wizard.setDisable(false);
                    /*wizard.addEventHandler(MouseEvent.MOUSE_ENTERED, event -> {
                        select(event);
                        event.consume();
                    });
                    wizard.addEventHandler(MouseEvent.MOUSE_EXITED, event -> {
                        unselect(event);
                        event.consume();
                    });*/
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
        nicknameNotAvailable.setVisible(visible);
    }

    public void setWizard(ActionEvent event) {
        gui.getConnection().send(new SetupMessage(((Button) event.getSource()).getText().toUpperCase()));
        gui.changeScene("waiting.fxml");
    }

    public void setPlayerColor(MouseEvent event) {
        gui.getConnection().send(new SetupMessage(((ImageView) event.getSource()).getId().toUpperCase()));
    }

    public void setCheckNickname(boolean disable) {
        check.setDisable(disable);
    }

    @Override
    public void setGui(Gui gui) {
        this.gui = gui;
    }
}
