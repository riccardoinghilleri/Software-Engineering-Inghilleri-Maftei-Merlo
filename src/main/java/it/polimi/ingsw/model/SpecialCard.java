package it.polimi.ingsw.model;

import it.polimi.ingsw.model.enums.SpecialCardName;

public class SpecialCard {
    private int cost;
    private final String description;
    private final SpecialCardName name;

    public SpecialCard(SpecialCardName name, int cost, String description)
    {
        this.name = name;
        this.cost = cost;
        this.description = description;
    }

    public SpecialCardName getName() {
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
