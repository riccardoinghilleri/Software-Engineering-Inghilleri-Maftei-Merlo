package it.polimi.ingsw.server.model;

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

    public void setClientID(int clientID) {
        this.clientID = clientID;
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

}
