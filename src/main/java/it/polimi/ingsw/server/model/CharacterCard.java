package it.polimi.ingsw.server.model;

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

    public StringBuilder draw() {
        StringBuilder card = new StringBuilder();
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
        card.append(top_wall);
        for (int j = 0; j < 14; j++) {
            if (j == 0 || j == 13) card.append(vertical);
            else if (j > (12-name.toString().length())/2 && name_index < name.toString().length()) {
                card.append(name.toString().charAt(name_index));
                name_index++;
            } else card.append(" ");
        }
        card.append("\n");
        card.append(middle_wall);
        card.append(cost);
        card.append(middle_wall);
        card.append(empty);
        card.append(bottom_wall);
        return card;
    }

    @Override
    public String toString() {
        return this.name + ": " + this.description + "\nCost: " + this.cost;
    }
}
