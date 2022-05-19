package it.polimi.ingsw.client.gui.controllers;

import it.polimi.ingsw.client.ClientConnection;
import it.polimi.ingsw.client.gui.Gui;
import it.polimi.ingsw.server.ConnectionMessage.SettingsMessage;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;


import javax.swing.event.ChangeEvent;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SettingsController implements Initializable, GuiController {

    private Gui gui;
    private boolean expertMode;
    @FXML
    private TextField address;
    @FXML
    private TextField port;
    @FXML
    private ChoiceBox<Integer> playersNumber;
    @FXML
    private RadioButton normal, expert;
    @FXML
    private Label warningIp, warningPort, warningConnection;

    private final Integer[] numberOfPlayers = {2, 3, 4};

    public void confirm() {
        boolean error = false;
        Pattern pattern = Pattern.compile("^(([0-9]|[1-9][0-9]|1[0-9][0-9]|2[0-4][0-9]|25[0-5])(\\.(?!$)|$)){4}$");
        Matcher m = pattern.matcher(address.getText());
        warningIp.setVisible(false);
        warningPort.setVisible(false);
        warningConnection.setVisible(false);
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
                gui.getConnection().send(new SettingsMessage(gui.getPlayersNumber(), this.expertMode));
                gui.changeScene("waiting.fxml");
            } catch (IOException e) {
                //e.printStackTrace();
                warningConnection.setVisible(true);
                address.clear();
                port.clear();
                //printer.println("Error while opening the socket");
                //setupConnection();
            }
        }
    }

    public void getPlayersNumberChoice(ActionEvent event) {
        gui.setPlayersNumber(playersNumber.getValue());
    }

    public void getGameMode(ActionEvent event) {
        if (normal.isSelected()) {
            expertMode = false;
        } else if (expert.isSelected()) {
            expertMode = true;
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
}

