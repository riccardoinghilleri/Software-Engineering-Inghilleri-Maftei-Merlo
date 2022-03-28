package it.polimi.ingsw.model;

import it.polimi.ingsw.model.enums.PlayerColor;

public class SingleIsland extends Island {
    private Tower tower;

    public SingleIsland(boolean hasNatureMother) {

    }

    public SingleIsland(Student firstStudent) {

    }

    public Tower getTower() {
        return tower;
    }

    public void setTower(Tower tower) {
        this.tower = tower;
    }

    @Override //Non sono sicuro sia un override
    public PlayerColor getColorTower() {
        if(tower != null) return tower.getColor();
        return null;
    }
}
