package it.polimi.ingsw.client.gui.controllers;

import it.polimi.ingsw.client.gui.Gui;
import it.polimi.ingsw.enums.Action;
import it.polimi.ingsw.enums.CharacterColor;
import it.polimi.ingsw.server.ConnectionMessage.ActionMessage;
import it.polimi.ingsw.server.ConnectionMessage.AskActionMessage;
import it.polimi.ingsw.server.ConnectionMessage.UpdateBoard;
import it.polimi.ingsw.server.model.*;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.effect.Glow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.Objects;

/**
 * MainSceneController displays the counterpart of the board on Gui, and it is the principal scene,always visible when game starts. .
 * It handles the mainScene.fxml by executing commands from the GUI
 */
public class MainSceneController implements GuiController {
    private Gui gui;
    public boolean firstTime = true;
    private int motherNatureIndex, sup_islands, inf_islands;

    @FXML
    private AnchorPane schoolPane, cloudsPane, islandsPane;
    @FXML
    Label nickname, coin_label, infoText;
    @FXML
    AnchorPane entrance, greenStudents, redStudents, yellowStudents, pinkStudents, blueStudents, professors, towers;
    @FXML
    ImageView assistantCard, sachet;
    @FXML
    Circle green_Circle, red_Circle, yellow_Circle, pink_Circle, blue_Circle;
    @FXML
    Button shopBtn, noBtn, right ,left;

    private School[] school;
    private int[] coins;
    private int displayedSchool = 0;
    private String studentColor = null;
    private ActionMessage message = new ActionMessage();

    /**
     * SetAction method sets the action to an action message.
     * @param action type of action
     */
    //TODO CONTROLLA L'USO DEL DISABLESCHOOLBUTTONS
    public void setAction(Action action) {
        this.message = new ActionMessage();
        this.message.setAction(action);
        if (action == Action.DEFAULT_MOVEMENTS) {
            createPlayer(school[gui.getConnection().getClientId()]);
            glowEntrance(true);
        } else if (action == Action.USE_CHARACTER_CARD) {
            CharacterCardController controller = (CharacterCardController) gui.getControllerByFxmlName("characterCard.fxml");
            controller.setMessage(this.message);
        }
    }

    /**
     * Sets the text to the infoText label
     * @param text of type String
     */
    public void setInfoText(String text) {
        infoText.setText(text);
    }

    /**
     * @return the message of type ActionMessage
     */
    public ActionMessage getMessage() {
        return message;
    }

    /**
     * This method sets the message
     * @param message of type Action Message
     */
    public void setMessage(ActionMessage message) {
        this.message = message;
    }

    /**
     * This method manages the creation of the schools
     */
    public void setSchool(School[] school) {
        this.school = school;
        createPlayer(school[gui.getConnection().getClientId()]);
        displayedSchool = gui.getConnection().getClientId();
    }

    /**
     * Getter of button noBtn
     * @return a button
     */
    public Button getNoBtn() {
        return noBtn;
    }

    /**
     * Getter of button shopBtn
     * @return a button
     */
    public Button getShopBtn() {
        return shopBtn;
    }

    /**
     * This method disable the buttons right and left on the school.
     */
    public void disableSchoolButtons(boolean disable){
        right.setDisable(disable);
        left.setDisable(disable);
    }

    /**
     * This method manages the sending of messages from the gui throw the connection( of type ClientConnection)
     * @param event of type MouseEvent
     */
    public void sendMessage(MouseEvent event) {
        if (event.getSource() instanceof Circle) {
            ((Circle) event.getSource()).setVisible(false);
            glowDiningroom(studentColor, false);
            studentColor = null;
        }
        if (message.getAction() == Action.USE_CHARACTER_CARD
                && message.getCharacterCardName().equalsIgnoreCase("PERFORMER")) {
            message.setParameter(((Circle) event.getSource()).getId().split("_")[0].toUpperCase());
            ((Circle) event.getSource()).setFill(Color.WHITE);
            glowDiningroom(false);
            disableSchoolButtons(false);
            studentColor=null;
        }
        disableAllIslandsBroke();
        gui.getConnection().send(message);
    }

