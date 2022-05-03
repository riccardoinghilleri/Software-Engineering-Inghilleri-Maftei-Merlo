package it.polimi.ingsw.server.model;

import it.polimi.ingsw.enums.PlayerColor;
import org.junit.jupiter.api.Test;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

class TowerTest {

    @Test
    public void CorrectNameAndColor(){
        String owner="player1";
        PlayerColor color= PlayerColor.values()[new Random().nextInt(PlayerColor.values().length)];
        Tower tower= new Tower(owner, color);
        assertEquals(owner, tower.getOwner());
        assertEquals(color, tower.getColor());
    }


}