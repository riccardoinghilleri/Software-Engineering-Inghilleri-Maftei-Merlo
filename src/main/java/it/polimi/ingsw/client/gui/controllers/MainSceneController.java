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
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.effect.Glow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.Objects;

public class MainSceneController implements GuiController {

    private Gui gui;
    public boolean firstTime = true;
    private int motherNatureIndex;
    private int sup_islands, inf_islands;

    @FXML
    private AnchorPane schoolPane, cloudsPane, islandsPane;
    @FXML
    Label nickname, infoText;
    @FXML
    AnchorPane entrance, greenStudents, redStudents, yellowStudents, pinkStudents, blueStudents, professors, towers;
    @FXML
    ImageView assistantCard;
    @FXML
    Circle greenCircle, redCircle, yellowCircle, pinkCircle, blueCircle;
    @FXML
    Button shopBtn, noBtn;

    private School[] school;
    private int displayedSchool = 0;
    private String studentColor = null;
    private ActionMessage message;

    public void setAction(Action action) {
        this.message=new ActionMessage();
        this.message.setAction(action);
        if(action==Action.DEFAULT_MOVEMENTS)
            createPlayer(school[gui.getConnection().getClientId()]);
    }

    public void setInfoText(String text) {
        infoText.setText(text);
    }

    public void setSchool(School[] school) {
        this.school = school;
        createPlayer(school[gui.getConnection().getClientId()]);
        displayedSchool = gui.getConnection().getClientId();
    }

    public Button getNoBtn() {
        return noBtn;
    }

    public Button getShopBtn() {
        return shopBtn;
    }

    public void sendMessage(MouseEvent event) {
        if (event.getSource() instanceof Circle)
            ((Circle) event.getSource()).setVisible(false);
        disableAllIslandsBroke();
        gui.getConnection().send(message);
    }

    public void glowEntrance(boolean value) {
        for (int i = 0; i < 9; i++) {
            if (entrance.getChildren().get(i + 9).isVisible())
                entrance.getChildren().get(i).setVisible(value);
            else entrance.getChildren().get(i).setVisible(false);
        }
    }


    public void glowDiningroom(String color, boolean visible) {
        int size;
        switch (color) {
            case "GREEN":
                size = school[displayedSchool].getDiningRoom().get(CharacterColor.GREEN).size();
                greenCircle.setLayoutX(size == 0 ? 12 : 12 + 24 * (size));
                greenCircle.setVisible(visible);
                break;
            case "RED":
                size = school[displayedSchool].getDiningRoom().get(CharacterColor.RED).size();
                redCircle.setLayoutX(size == 0 ? 12 : 12 + 24 * (size));
                redCircle.setVisible(visible);
                break;
            case "YELLOW":
                size = school[displayedSchool].getDiningRoom().get(CharacterColor.YELLOW).size();
                yellowCircle.setLayoutX(size == 0 ? 12 : 12 + 24 * (size));
                yellowCircle.setVisible(visible);
                break;
            case "PINK":
                size = school[displayedSchool].getDiningRoom().get(CharacterColor.PINK).size();
                pinkCircle.setLayoutX(size == 0 ? 12 : 12 + 24 * (size));
                pinkCircle.setVisible(visible);
                break;
            case "BLUE":
                size = school[displayedSchool].getDiningRoom().get(CharacterColor.BLUE).size();
                blueCircle.setLayoutX(size == 0 ? 12 : 12 + 24 * (size));
                blueCircle.setVisible(visible);
                break;
        }

    }

    public void select(MouseEvent event) {
        if (event.getSource() instanceof Circle) {
            ((Circle) event.getSource()).setStroke(Color.BLACK);
        } else if (((ImageView) event.getSource()).getId().split("_")[1].equalsIgnoreCase("island")
                || ((ImageView) event.getSource()).getId().split("_")[1].equalsIgnoreCase("cloud")) {
            ((ImageView) event.getSource()).setEffect(new Glow(1.0));
        } else {
            int id = Integer.parseInt(((ImageView) event.getSource()).getId().split("_")[0]) - 9;
            ((Circle) entrance.getChildren().get(id)).setStroke(Color.BLACK);
        }
    }

