package it.polimi.ingsw.server.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PlayerTest {

    @Test
    public void testGetSetColor() {
        Player player = new Player("Ricky",1);
        player.setColor("WHITE");
        assertEquals("WHITE",player.getColor().toString());
    }

    @Test
    public void setClientId() {
        Player player = new Player("Ricky",1);
        player.setClientID(2);
        assertEquals(2,player.getClientID());
    }
}