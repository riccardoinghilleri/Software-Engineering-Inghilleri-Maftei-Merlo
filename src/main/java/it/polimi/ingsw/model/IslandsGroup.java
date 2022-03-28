package it.polimi.ingsw.model;

import it.polimi.ingsw.model.enums.PlayerColor;

import java.util.List;

public class IslandsGroup extends Island {
    private List<Tower> towers;
    private List<Island> islands;

    public IslandsGroup(List<Island> islands) {

    }

    public List<Tower> getTowers() {
        return towers;
    }

    public void addIsland(Island island) {
        islands.add(island);
    }

    public List<Tower> removeTowers() {
        List<Tower> tempTowers= towers;
        towers.clear();
        return tempTowers;
    }

    @Override //Non sono sicuro sia un override
    public PlayerColor getColorTower() {
        return towers.get(0).getColor();
    }

}