    public void unselect(MouseEvent event) {
        if (event.getSource() instanceof Circle) {
            ((Circle) event.getSource()).setStroke(Color.rgb(255, 223, 0));
        } else if (((ImageView) event.getSource()).getId().split("_")[1].equalsIgnoreCase("island")
                || ((ImageView) event.getSource()).getId().split("_")[1].equalsIgnoreCase("cloud")) {
            ((ImageView) event.getSource()).setEffect(null);
        } else {
            int id = Integer.parseInt(((ImageView) event.getSource()).getId().split("_")[0]) - 9;
            ((Circle) entrance.getChildren().get(id)).setStroke(Color.rgb(255, 223, 0));
        }
    }


    public void setStudentColor(MouseEvent event) {
        if (studentColor != null) {
            glowDiningroom(studentColor, false);
        }
        studentColor = ((ImageView) event.getSource()).getId().split("_")[1];
        if (message.getAction() == Action.DEFAULT_MOVEMENTS) {
            message.getParameters().clear();
            glowDiningroom(studentColor, true);
            enableAllIslandsBroke();
        }
        message.setParameter(studentColor.toUpperCase());
    }

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

    public void createPlayer(School school) {
        nickname.setText(school.getOwner().getNickname());
        int priority = (school.getOwner().getChosenAssistantCard() == null ? -1
                : school.getOwner().getChosenAssistantCard().getPriority());
        String wizard = school.getOwner().getDeck().getWizard().toString();
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
                                + school.getEntrance().get(i - 9).getColor().toString() + ".png"))));
                image.setId(i + "_" + school.getEntrance().get(i - 9).getColor().toString());
                image.setVisible(true);
            } else image.setVisible(false);
        }
        for (int i = 0; i < 10; i++)
            greenStudents.getChildren().get(i).setVisible(i < school.getDiningRoom().get(CharacterColor.GREEN).size());
        for (int i = 0; i < 10; i++)
            redStudents.getChildren().get(i).setVisible(i < school.getDiningRoom().get(CharacterColor.RED).size());
        for (int i = 0; i < 10; i++)
            yellowStudents.getChildren().get(i).setVisible(i < school.getDiningRoom().get(CharacterColor.YELLOW).size());
        for (int i = 0; i < 10; i++)
            pinkStudents.getChildren().get(i).setVisible(i < school.getDiningRoom().get(CharacterColor.PINK).size());
        for (int i = 0; i < 10; i++)
            blueStudents.getChildren().get(i).setVisible(i < school.getDiningRoom().get(CharacterColor.BLUE).size());
        for (int i = 0; i < 5; i++)
            professors.getChildren().get(i).setVisible(school.getProfessorByColor(CharacterColor.values()[i]) != null);
        for (int i = 0; i < 8; i++) {
            ImageView tower = (ImageView) towers.getChildren().get(i);
            tower.setImage(new Image(Objects.requireNonNull(getClass()
                    .getResourceAsStream("/graphics/pieces/" + school.getTowerColor().toString() + "_tower.png"))));
            tower.setVisible(i < school.getTowersNumber());
        }
        if (school.getOwnerId() != gui.getConnection().getClientId()
                || message == null || message.getAction() != Action.DEFAULT_MOVEMENTS) {
            schoolPane.setDisable(true);
            glowEntrance(false);
        } else {
            schoolPane.setDisable(false);
            glowEntrance(true);
        }

    }

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
        if(gui.isExpertMode()){
            ShopController controller=(ShopController)gui.getControllerByFxmlName("shop.fxml");
            controller.setCharacterCards(((BoardExpert)message.getBoard()).getCharacterCards());
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
                                        + s.getColor()
                                        + ".png"))));
                j++;
            }
            i++;
        }
        //Aggiornamento disposizione isole
        if (message.getBoard().getLastRemovedIslands().size() > 0) {
            double sup_distance, inf_distance;
            Timeline timeline;
            boolean sup = false, inf = false;
            int old_sup_islands = sup_islands;
            for (Integer index : message.getBoard().getLastRemovedIslands()) {
                islandsPane.getChildren().remove((int) index);
                if (index <= old_sup_islands) {
                    sup_islands--;
                    sup = true;
                } else {
                    inf_islands--;
                    inf = true;
                }
            }
            sup_distance = (685 - sup_islands * 137) / (double) (sup_islands + 1);
            inf_distance = (685 - inf_islands * 137) / (double) (inf_islands + 1);
            for (int count = 1; count <= sup_islands && sup; count++) {
                AnchorPane island = (AnchorPane) islandsPane.getChildren().get(count);
                double oldXPosition = island.getLayoutX();
                double finalX = 137 * count + sup_distance * count;
                timeline = new Timeline(new KeyFrame(
                        Duration.seconds(0.025), // ogni quanto va chiamata la funzione
                        x -> moveIsland(island, (int) (finalX - oldXPosition)))
                );
                timeline.setCycleCount((int) (Math.abs(oldXPosition - finalX)));
                timeline.play();
            }
            for (int count = islandsPane.getChildren().size() - 1; count >= sup_islands + 2 && inf; count--) {
                AnchorPane island = (AnchorPane) islandsPane.getChildren().get(count);
                double oldXPosition = island.getLayoutX();
                double finalX = 137 * (islandsPane.getChildren().size() - count) + inf_distance * (islandsPane.getChildren().size() - count);
                timeline = new Timeline(new KeyFrame(
                        Duration.seconds(0.025), // ogni quanto va chiamata la funzione
                        x -> moveIsland(island, (int) (finalX - oldXPosition)))
                );
                timeline.setCycleCount((int) (Math.abs(oldXPosition - finalX)));
                timeline.play();
            }
            message.getBoard().getLastRemovedIslands().clear();
        }

        /*
        if ((int) Math.ceil(((float) message.getBoard().getIslands().size() - 2.0) / 2.0) < sup_islands) {
            //islandsToRemove = (int) (sup_islands - Math.ceil(((float) message.getBoard().getIslands().size() - 2.0) / 2.0));
            FadeTransition fadeTransition = new FadeTransition(Duration.seconds(3), islandsPane.getChildren().get(1));
            fadeTransition.setFromValue(1.0);
            fadeTransition.setToValue(0.0);
            playAnimationAndWaitForFinish(fadeTransition);
            for(Integer index : message.getBoard().getLastRemovedIslands()){
                if(index<=sup_islands){
                    islandsPane.getChildren().remove((int)index);
                }
            }
            sup_islands = (int) Math.ceil(((float) message.getBoard().getIslands().size() - 2.0) / 2.0);
            distance = (685 - sup_islands * 137) / (double) (sup_islands + 1);
            for (int count = 1; count <= sup_islands; count++) {
                AnchorPane island = (AnchorPane) islandsPane.getChildren().get(count);
                double oldXPosition = island.getLayoutX();
                double finalX = 137 * count + distance * count;
                timeline = new Timeline(new KeyFrame(
                        Duration.seconds(0.025), // ogni quanto va chiamata la funzione
                        x -> moveIsland(island,(int)(finalX-oldXPosition)))
                );
                timeline.setCycleCount((int) (oldXPosition - finalX));
                timeline.play();
                shape.setLayoutX(137 * count + distance * count);
                shape.setLayoutY(0);
            }

        }
        if (message.getBoard().getIslands().size() - 2 - sup_islands < inf_islands) {
            //islandsToRemove = inf_islands - (message.getBoard().getIslands().size() - 2 - sup_islands);
            int oldSize = islandsPane.getChildren().size();
            for(Integer index : message.getBoard().getLastRemovedIslands()){
                if(index>sup_islands){
                    islandsPane.getChildren().remove((int)index);
                }
            }
            inf_islands = message.getBoard().getIslands().size() - 2 - sup_islands;
            distance = (685 - inf_islands * 137) / (double) (inf_islands + 1);
            for (int count = islandsPane.getChildren().size() - 1; count >= sup_islands + 2; count--) {
                AnchorPane island = (AnchorPane) islandsPane.getChildren().get(count);
                double oldXPosition = island.getLayoutX();
                double finalX = 137 * (islandsPane.getChildren().size() - count) + distance * (islandsPane.getChildren().size() - count);
                timeline = new Timeline(new KeyFrame(
                        Duration.seconds(0.025), // ogni quanto va chiamata la funzione
                        x -> moveIsland(island,(int)(finalX-oldXPosition)))
                );
                timeline.setCycleCount((int) (Math.abs(oldXPosition - finalX)));
                timeline.play();
                shape.setLayoutX(137 * (islandsPane.getChildren().size() - count) + distance * (islandsPane.getChildren().size() - count));
                shape.setLayoutY(274);
            }
        }*/

        //Riempimento isole
        clearAllIslands();
        motherNatureIndex = message.getBoard().getMotherNaturePosition();
        AnchorPane studentsPane;
        for (i = 0; i < message.getBoard().getIslands().size(); i++) {
            j = 1;
            shape = (AnchorPane) islandsPane.getChildren().get(i);
            shape.getChildren().get(6).setId(i + "_island");
            shape.getChildren().get(7).setVisible(message.getBoard().getIslands().get(i).hasMotherNature());

            if (message.getBoard().getIslands().get(i).hasNoEntryTile()) {
                ((Label) shape.getChildren().get(9)).setText("x1"); //TODO fare il caso con più NoEntryTile
                shape.getChildren().get(8).setVisible(true);
                shape.getChildren().get(9).setVisible(true);
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

    public void chooseCloud(MouseEvent event) {
        int index = Integer.parseInt(((ImageView) event.getSource()).getId().split("_")[0]);
        message.setData(index);
        disableClouds();
        //emptyCloud(Objects.requireNonNull(getCloudById(index)));
        gui.getConnection().send(message);
    }

    public void setIsland(MouseEvent event) {
        int index = Integer.parseInt(((ImageView) event.getSource()).getId().split("_")[0]);
        if (message.getAction() == Action.DEFAULT_MOVEMENTS)
            glowDiningroom(studentColor, false);
        else {
            if (index < motherNatureIndex) {
                index += islandsPane.getChildren().size();
            }
            index -= motherNatureIndex;
        }
        disableAllIslandsBroke();
        message.setData(index);
        gui.getConnection().send(message);
    }

    public void enableAllIslandsBroke() {
        for (Node node : islandsPane.getChildren()) {
            ((AnchorPane) node).getChildren().get(6).setDisable(false);
            ((AnchorPane) node).getChildren().get(6).setVisible(true);
        }
    }

    public void disableAllIslandsBroke() {
        for (Node node : islandsPane.getChildren()) {
            ((AnchorPane) node).getChildren().get(6).setDisable(true);
            ((AnchorPane) node).getChildren().get(6).setVisible(false);
        }
    }

    public void enableIslandsBroke(int motherNatureSteps) {
        for (int i = 0; i < motherNatureSteps; i++) {
            AnchorPane island = (AnchorPane) islandsPane.getChildren()
                    .get((i + 1 + motherNatureIndex) % islandsPane.getChildren().size());
            island.getChildren().get(6).setDisable(false);
            island.getChildren().get(6).setVisible(true);
        }
    }

    public void enableClouds(AskActionMessage message) {
        for (int i = 0; i < message.getClouds().length; i++) {
            if (!(message.getClouds())[i].getStudents().isEmpty()) {
                AnchorPane cloud = getCloudById(i);
                Objects.requireNonNull(cloud).getChildren().get(5).setDisable(false);
                cloud.getChildren().get(5).setVisible(true);
            }
        }
    }

    public void disableClouds() {
        for (Node node : cloudsPane.getChildren()) {
            ((AnchorPane) node).getChildren().get(5).setDisable(true);
            ((AnchorPane) node).getChildren().get(5).setVisible(false);
        }
    }

    private AnchorPane getCloudById(int index) {
        for (Node cloud : cloudsPane.getChildren()) {
            ImageView broke = (ImageView) ((AnchorPane) cloud).getChildren().get(5);
            if (cloud.isVisible() && Integer.parseInt(broke.getId().split("_")[0]) == index) {
                return (AnchorPane) cloud;
            }
        }
        return null;
    }

    private void emptyCloud(AnchorPane cloud) {
        for (int i = 1; i < cloud.getChildren().size() - 1; i++) {
            ((ImageView) cloud.getChildren().get(i)).setImage(null);
        }
    }

    private void clearAllIslands() {
        for (Node node : islandsPane.getChildren()) {
            AnchorPane island = (AnchorPane) node;
            for (int i = 1; i <= 5; i++) { //studenti
                island.getChildren().get(i).setDisable(true);
                island.getChildren().get(i).setVisible(false);
            }
            for (int i = 8; i <= 11; i++) { //torri e noEntryTiles
                island.getChildren().get(10).setVisible(false);//torre
            }

        }
    }

    public void pressButton(MouseEvent event) {
        ((Button) event.getSource()).getStyleClass().add("buttonPressed");

    }

    public void releaseButton(MouseEvent event) {
        ((Button) event.getSource()).getStyleClass().clear();
        ((Button) event.getSource()).getStyleClass().add("button");
    }

    public void openShop(ActionEvent event) {
        Platform.runLater(() -> {
            Stage shop = new Stage();
            shop.setTitle("CharacterCard Shop");
            shop.getIcons().add(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/graphics/icon.png"))));
            shop.setResizable(false);
            shop.setAlwaysOnTop(true);
            shop.setScene(gui.getScenes().get("shop.fxml"));
            shop.initModality(Modality.APPLICATION_MODAL);
            //shop.setOnCloseRequest(Event::consume);
            ShopController controller = (ShopController) gui.getControllerByFxmlName("shop.fxml");
            controller.setStage(shop);
            controller.setMessage(this.message);
            controller.createCharacterCard();
            //TODO devo mandarmi le monete che ho controller.setCoinsLabel();
            shop.show();
            if (!noBtn.isDisable()) {
                noBtn.setDisable(true);
                noBtn.setVisible(false);
            }
        });
    }

    public void noCharacterCard(ActionEvent event) {
        message.setCharacterCardName(null);
        noBtn.setDisable(true);
        noBtn.setVisible(false);
        gui.getConnection().send(message);
    }

    private void moveIsland(AnchorPane island, int diff) {
        if (diff < 0) {
            island.setLayoutX(island.getLayoutX() - 1);
        } else island.setLayoutX(island.getLayoutX() + 1);
    }
    /*
    private synchronized void playAnimationAndWaitForFinish(final Animation animation) {
        if (Platform.isFxApplicationThread()) {
            throw new IllegalThreadStateException("Cannot be executed on main JavaFX thread");
        }
        final Thread currentThread = Thread.currentThread();
        final EventHandler<ActionEvent> originalOnFinished = animation.getOnFinished();
        animation.setOnFinished(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                if (originalOnFinished != null) {
                    originalOnFinished.handle(event);
                }
                synchronized (currentThread) {
                    currentThread.notify();
                }
            }
        });
        Platform.runLater(new Runnable() {

            @Override
            public void run() {
                animation.play();
            }
        });
        synchronized (currentThread) {
            try {
                currentThread.wait();
            } catch (InterruptedException ex) {
                //somebody interrupted me, OK
            }
        }
    }*/


    @Override
    public void setGui(Gui gui) {
        this.gui = gui;
    }

}
