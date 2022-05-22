package it.polimi.ingsw.client.gui.controllers;

import it.polimi.ingsw.client.gui.Gui;
import it.polimi.ingsw.server.ConnectionMessage.ActionMessage;
import it.polimi.ingsw.server.model.CharacterCard;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.Glow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.util.Objects;

public class ShopController implements GuiController {

    private Stage stage;
    private Gui gui;
    private CharacterCard[] characterCards;
    private int displayedCard=0;

    private ActionMessage message;
    @FXML
    private Label characterCardName, characterCardDescription, coinsLabel;
    @FXML
    private Button buyBtn, closeBtn;

    @FXML
    private ImageView characterCardImg;

    public void setCharacterCards(CharacterCard[] characterCards) {
        this.characterCards = characterCards;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void setCoinsLabel(int coins) {
        coinsLabel.setText("x"+coins);
    }

    @Override
    public void setGui(Gui gui) {
        this.gui = gui;
    }

    public Button getBuyBtn() {
        return buyBtn;
    }

    public void setMessage(ActionMessage message) {
        this.message = message;
    }

    public void pressButton(MouseEvent event) {
        ((Button) event.getSource()).getStyleClass().add("buttonPressed");
    }

    public void releaseButton(MouseEvent event) {
        ((Button) event.getSource()).getStyleClass().clear();
        ((Button) event.getSource()).getStyleClass().add("button");
    }

    public void buy(ActionEvent event) {
        message.setCharacterCardName(characterCardName.getText().toUpperCase());
        buyBtn.setDisable(true);
        gui.getConnection().send(message);
        //stage.close(); in base alle carte
    }

    public void changeCard(ActionEvent event) {
        if (((Button) event.getSource()).getId().equals("right")) {
            displayedCard = (displayedCard + 1) % characterCards.length;
        } else if (((Button) event.getSource()).getId().equals("left")) {
            if (displayedCard == 0)
                displayedCard = characterCards.length - 1;
            else displayedCard -= 1;
        }
        createCharacterCard();
    }

    public void close() {
        if (!buyBtn.isDisable()) {
            buyBtn.setDisable(true);
        }
        message.setCharacterCardName(null);
        gui.getConnection().send(message);
        stage.close();
    }

    public void createCharacterCard(){
        characterCardName.setText(characterCards[displayedCard].getName().toString().toUpperCase());
        characterCardImg.setImage(new Image(Objects.requireNonNull(getClass()
                .getResourceAsStream("/graphics/characterCards/" + characterCards[displayedCard].getName().toString().toLowerCase() + ".jpg"))));
        characterCardDescription.setText(characterCards[displayedCard].getDescription().toUpperCase());

    }

    public void select(MouseEvent event) {
        Object object = event.getSource();
        ((Node) object).setEffect(new Glow(0.8));
    }

    public void unselect(MouseEvent event) {
        Object object = event.getSource();
        ((Node) object).setEffect(null);
    }

}