package it.polimi.ingsw.client.gui.controllers;

import it.polimi.ingsw.client.gui.Gui;
import it.polimi.ingsw.enums.Action;
import it.polimi.ingsw.server.ConnectionMessage.ActionMessage;
import it.polimi.ingsw.server.model.AssistantCard;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.Glow;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.util.List;

/**
 * Class AssistantCardController displays the assistant card on GUI
 */
public class AssistantCardsController implements GuiController {
    private Gui gui;
    private Stage stage;
    @FXML
    private AnchorPane mainPane;
    @FXML
    private Label warning;
    private int priority = -1;

    @Override
    public void setGui(Gui gui) {
        this.gui = gui;
    }

    /**
     * Method EnableCards creates a node for each assistant card available.
     * @param cards list of AssistantCards
     */
    public void enableCards(List<AssistantCard> cards) {
        for (AssistantCard card : cards) {
            ImageView imageFrame = (ImageView) mainPane.getChildren().get(card.getPriority() + 9);
            ImageView image = (ImageView) mainPane.getChildren().get(card.getPriority() - 1);
            image.setOpacity(1);
            imageFrame.setOpacity(1);
            imageFrame.setDisable(false);
        }
    }

    /**
     * Method that sets the priority on each assistantCard .
     * @param event of type MouseEvent
     */
    public void setPriority(MouseEvent event) {
        warning.setVisible(false);
        if (priority != -1) {
            ImageView oldImage = (ImageView) mainPane.getChildren().get(priority + 9);
            oldImage.setEffect(null);
            oldImage.setOnMouseExited(this::unselect);
        }
        ImageView image = (ImageView) event.getSource();
        priority = Integer.parseInt(image.getId().substring(5));
        image.setEffect(new Glow(0.8));
        image.setOnMouseExited(null);
    }

    /**
     * This method select the object when the mouse is on it, changing the glow of the node selected.
     * @param event of tpe MouseEvent
     */
    public void select(MouseEvent event) {
        Object object = event.getSource();
        ((Node) object).setEffect(new Glow(0.8));
    }
    /**
     * This method unselect the object when the mouse is on it, changing the glow of the node selected.
     * @param event of tpe MouseEvent
     */
    public void unselect(MouseEvent event) {
        Object object = event.getSource();
        ((Node) object).setEffect(null);
    }

    /**
     * This method shows an error message
     */
    public void error() {
        warning.setText("You cannot choose this card. Please try again");
        warning.setVisible(true);
    }

    /**
     * This method allows to choose the assistant Card.
     * It sends a message with the priority chosen to the server, throw clientConnection.
     * When the player has already chosen the stage is closed.
     */
    public void choose() {
        if (priority != -1) {
            ActionMessage message = new ActionMessage();
            message.setAction(Action.CHOOSE_CHARACTER_CARD);
            message.setData(priority);
            gui.getConnection().send(message);
            ImageView image = (ImageView) mainPane.getChildren().get(priority+9);//serve a disattivare glow e a risettare exited nella carta che non è una scelta valida
            image.setEffect(null);
            image.setOnMouseExited(this::unselect);
            disableAllCards();
            stage.close();
        } else {
            warning.setText("Please select a card");
            warning.setVisible(true);
        }
    }

    /**
     * This method disables the ability to choose a card after being already chosen, by setting the visibility to false.
     */
    private void disableAllCards() {
        priority=-1;
        for (Node node : mainPane.getChildren()) {
            if (!(node instanceof Button) && ! (node instanceof Label)) {
                node.setDisable(true);
                node.setOpacity(0.4);
            }
            warning.setVisible(false);
        }
    }

    public void pressButton(MouseEvent event){
        ((Button)event.getSource()).getStyleClass().add("buttonPressed");

    }
    public void releaseButton(MouseEvent event){
        ((Button)event.getSource()).getStyleClass().clear();
        ((Button)event.getSource()).getStyleClass().add("button");
    }

    /**
     * This method sets the stage of the assistant Card.
     */
    public void setStage(Stage stage) {
        this.stage = stage;
    }
}

