package it.polimi.ingsw.client.gui;


import it.polimi.ingsw.client.ClientConnection;
import it.polimi.ingsw.client.View;
import it.polimi.ingsw.client.gui.controllers.GuiController;
import it.polimi.ingsw.client.gui.controllers.SetupPlayerController;
import it.polimi.ingsw.client.gui.controllers.WaitingController;
import it.polimi.ingsw.server.ConnectionMessage.*;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class Gui extends Application implements View {
    private ClientConnection connection = null;
    private Stage stage;
    private Scene currentScene;
    private final HashMap<String, Scene> scenes = new HashMap<>(); //TODO forse è meglio avere un costruttore?
    private final HashMap<String, GuiController> controllers = new HashMap<>(); //TODO forse è meglio avere un costruttore?

    private String address;
    private int port;


    public static void main(String[] args) {
        launch(args);
    }

    public ClientConnection getConnection() {
        return connection;
    }

    public void setConnection(ClientConnection connection) {
        if (this.connection == null)
            this.connection = connection;
    }

    public GuiController getControllerByFxmlName(String name) {
        return controllers.get(name);
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public void changeScene(String newScene) {
        currentScene = scenes.get(newScene);
        stage.setScene(currentScene);
        stage.show();
    }

    private void setup() {
        List<String> fxmlList = new ArrayList<>(Arrays.asList("settings.fxml", "waiting.fxml", "setupPlayer.fxml"));
        try {
            for (String s : fxmlList) {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/" + s));
                scenes.put(s, new Scene(loader.load()));
                GuiController controller = loader.getController();
                controller.setGui(this);
                controllers.put(s, controller);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        currentScene = scenes.get("settings.fxml");
    }

    @Override
    public void start(Stage primaryStage) {
        setup();
        this.stage = primaryStage;
        stage.setTitle("Eriantys");
        stage.setScene(currentScene);
        stage.show();
    }

    @Override
    public void askAction(AskActionMessage message) {

    }

    @Override
    public void displayInfo(InfoMessage message) {

    }

    @Override
    public void setupMultipleChoice(MultipleChoiceMessage message) {

    }

    @Override
    public void setupNickname(NicknameMessage message) {
        Platform.runLater(() -> {
            changeScene("setupPlayer.fxml");
            SetupPlayerController controller = (SetupPlayerController) getControllerByFxmlName("setupPlayer.fxml");
            //controller.requestNickname(message);
        });
    }

    @Override
    public void displayBoard(UpdateBoard message) {

    }

    @Override
    public void setupConnection() {

    }

    @Override
    public String getAddress() {
        return address;
    }

    @Override
    public int getPort() {
        return port;
    }
}