    /**
     * This method is used when a player has to choose a student from the entrance.
     * All available students are highlighted.
     */
    public void glowEntrance(boolean value) {
        if (value)
            schoolPane.setDisable(false);
        entrance.setDisable(!value);
        for (int i = 0; i < 9; i++) {
            if (entrance.getChildren().get(i + 9).isVisible())
                entrance.getChildren().get(i).setVisible(value);
            else entrance.getChildren().get(i).setVisible(false);
        }
    }



    /**
     * This method is used when a player has to choose a student from the entrance to move it to the diningRoom.
     * All available places are highlighted.
     * @param color of type String
     * @param visible of type boolean
     */
    public void glowDiningroom(String color, boolean visible) {
        int size;
        CharacterColor c = CharacterColor.valueOf(color);
        size = school[displayedSchool].getDiningRoom().get(c).size();
        if (size < 10) {
            AnchorPane diningPane = (AnchorPane) schoolPane.getChildren().get(c.ordinal() + 2);
            diningPane.setDisable(false);
            diningPane.getChildren().get(10).setLayoutX(size == 0 ? 12 : 12 + 24 * (size));
            diningPane.getChildren().get(10).setVisible(visible);
        } else
            infoText.setText("You cannot move this student in the Dining Room");

    }

    /**
     * This method is used when a player has to choose a student from the entrance to move it to the diningRoom.
     * @param visible of type boolean
     */
    public void glowDiningroom(boolean visible) {
        int size;
        for (int i = 2; i < 7; i++) {
            size = school[displayedSchool].getDiningRoom().get(CharacterColor.values()[i - 2]).size() - 1;
            if (size > -1) {
                AnchorPane pane = (AnchorPane) schoolPane.getChildren().get(i);
                pane.setDisable(false);
                ((Circle) pane.getChildren().get(10)).setFill(Color.TRANSPARENT);
                pane.getChildren().get(10).setLayoutX(size == 0 ? 12 : 12 + 24 * (size));
                pane.getChildren().get(10).setVisible(visible);
            }
        }
    }

    /**
     * This method manages the selection of different types of mouseEvent.
     * @param event a mouse action on the object.
     */
    public void select(MouseEvent event) {
        if (event.getSource() instanceof Circle) {
            ((Circle) event.getSource()).setStroke(Color.BLACK);
        } else if (event.getSource() instanceof Button) {
            ((Button) event.getSource()).setEffect(new Glow(0.8));
        } else if (((ImageView) event.getSource()).getId().split("_")[1].equalsIgnoreCase("island")
                || ((ImageView) event.getSource()).getId().split("_")[1].equalsIgnoreCase("cloud")) {
            ((ImageView) event.getSource()).setEffect(new Glow(1.0));
        } else {
            int id = Integer.parseInt(((ImageView) event.getSource()).getId().split("_")[0]) - 9;
            ((Circle) entrance.getChildren().get(id)).setStroke(Color.BLACK);
        }
    }
    /**
     * This method manages the deselection of different types of mouseEvent.
     * @param event a mouse action on the object.
     */
    public void unselect(MouseEvent event) {
        if (event.getSource() instanceof Circle) {
            ((Circle) event.getSource()).setStroke(Color.rgb(255, 223, 0));
        } else if (event.getSource() instanceof Button) {
            ((Button) event.getSource()).setEffect(null);
        } else if (((ImageView) event.getSource()).getId().split("_")[1].equalsIgnoreCase("island")
                || ((ImageView) event.getSource()).getId().split("_")[1].equalsIgnoreCase("cloud")) {
            ((ImageView) event.getSource()).setEffect(null);
        } else {
            int id = Integer.parseInt(((ImageView) event.getSource()).getId().split("_")[0]) - 9;
            ((Circle) entrance.getChildren().get(id)).setStroke(Color.rgb(255, 223, 0));
        }
    }

