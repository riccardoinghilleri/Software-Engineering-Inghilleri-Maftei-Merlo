package it.polimi.ingsw.model;

import it.polimi.ingsw.model.enums.CharacterCardName;

public class CharacterCard {
    private int cost;
    private final String description;
    private final CharacterCardName name;

    public CharacterCard(CharacterCardName name, int cost, String description)
    {
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

    public void updateCost ()
    {
        this.cost++;
    }
}
