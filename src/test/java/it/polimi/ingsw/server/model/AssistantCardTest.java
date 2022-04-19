package it.polimi.ingsw.server.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AssistantCardTest {

    @Test
    public void testGetMotherNatureSteps() {
        AssistantCard assistantCard = new AssistantCard(5,3);
        assertEquals(3,assistantCard.getMotherNatureSteps());
    }

}