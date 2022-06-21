package it.polimi.ingsw.server.model;

import it.polimi.ingsw.constants.Constants;
import it.polimi.ingsw.enums.CharacterCardName;

import java.io.Serializable;

public class CharacterCard implements Serializable {
    private int cost;
    private final String description;
    private final CharacterCardName name;

    public CharacterCard(CharacterCardName name, int cost, String description) {
        this.name = name;
        this.cost = cost;
        this.description = description;
    }

    public CharacterCardName getName() {
        return name;
    }

    public int getCost() {
        return cost;
    }

    public String getDescription() {
        return description;
    }

    public void updateCost() {
        this.cost++;
    }
/*
    public StringBuilder draw(int x, int y) {
        StringBuilder card = new StringBuilder();
        card.append(Constants.cursorUp(y));
        String top_wall = "╒════════════╕\n";//14
        String middle_wall = "├────────────┤\n";
        String bottom_wall = "╘════════════╛\n";
        String vertical = "│";
        String cost;
        String empty= vertical+"            "+vertical+"\n";
        if (this.cost < 10)
            cost = vertical + "  COST: " + this.cost + "   " + vertical+"\n";
        else cost = vertical + "  COST: " + this.cost + "  " + vertical+"\n";
        int name_index = 0;
        Constants.moveObject(card,x,top_wall);
        card.append(Constants.cursorRight(x));
        for (int j = 0; j < 14; j++) {
            if (j == 0 || j == 13) card.append(vertical);
            else if (j > (12-name.toString().length())/2 && name_index < name.toString().length()) {
                card.append(name.toString().charAt(name_index));
                name_index++;
            } else card.append(" ");
        }
        card.append("\n");
        Constants.moveObject(card,x,middle_wall);
        Constants.moveObject(card,x,cost);
        Constants.moveObject(card,x,middle_wall);
        Constants.moveObject(card,x,empty);
        Constants.moveObject(card,x,bottom_wall);
        return card;
    }
*/
    @Override
    public String toString() {
        return this.name + ": " + this.description + "\nCost: " + this.cost;
    }
}
