package it.polimi.ingsw.client.gui.controllers;

import it.polimi.ingsw.client.gui.Gui;
import it.polimi.ingsw.enums.Action;
import it.polimi.ingsw.enums.CharacterColor;
import it.polimi.ingsw.server.ConnectionMessage.ActionMessage;
import it.polimi.ingsw.server.model.School;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

import java.util.Objects;

public class MainSceneController implements GuiController {

    private Gui gui;

    @FXML
    private AnchorPane schoolPane;
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
        } else {
            int id = Integer.parseInt(((ImageView) event.getSource()).getId().split("_")[0]) - 9;
            ((Circle) entrance.getChildren().get(id)).setStroke(Color.BLACK);
        }

    }

    public void unselect(MouseEvent event) {
        if (event.getSource() instanceof Circle) {
            ((Circle) event.getSource()).setStroke(Color.rgb(255, 223, 0));
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

    @Override
    public void setGui(Gui gui) {
        this.gui = gui;
    }

}
