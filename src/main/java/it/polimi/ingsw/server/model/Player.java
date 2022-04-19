package it.polimi.ingsw.server.model;

import it.polimi.ingsw.server.model.enums.PlayerColor;

public class Player {
    String nickname;
    int clientID;
    Deck deck;
    PlayerColor color;
    AssistantCard chosenAssistantCard;

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
        return chosenAssistantCard;
    }

    public void setColor(String color) {
        this.color=PlayerColor.valueOf(color.toUpperCase());
    }

    public void setAssistantCard(int priority){
        chosenAssistantCard=deck.removeAssistantCard(priority);
    }


}
