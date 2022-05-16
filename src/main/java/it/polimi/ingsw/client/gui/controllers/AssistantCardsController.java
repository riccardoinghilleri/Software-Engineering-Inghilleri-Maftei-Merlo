package it.polimi.ingsw.client.gui.controllers;

import it.polimi.ingsw.client.gui.Gui;
import it.polimi.ingsw.enums.Action;
import it.polimi.ingsw.server.ConnectionMessage.ActionMessage;
import it.polimi.ingsw.server.model.AssistantCard;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import java.util.List;

public class AssistantCardsController implements GuiController {
    private Gui gui;
    @FXML
    private AnchorPane mainPane;
    private int priority = -1;

    @Override
    public void setGui(Gui gui) {
        this.gui = gui;
    }

    public void enableCard(List<AssistantCard> cards) {
        for (AssistantCard card : cards) {
            ImageView image = (ImageView) mainPane.getChildren().get(card.getPriority());
            image.setDisable(true);
            image.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    setPriority(event);
                    event.consume();
                }
            });
            image.addEventHandler(MouseEvent.MOUSE_ENTERED, new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    select(event);
                    event.consume();
                }
            });
            image.addEventHandler(MouseEvent.MOUSE_EXITED, new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    unselect(event);
                    event.consume();
                }
            });

        }
    }

    public void setPriority(MouseEvent event) {
        ImageView image = (ImageView) event.getSource();
        priority = Integer.parseInt(image.getId());
    }

    public void select(MouseEvent event) {
        ImageView image = (ImageView) event.getSource();
        image.setOpacity(0.9);
    }

    public void unselect(MouseEvent event) {
        ImageView image = (ImageView) event.getSource();
        image.setOpacity(1);
    }

    public void choose() {
        if (priority != -1) {
            ActionMessage message = new ActionMessage();
            message.setAction(Action.CHOOSE_CHARACTER_CARD);
            message.setData(priority);
            gui.getConnection().send(message);
        }
    }
}

