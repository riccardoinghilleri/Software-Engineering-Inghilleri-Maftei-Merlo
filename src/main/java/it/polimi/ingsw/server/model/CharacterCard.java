package it.polimi.ingsw.server.model;

import it.polimi.ingsw.enums.CharacterCardName;

import java.io.Serializable;

public class CharacterCard implements Serializable {
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

    @Override
    public String toString() {
        return this.name + ": " + this.description + "\nCost: " + this.cost;
    }
}
