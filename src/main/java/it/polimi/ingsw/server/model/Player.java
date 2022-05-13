package it.polimi.ingsw.server.model;

import it.polimi.ingsw.constants.Constants;
import it.polimi.ingsw.enums.PlayerColor;

import java.io.Serializable;

/**
 * This class represents a player, with his nickname, clientId, deck, color and assistantCard.
 */
public class Player implements Serializable {
    String nickname;
    int clientID;
    Deck deck;
    PlayerColor color;
    AssistantCard chosenAssistantCard;

    /**
     * The constructor of the class
     */
    public Player(String nickname, int clientID) {
        this.nickname = nickname;
        this.clientID = clientID;
        deck = new Deck();
    }

    /**
     * @return the nickname of the player considered
     */
    public String getNickname() {
        return nickname;
    }
    /**
     * @return the ID of the player considered
     */
    public int getClientID() {
        return clientID;
    }
    /**
     * @return an instance of the enum PlayerColor, corresponding to the player considered
     */
    public PlayerColor getColor() {
        return color;
    }
    /**
     * @return player's deck
     */
    public Deck getDeck() {
        return deck;
    }
    /**
     * @return the assistant card chosen by the player
     */
    public AssistantCard getChosenAssistantCard() {
        return chosenAssistantCard;
    }
    /**
     * Method setColor sets the color chosen to the player.
     */
    public void setColor(String color) {
        this.color = PlayerColor.valueOf(color.toUpperCase());
    }
    /**
     * Method setAssistantCard, receiving in input a priority, assigns the card to the player,
     * by removing it from the player's deck.
     */
    public void setAssistantCard(int priority) {
        chosenAssistantCard = deck.removeAssistantCard(priority);
    }
    /**
     *
     * @return
     */
    public StringBuilder draw(int x, int y, int coins, boolean currentPlayer) {
        StringBuilder box = new StringBuilder();
        box.append(Constants.cursorUp(y));
        String top_wall = "╔══════════════╗\n";
        String middle_wall = "╠══════════════╣\n";
        String bottom_wall = "╚══════════════╝\n";
        String line;
        Constants.moveObject(box, x, top_wall);
        if(currentPlayer) line = "║✷" + this.nickname; //TODO mettere una stellina invece dell'asterisco
        else line = "║ " + this.nickname;
        Constants.moveObject(box, x, line);
        box.append(" ".repeat(Math.max(0, (15 - 2 - this.nickname.length()))));
        box.append("║\n");
        Constants.moveObject(box, x, middle_wall);
        if (color == PlayerColor.GREY)
            line = "║ COLOR: " + this.color + "  ║\n";
        else
            line = "║ COLOR: " + this.color + " ║\n";
        Constants.moveObject(box, x, line);
        Constants.moveObject(box, x, middle_wall);
        if (chosenAssistantCard == null) {
            line = "║ PRIORITY: X  ║\n";
            Constants.moveObject(box, x, line);
            Constants.moveObject(box, x, middle_wall);
            line = "║ STEPS: X     ║\n";
            Constants.moveObject(box, x, line);
        } else {
            if (chosenAssistantCard.getPriority() < 10)
                line = "║ PRIORITY: " + chosenAssistantCard.getPriority() + "  ║\n";
            else
                line = "║ PRIORITY: " + chosenAssistantCard.getPriority() + " ║\n";
            Constants.moveObject(box, x, line);
            Constants.moveObject(box, x, middle_wall);
            line = "║ STEPS: " + chosenAssistantCard.getMotherNatureSteps() + "     ║\n";
            Constants.moveObject(box, x, line);
        }
            if (coins != -1) {
                Constants.moveObject(box, x, middle_wall);
                if (coins < 10)
                    line = "║ COINS: " + coins + "     ║\n";
                else
                    line = "║ COINS: " + coins + "    ║\n";
                Constants.moveObject(box, x, line);
            }
        Constants.moveObject(box, x, bottom_wall);
        return box;
    }

}
