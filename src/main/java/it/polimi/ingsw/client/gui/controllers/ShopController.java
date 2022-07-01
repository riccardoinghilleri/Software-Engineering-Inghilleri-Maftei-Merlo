package it.polimi.ingsw.client.gui.controllers;

import it.polimi.ingsw.client.gui.Gui;
import it.polimi.ingsw.enums.CharacterCardName;
import it.polimi.ingsw.server.ConnectionMessage.ActionMessage;
import it.polimi.ingsw.server.model.CharacterCard;
import it.polimi.ingsw.server.model.CharacterCardwithProhibitions;
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

/**
 * ShopController displays the shop of character Cards
 */
public class ShopController implements GuiController {
    private Stage stage;
    private Gui gui;
    private CharacterCard[] characterCards;
    private int displayedCard = 0;
    private ActionMessage message;
    private boolean enableBuyBtn;
    @FXML
    private Label characterCardName, characterCardDescription, playerCoins, cost;
    @FXML
    private Button buyBtn;
    @FXML
    private AnchorPane studentsPane;
    @FXML
    private ImageView characterCardImg;

    /**
     * Setter of CharacterCards
     */
    public void setCharacterCards(CharacterCard[] characterCards) {
        this.characterCards = characterCards;
    }

    /**
     * Setter of stage
     * @param stage of type Stage
     */
    public void setStage(Stage stage) {
        this.stage = stage;
    }

    /**
     * It sets the coins label to the number of coins of the player
     * @param coins number of available coins
     */
    public void setCoinsLabel(int coins) {
        playerCoins.setText(String.valueOf(coins));
    }

    /**
     * It sets to enable the Buy button
     * @param value of type boolean
     */
    public void setEnableBuyBtn(boolean value) {
        enableBuyBtn = value;
    }

    /**
     * @param message of type ActionMessage, it is the message set in the MainScene
     */
    public void setMessage(ActionMessage message) {
        this.message = message;
    }

    /**
     * Method pressButton detects when a button is pressed
     * @param event of type Mouse Event
     */
    public void pressButton(MouseEvent event) {
        ((Button) event.getSource()).getStyleClass().add("buttonPressed");
    }

    /**
     * Method pressButton detects when a button is released
     * @param event of type Mouse Event
     */
    public void releaseButton(MouseEvent event) {
        ((Button) event.getSource()).getStyleClass().clear();
        ((Button) event.getSource()).getStyleClass().add("button");
    }

    /**
     * This method manages the buy action of a CharacterCard.
     */
    public void buy() {
        message.setCharacterCardName(characterCardName.getText().toUpperCase());
        gui.getConnection().send(message);
        enableBuyBtn = false;
        buyBtn.setDisable(true);
        ((MainSceneController) gui.getControllerByFxmlName("mainScene.fxml")).enableShop(false);
        ((MainSceneController) gui.getControllerByFxmlName("mainScene.fxml")).getNoBtn().setVisible(false);
        stage.close();
    }

    /**
     * This method manages the switch between the available CharacterCards in the shop.
     * @param event of type event
     */
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

    /**
     * This method closes the Shop Stage, and it disables the buy button if it was enabled
     */
    public void close() {
        if (enableBuyBtn) {
            enableBuyBtn = false;
            buyBtn.setDisable(true);
            message.setCharacterCardName(null);
            gui.getConnection().send(message);
            ((MainSceneController) gui.getControllerByFxmlName("mainScene.fxml")).enableShop(false);
            ((MainSceneController) gui.getControllerByFxmlName("mainScene.fxml")).getNoBtn().setVisible(false);
        }
        stage.close();
    }

    /**
     * This method displays a character card with his cost, description and elements that it has.
     * It also handles the enabling of the buy button, which cannot be used in case the card cannot be nought
     */
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
        if (enableBuyBtn) {
            if (!(characterCards[displayedCard].getCost() <= Integer.parseInt(playerCoins.getText())))
                buyBtn.setDisable(true);
            else if(characterCards[displayedCard].getName()== CharacterCardName.PERFORMER) {
                buyBtn.setDisable(((MainSceneController)gui.getControllerByFxmlName("mainScene.fxml")).getDiningroomStudents()<1);
            }
            else if(characterCards[displayedCard].getName()== CharacterCardName.HERBOLARIA){
                buyBtn.setDisable(((CharacterCardwithProhibitions)characterCards[displayedCard]).getProhibitionsNumber()==0);
            }
            else buyBtn.setDisable(false);
        }
    }

    /**
     * This method sets the visibility to false of all elements of the students' pane
     */
    public void clear() {
        for (int i = 0; i < 12; i++) {
            studentsPane.getChildren().get(i).setVisible(false);
        }
        studentsPane.setVisible(false);
    }

    /**
     * This method selects the object when the mouse is on it, changing the glow of the node selected.
     * @param event of type MouseEvent
     */
    public void select(MouseEvent event) {
        Object object = event.getSource();
        ((Node) object).setEffect(new Glow(0.8));
    }

    /**
     * This method unselects the object when the mouse is on it, changing the glow of the node selected.
     * @param event of type MouseEvent
     */
    public void unselect(MouseEvent event) {
        Object object = event.getSource();
        ((Node) object).setEffect(null);
    }

    /**
     * @see GuiController
     * @param gui of type Gui - the main Gui class
     */
    @Override
    public void setGui(Gui gui) {
        this.gui = gui;
    }
}