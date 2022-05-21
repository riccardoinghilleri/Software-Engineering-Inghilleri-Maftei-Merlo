package it.polimi.ingsw.client.gui;


import it.polimi.ingsw.client.ClientConnection;
import it.polimi.ingsw.client.View;
import it.polimi.ingsw.client.gui.controllers.*;

import it.polimi.ingsw.enums.Action;
import it.polimi.ingsw.server.ConnectionMessage.*;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.Event;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.*;

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

    private Integer playersNumber=2;
    private String address;
    private int port;

    private boolean alreadyAskedAssistantCard =false;

    public static void main(String[] args) {
        launch(args);
    }

    public ClientConnection getConnection() {
        return connection;
    }

    public HashMap<String, Scene> getScenes() {
        return scenes;
    }

    public void setConnection(ClientConnection connection) {
        if (this.connection == null)
            this.connection = connection;
    }

    public Integer getPlayersNumber() {
        return playersNumber;
    }

    public void setPlayersNumber(Integer playersNumber) {
        this.playersNumber = playersNumber;
    }

    public GuiController getControllerByFxmlName(String name) {
        return controllers.get(name);
    }

    public GuiController getControllerByScene(Scene scene){
        String currentSceneFxmlName=null;
        for(String s: scenes.keySet()){
            if(scenes.get(s).equals(currentScene)){
                currentSceneFxmlName=s;
            }
        }
        return getControllerByFxmlName(currentSceneFxmlName);
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
        List<String> fxmlList = new ArrayList<>(Arrays.asList("settings.fxml",
                "waiting.fxml", "setupPlayer.fxml", "assistantCards.fxml", "mainScene.fxml","shop.fxml"));
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
        MainSceneController mainSceneController= (MainSceneController) getControllerByFxmlName("mainScene.fxml");
        switch(message.getAction()){
            case CHOOSE_ASSISTANT_CARD:
                Platform.runLater(() -> {
                    Stage chooseAssistantCard= new Stage();
                    chooseAssistantCard.setTitle("Eriantys");
                    chooseAssistantCard.setResizable(false);
                    chooseAssistantCard.setAlwaysOnTop(true);
                    chooseAssistantCard.setScene(scenes.get("assistantCards.fxml"));
                    chooseAssistantCard.initModality(Modality.APPLICATION_MODAL);
                    chooseAssistantCard.setOnCloseRequest(Event::consume);
                    AssistantCardsController controller = (AssistantCardsController) getControllerByFxmlName("assistantCards.fxml");
                    controller.setStage(chooseAssistantCard);
                    controller.enableCards(message.getAvailableAssistantCards());
                    if(alreadyAskedAssistantCard)
                        controller.error();
                    alreadyAskedAssistantCard=true;
                    chooseAssistantCard.showAndWait();
                });
                break;
            case CHOOSE_CHARACTER_CARD:
                break;
            case USE_CHARACTER_CARD:
                break;
            case DEFAULT_MOVEMENTS:
                alreadyAskedAssistantCard=false;
                Platform.runLater(() -> {
                    mainSceneController.setInfoText("Move the entrance student: ");
                    mainSceneController.setCurrentClientId(message.getSchool().getOwner().getClientID());
                    mainSceneController.setAction(Action.DEFAULT_MOVEMENTS);
                });

                break;
            case MOVE_MOTHER_NATURE:
                mainSceneController.setAction(Action.MOVE_MOTHER_NATURE);
                Platform.runLater(() -> {
                    mainSceneController.setInfoText("Move mother nature: ");
                    mainSceneController.enableIslandsBroke(message.getData());
                });
                break;
            case CHOOSE_CLOUD:
                mainSceneController.setAction(Action.CHOOSE_CLOUD);
                Platform.runLater(() -> {
                    mainSceneController.setInfoText("Choose a cloud:  ");
                    mainSceneController.enableClouds(message);
                });
                break;
        }
    }

    @Override
    public void displayInfo(InfoMessage message) {
        Platform.runLater(() -> {
            if(getControllerByScene(currentScene) instanceof WaitingController)
                ((WaitingController)getControllerByScene(currentScene)).setInfoText(message.getString());
            else if (getControllerByScene(currentScene) instanceof MainSceneController)
                ((MainSceneController)getControllerByScene(currentScene)).setInfoText(message.getString());
        });


    }

    @Override
    public void setupMultipleChoice(MultipleChoiceMessage message) {
        Platform.runLater(() -> {
            SetupPlayerController controller = (SetupPlayerController) getControllerByFxmlName("setupPlayer.fxml");
            if (!controller.getCheck().isDisable() && !controller.getNickname().isDisable()) {
                controller.setNicknameNotAvailable(false);
                controller.enablePlayerColors(message.getAvailableChoices());
            } else {
                controller.enableWizards(message.getAvailableChoices());
                //WaitingController controller = (WaitingController) getControllerByFxmlName("waiting.fxml");
            }
        });
    }

    @Override
    public void setupNickname(NicknameMessage message) {
        Platform.runLater(() -> {
            SetupPlayerController controller = (SetupPlayerController) getControllerByFxmlName("setupPlayer.fxml");
            if (message.getAlreadyAsked()) {
                controller.setNicknameNotAvailable(true);
                controller.setCheckNickname(false);
            } else changeScene("setupPlayer.fxml");
        });
    }

    @Override
    public void displayBoard(UpdateBoard message) {
        Platform.runLater(() -> {
            if(getControllerByScene(currentScene) instanceof WaitingController)
                changeScene("mainScene.fxml");
            MainSceneController controller = (MainSceneController) getControllerByScene(currentScene);
            controller.update(message);
            controller.setSchool(message.getBoard().getSchools());
        });
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