    /**
     * Method setStudentColor
     * @param event type of MouseEvent
     */
    public void setStudentColor(MouseEvent event) {
        if (studentColor != null) {
            glowDiningroom(studentColor, false);
        }
        studentColor = ((ImageView) event.getSource()).getId().split("_")[1];
        if (message.getAction() == Action.DEFAULT_MOVEMENTS) {
            message.getParameters().clear();
            glowDiningroom(studentColor, true);
            enableAllIslandsBroke();
        } else if (message.getAction() == Action.USE_CHARACTER_CARD
                && message.getCharacterCardName().equalsIgnoreCase("PERFORMER")) {
            message.getParameters().clear();
            glowDiningroom(true);
            infoText.setText("Choose the Dining Room Student");
        } else if (message.getAction() == Action.USE_CHARACTER_CARD) { //TODO in teoria serve solo per il clown
            glowEntrance(false);
        }
        message.setParameter(studentColor.toUpperCase());
        if (message.getAction() == Action.USE_CHARACTER_CARD
                && message.getCharacterCardName().equalsIgnoreCase("CLOWN")) {
            gui.getConnection().send(message);
            disableSchoolButtons(false);
            studentColor=null;
        }
    }

    /**
     * This method allows to switch between all the schools of the game.
     * The player can press 2 buttons, left and right.
     * @param event type of event
     */
    public void changeSchool(ActionEvent event) {
        if (((Button) event.getSource()).getId().equals("right")) {
            displayedSchool = (displayedSchool + 1) % school.length;
        } else if (((Button) event.getSource()).getId().equals("left")) {
            if (displayedSchool == 0)
                displayedSchool = school.length - 1;
            else displayedSchool = displayedSchool - 1;
        }
        createPlayer(school[displayedSchool]);
    }

    /**
     * This method creates the school for each player.
     * It has the image of the assistant card, the entrance and the diningRoom.
     * @param school instance of School
     */
    public void createPlayer(School school) {
        nickname.setText(school.getOwner().getNickname());
        if (gui.isExpertMode()) {
            coin_label.setVisible(true);
            coin_label.setText("x" + coins[displayedSchool]);
            sachet.setVisible(true);
        }
        int priority = (school.getOwner().getChosenAssistantCard() == null ? -1
                : school.getOwner().getChosenAssistantCard().getPriority());
        String wizard = school.getOwner().getDeck().getWizard().toString().toLowerCase();
        if (priority != -1)
            assistantCard.setImage(new Image(Objects.requireNonNull(getClass()
                    .getResourceAsStream("/graphics/assistantCards/Assistente" + priority + ".png"))));
        else
            assistantCard.setImage(new Image(Objects.requireNonNull(getClass()
                    .getResourceAsStream("/graphics/assistantCards/" + wizard + ".png"))));
        for (int i = 9; i < 18; i++) {
            ImageView image = ((ImageView) entrance.getChildren().get(i));
            if (i < 9 + school.getEntrance().size()) {
                image.setImage(new Image(Objects.requireNonNull(getClass()
                        .getResourceAsStream("/graphics/pieces/student_"
                                + school.getEntrance().get(i - 9).getColor().toString().toLowerCase() + ".png"))));
                image.setId(i + "_" + school.getEntrance().get(i - 9).getColor().toString());
                image.setVisible(true);
            } else image.setVisible(false);
        }
        //costruisco le diningroom
        for (CharacterColor c : CharacterColor.values()) {
            for (int i = 0; i < 10; i++) {
                AnchorPane diningPane = (AnchorPane) schoolPane.getChildren().get(c.ordinal() + 2);
                diningPane.getChildren().get(i).setVisible(i < school.getDiningRoom().get(c).size());
            }
        }
        for (int i = 0; i < 5; i++)
            professors.getChildren().get(i).setVisible(school.getProfessorByColor(CharacterColor.values()[i]) != null);
        for (int i = 0; i < 8; i++) {
            ImageView tower = (ImageView) towers.getChildren().get(i);
            tower.setImage(new Image(Objects.requireNonNull(getClass()
                    .getResourceAsStream("/graphics/pieces/" + school.getTowerColor().toString().toLowerCase() + "_tower.png"))));
            tower.setVisible(i < school.getTowersNumber());
        }
        glowEntrance(false);
        if (school.getOwnerId() != gui.getConnection().getClientId()
                || message == null || message.getAction() != Action.DEFAULT_MOVEMENTS) {
            schoolPane.setDisable(true);
            if (studentColor != null)
                glowDiningroom(studentColor, false);

        } else {
            schoolPane.setDisable(false);
            glowEntrance(message.getAction()==Action.DEFAULT_MOVEMENTS);
            if (studentColor != null)
                glowDiningroom(studentColor, true);
        }

    }

