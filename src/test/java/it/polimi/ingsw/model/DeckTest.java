package it.polimi.ingsw.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DeckTest {

    @Test
    public void testGetAssistantCard() {
        Deck deck = new Deck();
        assertEquals(10,deck.getAssistantCards().size());
    }

}