package it.polimi.ingsw.client.gui.controllers;

import it.polimi.ingsw.client.gui.Gui;
import it.polimi.ingsw.enums.Action;
import it.polimi.ingsw.server.ConnectionMessage.ActionMessage;
import it.polimi.ingsw.server.model.AssistantCard;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.effect.Glow;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.util.List;

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

    public void enableCards(List<AssistantCard> cards) {
        for (AssistantCard card : cards) {
            ImageView imageFrame = (ImageView) mainPane.getChildren().get(card.getPriority()+9);
            ImageView image = (ImageView) mainPane.getChildren().get(card.getPriority()-1);
            image.setOpacity(1);
            imageFrame.setOpacity(1);
            imageFrame.setDisable(false);
            /*imageFrame.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
                setPriority(event);
                event.consume();
            });
            imageFrame.addEventHandler(MouseEvent.MOUSE_ENTERED, event -> {
                select(event);
                event.consume();
            });
            imageFrame.addEventHandler(MouseEvent.MOUSE_EXITED, event -> {
                unselect(event);
                event.consume();
            });*/

        }
    }

    public void setPriority(MouseEvent event) {
        warning.setVisible(false);
        if(priority!=-1){
            ImageView oldImage=(ImageView) mainPane.getChildren().get(priority+9);
            oldImage.setEffect(null);
            /*oldImage.addEventHandler(MouseEvent.MOUSE_EXITED, evt -> {
                unselect(evt);
                evt.consume();
            });*/
            oldImage.setOnMouseExited(this::unselect);
        }
        ImageView image = (ImageView) event.getSource();
        priority = Integer.parseInt(image.getId().substring(5));
        image.setEffect(new Glow(0.8));
        image.setOnMouseExited(null);
    }

    public void select(MouseEvent event) {
        Object object =  event.getSource();
        ((Node)object).setEffect(new Glow(0.8));
    }

    public void unselect(MouseEvent event) {
        Object object =  event.getSource();
        ((Node)object).setEffect(null);
    }

    public void error(){
        warning.setText("You cannot choose this card. Please try again");
        warning.setVisible(true);
    }

    public void choose() {
        if (priority != -1) {
            ActionMessage message = new ActionMessage();
            message.setAction(Action.CHOOSE_CHARACTER_CARD);
            message.setData(priority);
            gui.getConnection().send(message);
            stage.close();
        } else {
            warning.setText("Please select a card");
            warning.setVisible(true);
        }
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }
}

