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
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.stage.*;
import java.io.IOException;
import java.util.*;

/**
 * GUI class starts the graphical user interface, mapping each scene to the specified phase.
 *
 */
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

    /**
     * Main class of the Gui,which is called from the "Eriantys" class in case the player decides to play with this interface.
     * @param args main arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

    /**
     * Method get connection returns the client connection
     * @return the client connection
     */
    public ClientConnection getConnection() {
        return connection;
    }

    /**
     * Method setExpertMode sets the game mode
     * @param expertMode boolean
     */

    public void setExpertMode(boolean expertMode) {
        this.expertMode = expertMode;
    }

    /**
     * Method getScenes returns an hashMap which connects strings to scenes.
     * @return an hashMap
     */
    public HashMap<String, Scene> getScenes() {
        return scenes;
    }

    /**
     *Method setConnection sets the connection
     * @param connection of type ClientConnection
     */
    public void setConnection(ClientConnection connection) {
        if (this.connection == null)
            this.connection = connection;
    }

    /**
     *Method getPlayersNumber return the player numbers
     * @return player numbers
     */
    public Integer getPlayersNumber() {
        return playersNumber;
    }
    /**
     * method  SetPlayersNumber sets the player numbers
     */
    public void setPlayersNumber(Integer playersNumber) {
        this.playersNumber = playersNumber;
    }

    /**
     * This method returns a gui controller
     * @param name string of the FXML file
     * @return the corresponding Gui controller
     */
    public GuiController getControllerByFxmlName(String name) {
        return controllers.get(name);
    }

    /**
     * This method returns a gui controller by using a scene and a hashmap.
     * @param scene of type Scene
     * @return a gui controller
     */
    public GuiController getControllerByScene(Scene scene) {
        String currentSceneFxmlName = null;
        for (String s : scenes.keySet()) {
            if (scenes.get(s).equals(scene)) {
                currentSceneFxmlName = s;
            }
        }
        return getControllerByFxmlName(currentSceneFxmlName);
    }

    /**
     * isExpertMode returns the game mode
     */
    public boolean isExpertMode() {
        return expertMode;
    }
    /**
     * setAddress sets the ip address
     */
    public void setAddress(String address) {
        this.address = address;
    }
    /**
     * setPort sets the port
     */
    public void setPort(int port) {
        this.port = port;
    }

    /**
     * Method changeScene sets the currentScene to a new scene and sets it to the stage.
     * @param newScene of type Scene
     */
    public void changeScene(String newScene) {
        currentScene = scenes.get(newScene);
        stage.setScene(currentScene);
        stage.show();
    }

    /**
     * This method creates all the stage phases which are updated in other methods.
     * It also sets the currentScene to the WelcomeScene.
     */
    private void setup() {
        List<String> fxmlList = new ArrayList<>(Arrays.asList("welcome.fxml", "settings.fxml",
                "waiting.fxml", "setup.fxml", "assistantCards.fxml", "mainScene.fxml", "shop.fxml", "characterCard.fxml", "winner.fxml"));
        try {
            for (String s : fxmlList) {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/" + s));
                scenes.put(s, new Scene(loader.load()));
                GuiController controller = loader.getController();
                controller.setGui(this);
                controllers.put(s, controller);
            }
        } catch (IOException e) {
            //e.printStackTrace();
        }
        currentScene = scenes.get("welcome.fxml");
    }

    /**
     * This method closes a stage
     */
    public void close() {
        stage.close();
    }

    /**
     * This method calls the setup() method and sets the initialStage.
     * @param primaryStage of type Stage
     */
    @Override
    public void start(Stage primaryStage) {
        setup();
        this.stage = primaryStage;
        stage.setOnCloseRequest(this::closeRequestHandler);
        stage.setTitle("Eriantys");
        stage.getIcons().add(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/graphics/icon.png"))));
        stage.setScene(currentScene);
        stage.setResizable(false);
        stage.show();
    }

    /**
     * Method closeRequestHandler manages the Quit option, when a player wants to disconnect in any moment during the game
     * @param event of type WindowEvent
     */
    private void closeRequestHandler(WindowEvent event) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Close");
        alert.setHeaderText("Are you sure you want to quit?");
        ButtonType yes = new ButtonType("YES");
        ButtonType no = new ButtonType("NO");

        alert.getButtonTypes().setAll(yes, no);
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == yes && connection!=null) {
            connection.send(new InfoMessage("QUIT",false));
        }
    }

    /**
     * Method askAction creates the mainSceneController,bound to the FXML scene of board
     * and manages the different phases of each turn during the game.
     * @param message of type AskActionMessage
     */
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
                    mainSceneController.setAction(Action.CHOOSE_CHARACTER_CARD);
                    mainSceneController.getNoBtn().setDisable(false);
                    mainSceneController.getNoBtn().setVisible(true);
                    mainSceneController.enableShop(true);
                    ShopController shopController = (ShopController) getControllerByFxmlName("shop.fxml");
                    shopController.setCharacterCards(message.getCharacterCards());
                    mainSceneController.setInfoText("Do you want to use a Character Card?");
                    ((CharacterCardController) (getControllerByFxmlName("characterCard.fxml")))
                            .setAlreadyAskedMovements(false);
                    mainSceneController.glowEntrance(false);
                });
                break;
            case USE_CHARACTER_CARD:
                Platform.runLater(() -> {
                    mainSceneController.setInfoText("You are using the Character Card");
                    mainSceneController.setAction(Action.USE_CHARACTER_CARD);
                    mainSceneController.getMessage()
                            .setCharacterCardName(message.getChosenCharacterCard().getName().toString().toUpperCase());
                    if (message.getChosenCharacterCard().getName().toString().equalsIgnoreCase("PERFORMER")
                            && ((CharacterCardController) getControllerByFxmlName("characterCard.fxml"))
                            .isAlreadyAskedMovements()) {
                             mainSceneController.setInfoText("Choose the Entrance Student:");
                             mainSceneController.glowEntrance(true);
                             mainSceneController.disableSchoolButtons(true);
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
                break;
        }

    }

    /**
     * This method displays the messages from server differently based on if the currentScene is a waitingController ot a mainSceneController
     * @param message of type InfoMessage
     */
    @Override
    public void displayInfo(InfoMessage message) {
        Platform.runLater(() -> {
            if (getControllerByScene(currentScene) instanceof WaitingController)
                ((WaitingController) getControllerByScene(currentScene)).setInfoText(message.getString().replace(">", ""));
            else if (getControllerByScene(currentScene) instanceof MainSceneController)
                ((MainSceneController) getControllerByScene(currentScene)).setInfoText(message.getString().replace(">", ""));
        });
    }

    /**
     * This method sets the available choices of wizards and playerColors on the setUpController ,from the message multipleChoiceMessage
     * @param message of type MultipleChoiceMessage
     */
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
    /**
     * This method sets the nickname of a player from the nickName message
     * @param message of type NicknameMessage
     */
    @Override
    public void setupNickname(NicknameMessage message) {
        Platform.runLater(() -> {
            SetupController controller = (SetupController) getControllerByFxmlName("setup.fxml");
            if (message.getAlreadyAsked()) {
                controller.setNicknameNotAvailable(true);
            } else changeScene("setup.fxml");
        });
    }

    /**
     * This method displays the updated Board, paying attention if the current scene is a waitingController and the connection
     * corresponds to the last player in the game.
     * @param message of type UpdateBoard
     */
    @Override
    public void displayBoard(UpdateBoard message) {
        Platform.runLater(() -> {
            MainSceneController controller;
            if (getControllerByScene(currentScene) instanceof WaitingController || connection.isLastPlayer()) {
                changeScene("mainScene.fxml");
                controller = (MainSceneController) getControllerByScene(currentScene);
                if (expertMode) {
                    controller.getShopBtn().setVisible(true);
                    controller.getShopBtn().setDisable(false);
                }
            }
            controller = (MainSceneController) getControllerByScene(currentScene);
            controller.update(message);
            controller.setSchool(message.getBoard().getSchools());
        });
    }

    /**
     * This method displays the winner stage and the message inside it.
     * @param message of type InfoMessage
     */
    public void displayWinner(InfoMessage message) {
        Platform.runLater(() -> {
            Stage winnerStage = new Stage();
            winnerStage.setTitle("Eriantys Winner");
            winnerStage.getIcons().add(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/graphics/icon.png"))));
            winnerStage.setResizable(false);
            winnerStage.setAlwaysOnTop(true);
            winnerStage.setScene(scenes.get("winner.fxml"));
            winnerStage.initModality(Modality.APPLICATION_MODAL);
            winnerStage.setOnCloseRequest(Event::consume);
            WinnerController controller =
                    (WinnerController) getControllerByFxmlName("winner.fxml");
            controller.setStage(winnerStage);
            controller.setNickname(!message.getWinner().equalsIgnoreCase("draw") ? message.getWinner() : "There are no winners.");
            if (message.getWinner().equalsIgnoreCase("draw"))
                controller.setDrawImage();
            winnerStage.setOnCloseRequest(this::closeStage);
            winnerStage.show();
        });
    }

    /**
     * This method closes the stage
     * @param event of type WindowEvent
     */
    public void closeStage(WindowEvent event){
        stage.close();
    }

    /**
     * This method displays on the stage the disconnection message
     * @param message of type InfoMessage
     */
    public void displayDisconnectionAlert(InfoMessage message) {
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Disconnection");
            alert.setHeaderText(message.getString());
            ButtonType ok = new ButtonType("OK");

            alert.getButtonTypes().setAll(ok);
            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ok) {
                stage.close();
            }
        });
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
