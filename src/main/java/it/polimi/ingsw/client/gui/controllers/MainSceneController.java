package it.polimi.ingsw.client.gui.controllers;

import it.polimi.ingsw.client.gui.Gui;
import it.polimi.ingsw.enums.Action;
import it.polimi.ingsw.enums.CharacterColor;
import it.polimi.ingsw.server.ConnectionMessage.ActionMessage;
import it.polimi.ingsw.server.ConnectionMessage.AskActionMessage;
import it.polimi.ingsw.server.ConnectionMessage.UpdateBoard;
import it.polimi.ingsw.server.model.Cloud;
import it.polimi.ingsw.server.model.Student;
import it.polimi.ingsw.server.model.School;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
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

import java.util.Objects;

public class MainSceneController implements GuiController {

    private Gui gui;
    public boolean firstTime = true;
    private Action phase;

    @FXML
    private AnchorPane schoolPane, cloudsPane, islandsPane;
    @FXML
    Label nickname;

    @FXML
    TextArea infoText;

    @FXML
    AnchorPane entrance, greenStudents, redStudents, yellowStudents, pinkStudents, blueStudents, professors, towers;
    @FXML
    ImageView assistantCard;
    @FXML
    Circle greenCircle, redCircle, yellowCircle, pinkCircle, blueCircle;
    private School[] school;
    private int displayedSchool = 0, currentClientId = -1;
    private String studentColor = null;
    private ActionMessage message;

    public void setAction(Action action) {
        message = new ActionMessage();
        this.message.setAction(action);
    }

    public void setInfoText(String text) {
        infoText.setText(text);
    }

    public void setSchool(School[] school) {
        this.school = school;
        createPlayer(school[0]);
        displayedSchool = 0;
    }

    public void sendMessage(MouseEvent event) {
        if (event.getSource() instanceof Circle)
            ((Circle) event.getSource()).setVisible(false);
        gui.getConnection().send(message);
    }

    public void setCurrentClientId(int id) {
        currentClientId = id;
    }


    public void glowEntrance(boolean value) {
        for (int i = 0; i < 9; i++) {
            if (entrance.getChildren().get(i + 9).isVisible())
                entrance.getChildren().get(i).setVisible(value);
            else entrance.getChildren().get(i).setVisible(false);
        }
    }


