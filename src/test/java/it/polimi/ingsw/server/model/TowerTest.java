package it.polimi.ingsw.server.model;

import it.polimi.ingsw.constants.Constants;
import it.polimi.ingsw.enums.PlayerColor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

class TowerTest {

    PlayerColor color;
    Tower tower;
    @BeforeEach
    void setUp() {
        color= PlayerColor.values()[new Random().nextInt(PlayerColor.values().length)];
        tower= new Tower(1, color);

    }

    @Test
    public void CorrectNameAndColor(){
        int owner=1;
        PlayerColor color= PlayerColor.values()[new Random().nextInt(PlayerColor.values().length)];
        Tower tower= new Tower(1, color);
        assertEquals(owner, tower.getOwner());
        assertEquals(color, tower.getColor());
    }

    @Test
    public void testToString(){
        assertEquals(Constants.getAnsi(color)+"Д"+Constants.ANSI_RESET,tower.toString());
    }
}