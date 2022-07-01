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

/**
 * SettingsController displays the page of settings(ip address and number port)
 */
public class SettingsController implements Initializable, GuiController {
    private Gui gui;
    private boolean expertMode;
    private final Integer[] numberOfPlayers = {2, 3, 4};
    @FXML
    AnchorPane connection_pane,settings_pane;
    @FXML
    Button confirm;
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

    /**
     * This method sets the ip address and the port number and checks if they are correct.
     * Then the player chooses the number of players and the game mode.
     * If there are no errors it also changes the scene to the 'waiting' scene
     */
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

    /**
     * It sets the players Number chosen
     * @param event of type ActionEvent
     */
    public void getPlayersNumberChoice(ActionEvent event) {
        gui.setPlayersNumber(playersNumber.getValue());
    }

    /**
     * It sets the gameMode chosen
     */
    public void getGameMode() {
        if (normal.isSelected()) {
            expertMode = false;
            gui.setExpertMode(false);
        } else if (expert.isSelected()) {
            expertMode = true;
            gui.setExpertMode(true);
        }
    }

    /**
     * This method initializes the playersNumber
     * @param url of type URl
     * @param resourceBundle of type ResourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        playersNumber.getItems().addAll(numberOfPlayers);
        playersNumber.setValue(2);
        playersNumber.setOnAction(this::getPlayersNumberChoice);
    }

    /**
     * @param gui of type Gui- the main Gui class
     * @see GuiController
     */
    @Override
    public void setGui(Gui gui) {
        this.gui = gui;
    }
    /**
     * Method pressButton detects when  a button is pressed
     * @param event of type Mouse Event
     */
    public void pressButton(MouseEvent event){
        ((Button)event.getSource()).getStyleClass().add("buttonPressed");

    }
    /**
     * Method pressButton detects when  a button is released
     * @param event of type Mouse Event
     */
    public void releaseButton(MouseEvent event){
        ((Button)event.getSource()).getStyleClass().clear();
        ((Button)event.getSource()).getStyleClass().add("button");
    }
}

