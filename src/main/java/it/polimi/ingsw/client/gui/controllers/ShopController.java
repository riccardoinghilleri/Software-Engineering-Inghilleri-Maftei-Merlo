package it.polimi.ingsw.client.gui.controllers;

import it.polimi.ingsw.client.gui.Gui;
import it.polimi.ingsw.server.ConnectionMessage.ActionMessage;
import it.polimi.ingsw.server.model.CharacterCard;
import it.polimi.ingsw.server.model.CharacterCardwithStudents;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.Glow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.util.Objects;

public class ShopController implements GuiController {

    private Stage stage;
    private Gui gui;
    private CharacterCard[] characterCards;
    private int displayedCard = 0;

    private ActionMessage message;
    @FXML
    private Label characterCardName, characterCardDescription, playerCoins,cost;
    @FXML
    private Button buyBtn;
    @FXML
    private AnchorPane studentsPane;
    @FXML
    private ImageView characterCardImg;

    private boolean enableBuyBtn;

    public void setCharacterCards(CharacterCard[] characterCards) {
        this.characterCards = characterCards;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void setCoinsLabel(int coins) {
        playerCoins.setText(String.valueOf(coins));
    }

    public void setEnableBuyBtn(boolean value){
        enableBuyBtn=value;
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

    public void buy() {
        message.setCharacterCardName(characterCardName.getText().toUpperCase());
        enableBuyBtn=false;
        buyBtn.setDisable(true);
        gui.getConnection().send(message);
        stage.close();
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
        if (enableBuyBtn) {
            enableBuyBtn=false;
            buyBtn.setDisable(true);
            message.setCharacterCardName(null);
            gui.getConnection().send(message);
            ((MainSceneController)gui.getControllerByFxmlName("mainScene.fxml")).enableShop(false);
        }
        stage.close();
    }

    public void createCharacterCard() {
        cost.setText(String.valueOf(characterCards[displayedCard].getCost()));
        characterCardName.setText(characterCards[displayedCard].getName().toString().toUpperCase());
        characterCardImg.setImage(new Image(Objects.requireNonNull(getClass()
                .getResourceAsStream("/graphics/characterCards/" + characterCards[displayedCard].getName().toString().toLowerCase() + ".jpg"))));
        characterCardDescription.setText(characterCards[displayedCard].getDescription().toUpperCase());
        if (characterCards[displayedCard] instanceof CharacterCardwithStudents) {
            studentsPane.setVisible(true);
            for (int i = 0; i < 6; i++) {
                if (i < ((CharacterCardwithStudents) characterCards[displayedCard]).getStudents().size()) {
                    String color = ((CharacterCardwithStudents) characterCards[displayedCard])
                            .getStudents().get(i).getColor().toString();
                    ((ImageView) studentsPane.getChildren().get(i)).setImage(new Image(Objects.requireNonNull(getClass()
                            .getResourceAsStream("/graphics/pieces/student_"
                                    + color.toLowerCase() + ".png"))));
                    studentsPane.getChildren().get(i + 6).setVisible(true);
                    studentsPane.getChildren().get(i).setVisible(true);
                    studentsPane.getChildren().get(i + 6).setId(color);
                } else {
                    studentsPane.getChildren().get(i + 6).setVisible(false);
                    studentsPane.getChildren().get(i).setVisible(false);
                }
            }
        } else {
            studentsPane.setVisible(false);
        }
        if(enableBuyBtn){
            buyBtn.setDisable(!(characterCards[displayedCard].getCost()<=Integer.parseInt(playerCoins.getText())));
        }
    }

    public void clear() {
        for (int i = 0; i < 12; i++) {
            studentsPane.getChildren().get(i).setVisible(false);
        }
        studentsPane.setVisible(false);
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