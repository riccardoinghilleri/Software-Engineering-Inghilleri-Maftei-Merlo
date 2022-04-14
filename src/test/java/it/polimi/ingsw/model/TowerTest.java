package it.polimi.ingsw.model;

import it.polimi.ingsw.model.enums.CharacterColor;
import it.polimi.ingsw.model.enums.PlayerColor;
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