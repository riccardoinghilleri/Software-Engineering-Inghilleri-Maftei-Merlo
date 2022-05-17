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


import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SettingsController implements Initializable, GuiController {

    private Gui gui;
    private Integer myChoicePlayersNumber;
    private boolean expertMode;

    @FXML
    private Label title, warningIp, warningPort;
    @FXML
    private TextField address;
    @FXML
    private TextField port;
    @FXML
    private ChoiceBox<Integer> playersNumber;
    @FXML
    private RadioButton normal,expert;

    private final Integer[] numberOfPlayers = {2, 3, 4};

    public void confirm() {
        Pattern pattern = Pattern.compile("^(([0-9]|[1-9][0-9]|1[0-9][0-9]|2[0-4][0-9]|25[0-5])(\\.(?!$)|$)){4}$");
        Matcher m = pattern.matcher(address.getText());

        if (!m.matches()) {
            title.setText("Ip address is not correct!");
        } else if (Integer.parseInt(port.getText()) < 1024 || Integer.parseInt(port.getText()) > 65535) {
            title.setText("Server Port is not correct!");
        } else if (myChoicePlayersNumber < 2 || myChoicePlayersNumber > 4) {
        } else {
            try {
                gui.setAddress(address.getText());
                gui.setPort(Integer.parseInt(port.getText()));
                gui.setConnection(new ClientConnection(gui));
                Thread t = new Thread(gui.getConnection());
                t.start();
                gui.getConnection().send(new SettingsMessage(this.myChoicePlayersNumber,this.expertMode));
                gui.changeScene("waiting.fxml");
            } catch (IOException e) {
                e.printStackTrace();
                //printer.println("Error while opening the socket");
                //setupConnection();
            }
        }
    }

    public void getPlayersNumberChoice(ActionEvent event) {
        myChoicePlayersNumber = playersNumber.getValue();
    }



    public void getGameMode(ActionEvent event) {
        if(normal.isSelected()){
            expertMode=false;
        } else if(expert.isSelected()){
            expertMode=true;
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        playersNumber.getItems().addAll(numberOfPlayers);
        playersNumber.setOnAction(this::getPlayersNumberChoice);
    }

    @Override
    public void setGui(Gui gui) {
        this.gui=gui;
    }
}

