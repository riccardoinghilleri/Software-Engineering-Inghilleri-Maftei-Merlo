package it.polimi.ingsw.client.gui.controllers;

import it.polimi.ingsw.client.ClientConnection;
import it.polimi.ingsw.client.gui.Gui;
import it.polimi.ingsw.server.ConnectionMessage.SettingsMessage;
import javafx.event.ActionEvent;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SettingsController implements Initializable, GuiController {

    private Gui gui;
    private boolean expertMode;
    @FXML
    AnchorPane connection_pane,settings_pane;
    @FXML
    private TextField address;
    @FXML
    private TextField port;
    @FXML
    private ChoiceBox<Integer> playersNumber;
    @FXML
    private RadioButton normal, expert;
    @FXML
    private Label warningIp, warningPort, warningConnection,subtitle;

    private final Integer[] numberOfPlayers = {2, 3, 4};

    //1550,4*830,4   1.867

    public void confirm() {
        boolean error = false;
        Pattern pattern = Pattern.compile("^(([0-9]|[1-9][0-9]|1[0-9][0-9]|2[0-4][0-9]|25[0-5])(\\.(?!$)|$)){4}$");
        Matcher m = pattern.matcher(address.getText());
        warningIp.setVisible(false);
        warningPort.setVisible(false);
        warningConnection.setVisible(false);

        if(connection_pane.isVisible()) {
            if (!m.matches()) {
                warningIp.setVisible(true);
                address.clear();
                error = true;
            }
            if (port.getText().isEmpty() || Integer.parseInt(port.getText()) < 1024 || Integer.parseInt(port.getText()) > 65535) {
                warningPort.setVisible(true);
                port.clear();
                error = true;
            }
            if (!error) {
                try {
                    gui.setAddress(address.getText());
                    gui.setPort(Integer.parseInt(port.getText()));
                    gui.setConnection(new ClientConnection(gui));
                    Thread t = new Thread(gui.getConnection());
                    t.start();
                    subtitle.setText("GameMode");
                    connection_pane.setVisible(false);
                    settings_pane.setVisible(true);
                } catch (IOException e) {
                    warningConnection.setVisible(true);
                    address.clear();
                    port.clear();

                }
            }
        } else {
            gui.getConnection().send(new SettingsMessage(gui.getPlayersNumber(), this.expertMode));
            gui.changeScene("waiting.fxml");
        }
    }

    public void getPlayersNumberChoice(ActionEvent event) {
        gui.setPlayersNumber(playersNumber.getValue());
    }

    public void getGameMode(ActionEvent event) {
        if (normal.isSelected()) {
            expertMode = false;
            gui.setExpertMode(false);
        } else if (expert.isSelected()) {
            expertMode = true;
            gui.setExpertMode(true);
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        playersNumber.getItems().addAll(numberOfPlayers);
        playersNumber.setValue(2);
        playersNumber.setOnAction(this::getPlayersNumberChoice);
    }

    @Override
    public void setGui(Gui gui) {
        this.gui = gui;
    }

    public void pressButton(MouseEvent event){
        ((Button)event.getSource()).getStyleClass().add("buttonPressed");

    }
    public void releaseButton(MouseEvent event){
        ((Button)event.getSource()).getStyleClass().clear();
        ((Button)event.getSource()).getStyleClass().add("button");
    }
}

