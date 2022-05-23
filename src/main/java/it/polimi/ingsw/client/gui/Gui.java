package it.polimi.ingsw.client.gui;


import it.polimi.ingsw.client.ClientConnection;
import it.polimi.ingsw.client.View;
import it.polimi.ingsw.client.gui.controllers.*;

import it.polimi.ingsw.enums.Action;
import it.polimi.ingsw.server.ConnectionMessage.*;
import it.polimi.ingsw.server.model.BoardExpert;
import it.polimi.ingsw.server.model.CharacterCard;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.Event;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.*;

import javax.swing.plaf.basic.BasicSplitPaneUI;
import java.io.IOException;
import java.util.*;

public class Gui extends Application implements View {
    private ClientConnection connection = null;
    private Stage stage;
    private Scene currentScene;
    private final HashMap<String, Scene> scenes = new HashMap<>(); //TODO forse è meglio avere un costruttore?
    private final HashMap<String, GuiController> controllers = new HashMap<>(); //TODO forse è meglio avere un costruttore?

    private Integer playersNumber = 2;
    private String address;
    private int port;
    private boolean expertMode;

    private boolean alreadyAskedAssistantCard = false;

    public static void main(String[] args) {
        launch(args);
    }

    public ClientConnection getConnection() {
        return connection;
    }


