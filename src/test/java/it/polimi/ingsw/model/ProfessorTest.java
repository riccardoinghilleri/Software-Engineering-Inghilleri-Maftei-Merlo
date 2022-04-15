package it.polimi.ingsw.model;

import it.polimi.ingsw.model.enums.CharacterColor;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ProfessorTest {

    @Test
    void testConstructorProfessor(){
        Professor professor= new Professor(CharacterColor.YELLOW);
        assertEquals("NONE", professor.getOwner());
        assertEquals(CharacterColor.YELLOW, professor.getColor());
    }
    @Test
    void testSetOwner(){
        String owner="PlAYER 1";
        Professor professor= new Professor(CharacterColor.PINK);
        assertEquals(CharacterColor.PINK, professor.getColor());
        professor.setOwner(owner);
        assertEquals(owner, professor.getOwner());



    }
}