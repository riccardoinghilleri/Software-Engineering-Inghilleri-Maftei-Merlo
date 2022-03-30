package it.polimi.ingsw.model;

import it.polimi.ingsw.model.enums.CharacterColor;
import it.polimi.ingsw.model.enums.PlayerColor;

import java.util.Locale;

public class Player {
    String nickname;
    int clientID;
    Deck deck;
    PlayerColor color;
    AssistantCard choosenAssistantCard;

    public Player(String nickname, int clientID){
        this.nickname=nickname;
        this.clientID=clientID;
        deck=new Deck();
    }

    public String getNickname() {
        return nickname;
    }

    public int getClientID() {
        return clientID;
    }

    public PlayerColor getColor() {
        return color;
    }

    public Deck getDeck() {
        return deck;
    }

    public AssistantCard getChoosenAssistantCard(){
        return choosenAssistantCard;
    }

    public void setColor(String color) {
        this.color=PlayerColor.valueOf(color.toUpperCase());
    }

    public void setAssistantCard(int priority){
        choosenAssistantCard=deck.removeAssistantCard(priority);
    }
}
