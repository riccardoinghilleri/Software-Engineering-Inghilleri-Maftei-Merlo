package it.polimi.ingsw.server.model;

import it.polimi.ingsw.constants.Constants;
import it.polimi.ingsw.enums.PlayerColor;

public class Player {
    String nickname;
    int clientID;
    Deck deck;
    PlayerColor color;
    AssistantCard chosenAssistantCard;

    public Player(String nickname, int clientID) {
        this.nickname = nickname;
        this.clientID = clientID;
        deck = new Deck();
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

    public AssistantCard getChosenAssistantCard() {
        return chosenAssistantCard;
    }

    public void setColor(String color) {
        this.color = PlayerColor.valueOf(color.toUpperCase());
    }

    public void setAssistantCard(int priority) {
        chosenAssistantCard = deck.removeAssistantCard(priority);
    }

    public StringBuilder draw(int x, int y, int coins) {
        StringBuilder box = new StringBuilder();
        box.append(Constants.cursorUp(y));
        String top_wall ="╔══════════════╗\n";
        String middle_wall ="╠══════════════╣\n";
        String bottom_wall ="╚══════════════╝\n";
        String line;
        Constants.moveObject(box,x,top_wall);
        line = "║ " + this.nickname;
        Constants.moveObject(box,x,line);
        box.append(" ".repeat(Math.max(0, (15 - 2 - this.nickname.length()))));
        box.append("║\n");
        Constants.moveObject(box,x,middle_wall);
        if (color == PlayerColor.GREY)
            line = "║ COLOR: " + this.color + "  ║\n";
        else
            line = "║ COLOR: " + this.color + " ║\n";
        Constants.moveObject(box,x,line);
        Constants.moveObject(box,x,middle_wall);
        if (chosenAssistantCard.getPriority() < 10)
            line = "║ PRIORITY: " + chosenAssistantCard.getPriority() + "  ║\n";
        else
            line = "║ PRIORITY: " + chosenAssistantCard.getPriority() + " ║\n";
        Constants.moveObject(box,x,line);
        Constants.moveObject(box,x,middle_wall);
        line = "║ STEPS: " + chosenAssistantCard.getMotherNatureSteps() + "     ║\n";
        Constants.moveObject(box,x,line);
        if(coins!=-1){
            Constants.moveObject(box,x,middle_wall);
            if (coins < 10)
                line = "║ COINS: " + coins + "     ║\n";
            else
                line = "║ COINS: " + coins + "    ║\n";
            Constants.moveObject(box,x,line);
        }
        Constants.moveObject(box,x,bottom_wall);
        return box;
    }

}