    /**
     * This method manages the update of the board.
     * It fills the clouds, updates the islands arrangement and fills the islands.
     * @param message type of UpdateBoard message
     */
    public void update(UpdateBoard message) {
        if (firstTime) {
            switch (gui.getPlayersNumber()) {
                case 2:
                    cloudsPane.getChildren().get(1).setVisible(true);
                    ((AnchorPane) cloudsPane.getChildren().get(1)).getChildren().get(5).setId("0_cloud");
                    cloudsPane.getChildren().get(3).setVisible(true);
                    ((AnchorPane) cloudsPane.getChildren().get(3)).getChildren().get(5).setId("1_cloud");
                    break;
                case 3:
                    cloudsPane.getChildren().get(0).setVisible(true);
                    ((AnchorPane) cloudsPane.getChildren().get(0)).getChildren().get(5).setId("0_cloud");
                    cloudsPane.getChildren().get(2).setVisible(true);
                    ((AnchorPane) cloudsPane.getChildren().get(2)).getChildren().get(5).setId("1_cloud");
                    cloudsPane.getChildren().get(4).setVisible(true);
                    ((AnchorPane) cloudsPane.getChildren().get(4)).getChildren().get(5).setId("2_cloud");
                    break;
                case 4:
                    cloudsPane.getChildren().get(0).setVisible(true);
                    ((AnchorPane) cloudsPane.getChildren().get(0)).getChildren().get(5).setId("0_cloud");
                    cloudsPane.getChildren().get(1).setVisible(true);
                    ((AnchorPane) cloudsPane.getChildren().get(1)).getChildren().get(5).setId("1_cloud");
                    cloudsPane.getChildren().get(3).setVisible(true);
                    ((AnchorPane) cloudsPane.getChildren().get(3)).getChildren().get(5).setId("2_cloud");
                    cloudsPane.getChildren().get(4).setVisible(true);
                    ((AnchorPane) cloudsPane.getChildren().get(4)).getChildren().get(5).setId("3_cloud");
                    break;
            }
            firstTime = false;
            sup_islands = 5;
            inf_islands = 5;
        }
        if (gui.isExpertMode()) {
            ShopController controller = (ShopController) gui.getControllerByFxmlName("shop.fxml");
            controller.setCharacterCards(((BoardExpert) message.getBoard()).getCharacterCards());
            coins = ((BoardExpert) message.getBoard()).getCoins();
            //TODO settare monete ma non so il client id
        }
        //Riempimento nuvole
        AnchorPane shape;
        int i = 0, j;
        for (Cloud cloud : message.getBoard().getClouds()) {
            j = 1;
            while (!cloudsPane.getChildren().get(i).isVisible()) {
                i++;
            }
            shape = (AnchorPane) cloudsPane.getChildren().get(i);
            emptyCloud(shape);
            for (Student s : cloud.getStudents()) {
                ((ImageView) shape.getChildren().get(j))
                        .setImage(new Image(Objects.requireNonNull(getClass()
                                .getResourceAsStream("/graphics/pieces/student_"
                                        + s.getColor().toString().toLowerCase()
                                        + ".png"))));
                j++;
            }
            i++;
        }
        //Aggiornamento disposizione isole
        int islandsToRemove;
        double distance, oldXPosition;
        Timeline timeline;
        if ((int) Math.ceil(((float) message.getBoard().getIslands().size() - 2.0) / 2.0) < sup_islands) {
            islandsToRemove = (int) (sup_islands - Math.ceil(((float) message.getBoard().getIslands().size() - 2.0) / 2.0));
            islandsPane.getChildren().subList(1, islandsToRemove + 1).clear();
            sup_islands = (int) Math.ceil(((float) message.getBoard().getIslands().size() - 2.0) / 2.0);
            distance = (685 - sup_islands * 137) / (double) (sup_islands + 1);
            for (int count = 1; count <= sup_islands; count++) {
                AnchorPane island = (AnchorPane) islandsPane.getChildren().get(count);
                oldXPosition = island.getLayoutX();
                timeline = new Timeline(new KeyFrame(
                        Duration.seconds(0.025), // ogni quanto va chiamata la funzione
                        x -> island.setLayoutX(island.getLayoutX() - 1))
                );
                timeline.setCycleCount((int) oldXPosition - (int) (137 * count + distance * count));
                timeline.play();
                //shape.setLayoutX(137 * count + distance * count);
                island.setLayoutY(0);
            }
        }
        if (message.getBoard().getIslands().size() - 2 - sup_islands < inf_islands) {
            islandsToRemove = inf_islands - (message.getBoard().getIslands().size() - 2 - sup_islands);
            int oldSize = islandsPane.getChildren().size();
            islandsPane.getChildren().subList(oldSize - islandsToRemove, oldSize).clear();
            inf_islands = message.getBoard().getIslands().size() - 2 - sup_islands;
            distance = (685 - inf_islands * 137) / (double) (inf_islands + 1);
            for (int count = islandsPane.getChildren().size() - 1; count >= sup_islands + 2; count--) {
                AnchorPane island = (AnchorPane) islandsPane.getChildren().get(count);
                oldXPosition = island.getLayoutX();
                timeline = new Timeline(new KeyFrame(
                        Duration.seconds(0.025), // ogni quanto va chiamata la funzione
                        x -> island.setLayoutX(island.getLayoutX() - 1))
                );
                timeline.setCycleCount((int) oldXPosition
                        - (int) (137 * (islandsPane.getChildren().size() - count)
                        + distance * (islandsPane.getChildren().size() - count)));
                timeline.play();
                //shape.setLayoutX(137 * (islandsPane.getChildren().size() - count) + distance * (islandsPane.getChildren().size() - count));
                island.setLayoutY(274);
            }
        }
        //Riempimento isole
        clearAllIslands();
        motherNatureIndex = message.getBoard().getMotherNaturePosition();
        AnchorPane studentsPane;
        for (i = 0; i < message.getBoard().getIslands().size(); i++) {
            j = 1;
            shape = (AnchorPane) islandsPane.getChildren().get(i);
            shape.getChildren().get(6).setId(i + "_island");
            shape.getChildren().get(7).setVisible(message.getBoard().getIslands().get(i).hasMotherNature());

            shape.getChildren().get(8).setVisible(message.getBoard().getIslands().get(i).hasNoEntryTile());
            shape.getChildren().get(9).setVisible(message.getBoard().getIslands().get(i).hasNoEntryTile());
            if (message.getBoard().getIslands().get(i).hasNoEntryTile()) {
                ((Label) shape.getChildren().get(9)).setText("x"+message.getBoard().getIslands().get(i).getNoEntryTile()); //TODO fare il caso con più NoEntryTile dopo il merge
            }
            if (!message.getBoard().getIslands().get(i).getTowers().isEmpty()) {
                ((ImageView) shape.getChildren().get(10))
                        .setImage(new Image(Objects.requireNonNull(getClass()
                                .getResourceAsStream("/graphics/pieces/" + message.getBoard().getIslands()
                                        .get(i).getColorTower().toString().toLowerCase() + "_tower.png"))));
                shape.getChildren().get(10).setVisible(true);
                ((Label) shape.getChildren().get(11)).setText("x" + message.getBoard()
                        .getIslands().get(i).getTowers().size());
                shape.getChildren().get(11).setVisible(true);
            }
            for (CharacterColor color : CharacterColor.values()) {
                if (!message.getBoard().getIslands().get(i).getStudents().get(color).isEmpty()) {
                    studentsPane = (AnchorPane) shape.getChildren().get(j);
                    studentsPane.getChildren().get(1).setId(i + "_" + color);//setto id all'immagine dello studente
                    Label studentsNumber = (Label) studentsPane.getChildren().get(2);
                    studentsNumber.setText("x" + message.getBoard().getIslands()
                            .get(i).getStudents().get(color).size());
                    studentsPane.setDisable(false);
                    studentsPane.setVisible(true);
                }
                j++;
            }
        }

    }

