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
        String top_wall = Constants.cursorRight(x) + "╔══════════════╗\n";
        String middle_wall = Constants.cursorRight(x) + "╠══════════════╣\n";
        String bottom_wall = Constants.cursorRight(x) + "╚══════════════╝\n";
        String vertical_wall = "║";// PRIORITY: 10  COLOR: WHITE
        String line;
        if (color == PlayerColor.GREY)
            line = Constants.cursorRight(x) + "║ COLOR: " + this.color + "  ║\n";
        else
            line = Constants.cursorRight(x) + "║ COLOR: " + this.color + " ║\n";
        box.append(top_wall);
        line = Constants.cursorRight(x) + "║ " + this.nickname;
        box.append(line);
        box.append(" ".repeat(Math.max(0, (15 - 2 - this.nickname.length()))));
        box.append("║\n");
        box.append(middle_wall);
        box.append(line);
        box.append(middle_wall);
        if (chosenAssistantCard.getPriority() < 10)
            line = Constants.cursorRight(x) + "║ PRIORITY: " + chosenAssistantCard.getPriority() + "  ║\n";
        else
            line = Constants.cursorRight(x) + "║ PRIORITY: " + chosenAssistantCard.getPriority() + " ║\n";
        box.append(line);
        box.append(middle_wall);
        line = Constants.cursorRight(x) + "║ STEPS: " + chosenAssistantCard.getMotherNatureSteps() + "     ║\n";
        box.append(line);
        if(coins!=-1){
            box.append(middle_wall);
            if (coins < 10)
                line = Constants.cursorRight(x) + "║ COINS: " + coins + "     ║\n";
            else
                line = Constants.cursorRight(x) + "║ COINS: " + coins + "    ║\n";
            box.append(line);
        }
        box.append(bottom_wall);
        return box;
    }

}
