package it.polimi.ingsw.model;

import it.polimi.ingsw.model.enums.PlayerColor;

public class Tower {
    private final PlayerColor color;

    public Tower(PlayerColor color) {
        this.color = color;
    }

    public PlayerColor getColor() {
        return color;
    }
}