    /**
     * This method allows to choose the cloud and send the id of the cloud throw the connection.
     * Moreover, after choosing the cloud it enables the shop.
     * @param event of type MouseEvent
     */
    public void chooseCloud(MouseEvent event) {
        int index = Integer.parseInt(((ImageView) event.getSource()).getId().split("_")[0]);
        message.setData(index);
        disableClouds();
        gui.getConnection().send(message);
        enableShop(true);
    }

    /**
     * This method manages the actions which can be done on an island.
     * @param event a type of MouseEvent.
     */
    public void setIsland(MouseEvent event) {
        int index = Integer.parseInt(((ImageView) event.getSource()).getId().split("_")[0]);
        if (message.getAction() == Action.DEFAULT_MOVEMENTS){
            glowDiningroom(studentColor, false);
            studentColor = null;
        }
        else if (message.getAction() == Action.MOVE_MOTHER_NATURE) {
            if (index < motherNatureIndex) {
                index += islandsPane.getChildren().size();
            }
            index -= motherNatureIndex;
        }
        disableAllIslandsBroke();
        message.setData(index);
        gui.getConnection().send(message);
    }

    /**
     * This method enable all the islands to set the student
     */
    public void enableAllIslandsBroke() {
        for (Node node : islandsPane.getChildren()) {
            ((AnchorPane) node).getChildren().get(6).setDisable(false);
            ((AnchorPane) node).getChildren().get(6).setVisible(true);
        }
    }

