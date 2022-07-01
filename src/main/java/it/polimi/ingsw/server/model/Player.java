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
     * The constructor of the class Player
     *
     * @param nickname nickname chosen by the player
     * @param clientID id assigned by the gameHandler to the player
     */
    public Player(String nickname, int clientID) {
        this.nickname = nickname;
        this.clientID = clientID;
        this.deck = new Deck();
        this.color = null;
        this.chosenAssistantCard = null;
    }

    /**
     * @return the nickname of the player
     */
    public String getNickname() {
        return nickname;
    }

    /**
     * @return the ID of the player
     */
    public int getClientID() {
        return clientID;
    }

    /**
     * @return the PlayerColor chosen by the player.
     */
    public PlayerColor getColor() {
        return color;
    }

    /**
     * @return player's deck.
     */
    public Deck getDeck() {
        return deck;
    }

    /**
     * @return the assistant card chosen by the player.
     */
    public AssistantCard getChosenAssistantCard() {
        return chosenAssistantCard;
    }

    /**
     * This method set the player's id
     *
     * @param clientID id to be assigned to the player.
     */
    public void setClientID(int clientID) {
        this.clientID = clientID;
    }

    /**
     * Method setColor sets the color chosen to the player.
     *
     * @param color color chosen by the player.
     */
    public void setColor(String color) {
        this.color = PlayerColor.valueOf(color.toUpperCase());
    }

    /**
     * Method setAssistantCard assigns the card to the player ,by removing it
     * from the player's deck.
     *
     * @param priority priority of the chosen AssistantCard.
     */
    public void setAssistantCard(int priority) {
        if (priority == -1) {
            this.chosenAssistantCard = null;
        } else chosenAssistantCard = deck.removeAssistantCard(priority);
    }

}