    public void setExpertMode(boolean expertMode) {
        this.expertMode = expertMode;
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

    public GuiController getControllerByScene(Scene scene) {
        String currentSceneFxmlName = null;
        for (String s : scenes.keySet()) {
            if (scenes.get(s).equals(scene)) {
                currentSceneFxmlName = s;
            }
        }
        return getControllerByFxmlName(currentSceneFxmlName);
    }

    public boolean isExpertMode() {
        return expertMode;
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
        List<String> fxmlList = new ArrayList<>(Arrays.asList("welcome.fxml","settings.fxml",
                "waiting.fxml", "setup.fxml", "assistantCards.fxml", "mainScene.fxml", "shop.fxml", "characterCard.fxml"));
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
        currentScene = scenes.get("welcome.fxml");
    }


    public void close(){
        stage.close();
    }
    @Override
    public void start(Stage primaryStage) {
        setup();
        this.stage = primaryStage;
        stage.setTitle("Eriantys");
        stage.getIcons().add(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/graphics/icon.png"))));
        stage.setScene(currentScene);
        stage.setResizable(false);
        stage.show();
    }

    @Override
    public void askAction(AskActionMessage message) {
        MainSceneController mainSceneController = (MainSceneController) getControllerByFxmlName("mainScene.fxml");
        switch (message.getAction()) {
            case CHOOSE_ASSISTANT_CARD:
                Platform.runLater(() -> {
                    Stage chooseAssistantCard = new Stage();
                    chooseAssistantCard.setTitle("Eriantys");
                    chooseAssistantCard.getIcons().add(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/graphics/icon.png"))));
                    chooseAssistantCard.setResizable(false);
                    chooseAssistantCard.setAlwaysOnTop(true);
                    chooseAssistantCard.setScene(scenes.get("assistantCards.fxml"));
                    chooseAssistantCard.initModality(Modality.APPLICATION_MODAL);
                    chooseAssistantCard.setOnCloseRequest(Event::consume);
                    AssistantCardsController controller =
                            (AssistantCardsController) getControllerByFxmlName("assistantCards.fxml");
                    controller.setStage(chooseAssistantCard);
                    controller.enableCards(message.getAvailableAssistantCards());
                    if (alreadyAskedAssistantCard)
                        controller.error();
                    alreadyAskedAssistantCard = true;
                    chooseAssistantCard.showAndWait();
                });
                break;
            case CHOOSE_CHARACTER_CARD:
                Platform.runLater(() -> {
                    mainSceneController.getNoBtn().setDisable(false);
                    mainSceneController.getNoBtn().setVisible(true);
                    mainSceneController.enableShop(true);
                    ShopController shopController = (ShopController) getControllerByFxmlName("shop.fxml");
                    shopController.setCharacterCards(message.getCharacterCards());
                    mainSceneController.setInfoText("Do you want to use a Character Card?");
                    mainSceneController.setAction(Action.CHOOSE_CHARACTER_CARD);
                    ((CharacterCardController) (getControllerByFxmlName("characterCard.fxml")))
                            .setAlreadyAskedMovements(false);
                });
                break;
            case USE_CHARACTER_CARD:
                Platform.runLater(() -> {
                    mainSceneController.setInfoText("You are using the Character Card");
                    mainSceneController.setAction(Action.USE_CHARACTER_CARD);
                    mainSceneController.getMessage()
                            .setCharacterCardName(message.getChosenCharacterCard().toString().toUpperCase());
                    if (message.getChosenCharacterCard().getName().toString().equalsIgnoreCase("PERFORMER")
                            && ((CharacterCardController) getControllerByFxmlName("characterCard.fxml"))
                            .isAlreadyAskedMovements()) {
                            //TODO performer. fare movimento tra diningroom ed entrance
                    } else {
                        switch (message.getChosenCharacterCard().getName()) {
                            case DIPLOMAT:
                            case HERBOLARIA:
                                mainSceneController.enableAllIslandsBroke();
                                break;
                            default:
                                Stage characterCard = new Stage();
                                characterCard.setTitle("Eriantys");
                                characterCard.getIcons().add(new Image(Objects.requireNonNull(getClass()
                                        .getResourceAsStream("/graphics/icon.png"))));
                                characterCard.setResizable(false);
                                characterCard.setAlwaysOnTop(true);
                                characterCard.setScene(scenes.get("characterCard.fxml"));
                                characterCard.initModality(Modality.APPLICATION_MODAL);
                                //characterCard.setOnCloseRequest(Event::consume);
                                CharacterCardController controller =
                                        (CharacterCardController) getControllerByFxmlName("characterCard.fxml");
                                controller.setStage(characterCard);
                                controller.setMainSceneController(mainSceneController);
                                controller.update(message.getChosenCharacterCard());
                                characterCard.showAndWait();
                                break;
                        }
                    }
                });
                break;
            case DEFAULT_MOVEMENTS:
                mainSceneController.enableShop(false);
                alreadyAskedAssistantCard = false;
                Platform.runLater(() -> {
                    mainSceneController.setInfoText("Move the entrance student: ");
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
                mainSceneController.enableShop(true);
                break;
        }

    }

    @Override
    public void displayInfo(InfoMessage message) {
        Platform.runLater(() -> {
            if (getControllerByScene(currentScene) instanceof WaitingController)
                ((WaitingController) getControllerByScene(currentScene)).setInfoText(message.getString());
            else if (getControllerByScene(currentScene) instanceof MainSceneController)
                ((MainSceneController) getControllerByScene(currentScene)).setInfoText(message.getString());
        });
    }

    @Override
    public void setupMultipleChoice(MultipleChoiceMessage message) {
        Platform.runLater(() -> {
            SetupController controller = (SetupController) getControllerByFxmlName("setup.fxml");
            if (controller.getNickname().isVisible()) {
                controller.enablePlayerColors(message.getAvailableChoices());
            } else {
                controller.enableWizards(message.getAvailableChoices());
            }
        });
    }

    @Override
    public void setupNickname(NicknameMessage message) {
        Platform.runLater(() -> {
            SetupController controller = (SetupController) getControllerByFxmlName("setup.fxml");
            if (message.getAlreadyAsked()) {
                controller.setNicknameNotAvailable(true);
            } else changeScene("setup.fxml");
        });
    }

    @Override
    public void displayBoard(UpdateBoard message) {
        Platform.runLater(() -> {
            MainSceneController controller;
            if (getControllerByScene(currentScene) instanceof WaitingController) {
                changeScene("mainScene.fxml");
                controller = (MainSceneController) getControllerByScene(currentScene);
                if (expertMode) {
                    controller.getShopBtn().setVisible(true);
                    controller.getShopBtn().setDisable(false);
                }
            }
            controller = (MainSceneController) getControllerByScene(currentScene);
            controller.setAction(null);
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