    /**
     * This method disable all the island nodes from the IslandPane.
     */
    public void disableAllIslandsBroke() {
        for (Node node : islandsPane.getChildren()) {
            ((AnchorPane) node).getChildren().get(6).setDisable(true);
            ((AnchorPane) node).getChildren().get(6).setVisible(false);
        }
    }
    /**
     * This method makes enable all the island from the current island of mother nature
     * to the maximum number of steps of motherNature.
     */
    public void enableIslandsBroke(int motherNatureSteps) {
        for (int i = 0; i < motherNatureSteps; i++) {
            AnchorPane island = (AnchorPane) islandsPane.getChildren()
                    .get((i + 1 + motherNatureIndex) % islandsPane.getChildren().size());
            island.getChildren().get(6).setDisable(false);
            island.getChildren().get(6).setVisible(true);
        }
    }

    /**
     * Getting the number of clouds from the message, this method enable and makes visible the clouds.
     * @param message type of askActionMessage
     */
    public void enableClouds(AskActionMessage message) {
        for (int i = 0; i < message.getClouds().length; i++) {
            if (!(message.getClouds())[i].getStudents().isEmpty()) {
                AnchorPane cloud = getCloudById(i);
                Objects.requireNonNull(cloud).getChildren().get(5).setDisable(false);
                cloud.getChildren().get(5).setVisible(true);
            }
        }
    }

    /**
     * This method disable the clouds and the node cloud can't be pressed anymore.
     */
    public void disableClouds() {
        for (Node node : cloudsPane.getChildren()) {
            ((AnchorPane) node).getChildren().get(5).setDisable(true);
            ((AnchorPane) node).getChildren().get(5).setVisible(false);
        }
    }