    public void glowDiningroom(String color, boolean visible) {
        int size = -1;
        if (displayedSchool == currentClientId) {
            switch (color) {
                case "GREEN":
                    size = school[displayedSchool].getDiningRoom().get(CharacterColor.GREEN).size();
                    greenCircle.setLayoutX(size == 0 ? 12 : 12 * 3 * (size));
                    greenCircle.setVisible(visible);
                    break;
                case "RED":
                    size = school[displayedSchool].getDiningRoom().get(CharacterColor.RED).size();
                    redCircle.setLayoutX(size == 0 ? 12 : 12 * 3 * (size));
                    redCircle.setVisible(visible);
                    break;
                case "YELLOW":
                    size = school[displayedSchool].getDiningRoom().get(CharacterColor.YELLOW).size();
                    yellowCircle.setLayoutX(size == 0 ? 12 : 12 * 3 * (size));
                    yellowCircle.setVisible(visible);
                    break;
                case "PINK":
                    size = school[displayedSchool].getDiningRoom().get(CharacterColor.PINK).size();
                    pinkCircle.setLayoutX(size == 0 ? 12 : 12 * 3 * (size));
                    pinkCircle.setVisible(visible);
                    break;
                case "BLUE":
                    size = school[displayedSchool].getDiningRoom().get(CharacterColor.BLUE).size();
                    blueCircle.setLayoutX(size == 0 ? 12 : 12 * 3 * (size));
                    blueCircle.setVisible(visible);
                    break;
            }
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
        int priority = (school.getOwner().getChosenAssistantCard() == null ? -1 : school.getOwner().getChosenAssistantCard().getPriority());
        String wizard = school.getOwner().getDeck().getWizard().toString();
        if (priority != -1)
            assistantCard.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/graphics/assistantCards/Assistente" + priority + ".png"))));
        else
            assistantCard.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/graphics/assistantCards/" + wizard + ".png"))));
        for (int i = 9; i < 18; i++) {
            ImageView image = ((ImageView) entrance.getChildren().get(i));
            if (i < 9 + school.getEntrance().size()) {
                image.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/graphics/pieces/student_" + school.getEntrance().get(i - 9).getColor().toString() + ".png"))));
                image.setId(i + "_" + school.getEntrance().get(i - 9).getColor().toString());
                image.setVisible(true);
            } else image.setVisible(false);
        }
        for (int i = 0; i < 10; i++)
            ((AnchorPane) greenStudents).getChildren().get(i).setVisible(i < school.getDiningRoom().get(CharacterColor.GREEN).size());
        for (int i = 0; i < 10; i++)
            ((AnchorPane) redStudents).getChildren().get(i).setVisible(i < school.getDiningRoom().get(CharacterColor.RED).size());
        for (int i = 0; i < 10; i++)
            ((AnchorPane) yellowStudents).getChildren().get(i).setVisible(i < school.getDiningRoom().get(CharacterColor.YELLOW).size());
        for (int i = 0; i < 10; i++)
            ((AnchorPane) pinkStudents).getChildren().get(i).setVisible(i < school.getDiningRoom().get(CharacterColor.PINK).size());
        for (int i = 0; i < 10; i++)
            ((AnchorPane) blueStudents).getChildren().get(i).setVisible(i < school.getDiningRoom().get(CharacterColor.BLUE).size());
        for (int i = 0; i < 5; i++)
            ((AnchorPane) professors).getChildren().get(i).setVisible(school.getProfessorByColor(CharacterColor.values()[i]) != null);
        for (int i = 0; i < 8; i++)
            ((ImageView) ((AnchorPane) towers).getChildren().get(i)).setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/graphics/pieces/" + school.getTowerColor().toString() + "_tower.png"))));
        if (school.getOwnerId() != currentClientId || (message != null && message.getAction() != Action.DEFAULT_MOVEMENTS)) {
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
                    //cloudsPane.getChildren().get(1).setDisable(false);
                    cloudsPane.getChildren().get(1).setVisible(true);
                    ((AnchorPane) cloudsPane.getChildren().get(1)).getChildren().get(5).setId("0_cloud");
                    //cloudsPane.getChildren().get(3).setDisable(false);
                    cloudsPane.getChildren().get(3).setVisible(true);
                    ((AnchorPane) cloudsPane.getChildren().get(3)).getChildren().get(5).setId("1_cloud");
                    break;
                case 3:
                    //cloudsPane.getChildren().get(0).setDisable(false);
                    cloudsPane.getChildren().get(0).setVisible(true);
                    ((AnchorPane) cloudsPane.getChildren().get(0)).getChildren().get(5).setId("0_cloud");
                    //cloudsPane.getChildren().get(2).setDisable(false);
                    cloudsPane.getChildren().get(2).setVisible(true);
                    ((AnchorPane) cloudsPane.getChildren().get(2)).getChildren().get(5).setId("1_cloud");
                    //cloudsPane.getChildren().get(4).setDisable(false);
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
        //Riempimento isole
        AnchorPane studentsPane;
        for (i = 0; i < message.getBoard().getIslands().size(); i++) {
            j = 1;
            shape = (AnchorPane) islandsPane.getChildren().get(i);
            shape.getChildren().get(6).setId((i + 1) + "_island"); //a ogni broke setto l'indice dell'isola (uso i+1 come la cli)
            if (message.getBoard().getIslands().get(i).hasMotherNature()) {
                shape.getChildren().get(7).setVisible(true);
            } else {
                shape.getChildren().get(7).setVisible(false);
            }
            if (message.getBoard().getIslands().get(i).hasNoEntryTile()) {
                ((Label) shape.getChildren().get(9)).setText("x1"); //TODO fare il caso con più NoEntryTile
                shape.getChildren().get(8).setVisible(true);
            }
            if(!message.getBoard().getIslands().get(i).getTowers().isEmpty()){
                ((ImageView)shape.getChildren().get(10))
                        .setImage(new Image(Objects.requireNonNull(getClass()
                                .getResourceAsStream(message.getBoard().getIslands()
                                        .get(i).getColorTower().toString().toLowerCase() + "_tower.png"))));
                shape.getChildren().get(10).setVisible(true);
                ((Label)shape.getChildren().get(11)).setText("x"+message.getBoard()
                        .getIslands().get(i).getTowers().size());
                shape.getChildren().get(11).setVisible(true);
            }
            for (CharacterColor color : message.getBoard().getIslands().get(i).getStudents().keySet()) {
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
        gui.getConnection().send(message);
    }

    public void setIsland(MouseEvent event) {
        int index = Integer.parseInt(((ImageView) event.getSource()).getId().split("_")[0]);
        disableAllIslandsBroke();
        message.setData(index - 1);
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
        int motherNatureIndex = -1;
        for (Node node : islandsPane.getChildren()) {
            AnchorPane island = (AnchorPane) node;
            if (island.getChildren().get(7).isVisible()) {
                motherNatureIndex = Integer.parseInt(island.getChildren().get(6).getId().split("_")[0]);
                break;
            }
        }
        for (int i = 0; i < motherNatureSteps; i++) {
            AnchorPane island = (AnchorPane) islandsPane.getChildren()
                    .get((i + motherNatureIndex) % islandsPane.getChildren().size());
            island.getChildren().get(6).setDisable(false);
            island.getChildren().get(6).setVisible(true);
        }
    }

    public void enableClouds(AskActionMessage message) {
        for (int i = 0; i < message.getClouds().length; i++) {
            if (!(message.getClouds())[i].getStudents().isEmpty()) {
                AnchorPane cloud = getCloudById(i);
                cloud.getChildren().get(5).setDisable(false);
            }
        }
    }

    public void disableClouds() {
        for (Node node : cloudsPane.getChildren()) {
            ((AnchorPane)node).getChildren().get(5).setDisable(true);
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
    /*
    private void emptyCloud(AnchorPane cloud) {
        for (int i = 1; i < cloud.getChildren().size(); i++) {
            ((ImageView) cloud.getChildren().get(i)).setImage(null);
        }

    }*/

    @Override
    public void setGui(Gui gui) {
        this.gui = gui;
    }

}
