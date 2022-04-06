package model;

import model.enums.PlayerColor;

public class Tower {
    private final PlayerColor color;
    private final String owner;

    public Tower(String owner, PlayerColor color) {
        this.owner=owner;
        this.color = color;
    }

    public String getOwner() {
        return owner;
    }

    public PlayerColor getColor() {
        return color;
    }
}