    /**
     * This method returns the cloud requested throw the id
     * @param index id of cloud
     * @return the anchor pane type cloud
     */
    private AnchorPane getCloudById(int index) {
        for (Node cloud : cloudsPane.getChildren()) {
            ImageView broke = (ImageView) ((AnchorPane) cloud).getChildren().get(5);
            if (cloud.isVisible() && Integer.parseInt(broke.getId().split("_")[0]) == index) {
                return (AnchorPane) cloud;
            }
        }
        return null;
    }

    /**
     * This method displays the image of the cloud without students after being chosen by a player.
     * @param cloud anchorPane of type cloud
     */
    private void emptyCloud(AnchorPane cloud) {
        for (int i = 1; i < cloud.getChildren().size() - 1; i++) {
            ((ImageView) cloud.getChildren().get(i)).setImage(null);
        }
    }

    /**
     * This method manages the visibility of the objects on the island( students, tower and no entry tiles)
     */
    private void clearAllIslands() {
        for (Node node : islandsPane.getChildren()) {
            AnchorPane island = (AnchorPane) node;
            for (int i = 1; i <= 5; i++) { //studenti
                island.getChildren().get(i).setDisable(true);
                island.getChildren().get(i).setVisible(false);
            }
            for (int i = 8; i <= 11; i++) { //torri e noEntryTiles
                island.getChildren().get(i).setVisible(false);//torre
            }

        }
    }

    /**
     * This method detects when a button is pressed
     * @param event  of type MouseEvent
     */
    public void pressButton(MouseEvent event) {
        ((Button) event.getSource()).getStyleClass().add("buttonPressed");
    }
    /**
     * This method detects when a button is released
     * @param event of type MouseEvent
     */
    public void releaseButton(MouseEvent event) {
        ((Button) event.getSource()).getStyleClass().clear();
        ((Button) event.getSource()).getStyleClass().add("button");
    }

    /**
     * This method makes the button shopBtn ready to be pressed.
     * @param value boolean
     */
    public void enableShop(boolean value) {
        shopBtn.setDisable(!value);
    }

    /**
     * This method displays the stage shop, which contains the 3 character cards.
     */
    public void openShop() {
        Platform.runLater(() -> {
            Stage shop = new Stage();
            shop.setTitle("CharacterCard Shop");
            shop.getIcons().add(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/graphics/icon.png"))));
            shop.setResizable(false);
            shop.setAlwaysOnTop(true);
            shop.setScene(gui.getScenes().get("shop.fxml"));
            shop.initModality(Modality.APPLICATION_MODAL);
            ShopController controller = (ShopController) gui.getControllerByFxmlName("shop.fxml");
            controller.setStage(shop);
            controller.setMessage(this.message);
            controller.clear();
            controller.createCharacterCard();
            controller.setCoinsLabel(coins[gui.getConnection().getClientId()]);
            controller.setEnableBuyBtn(message.getAction() == Action.CHOOSE_CHARACTER_CARD);
            controller.createCharacterCard();
            //TODO devo mandarmi le monete che ho controller.setCoinsLabel();
            shop.show();
            if (!(message.getAction() == Action.CHOOSE_CHARACTER_CARD)) {
                noBtn.setDisable(true);
                noBtn.setVisible(false);
            }
        });
    }

    /**
     * This method sets the characterCardName to null and sends the message throw the connection.
     * The shop becomes not visible anymore.
     */
    public void noCharacterCard() {
        message.setCharacterCardName(null);
        noBtn.setDisable(true);
        noBtn.setVisible(false);
        enableShop(false);
        gui.getConnection().send(message);
    }

    /**
     * This method returns the number of the students in the diningRoom.
     */
    public int getDiningroomStudents() {
        return school[gui.getConnection().getClientId()].getNumDiningRoomStudents();
    }

    /**
     * @see GuiController
     * @param gui of type Gui- the main Gui class
     */
    @Override
    public void setGui(Gui gui) {
        this.gui = gui;
    }
}
