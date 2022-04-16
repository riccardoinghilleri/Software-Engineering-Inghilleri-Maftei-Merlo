package it.polimi.ingsw.model;

import it.polimi.ingsw.model.enums.CharacterCardName;
import it.polimi.ingsw.model.enums.CharacterColor;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CharacterCardWithStudentsTest {

    @Test
    //forse qualcosa in più da controllare sulla lista
    void testConstructorCharacterCardWithStudents(){
        List<Student> students= new ArrayList<>();
        CharacterCardwithStudents cardWithStudents= new CharacterCardwithStudents(CharacterCardName.QUEEN, 1, "Stringa descrizione di prova", students);
        assertEquals(CharacterCardName.QUEEN, cardWithStudents.getName());
        assertEquals(1, cardWithStudents.getCost());
        assertEquals("Stringa descrizione di prova", cardWithStudents.getDescription());
        assertTrue(students.isEmpty());
    }
   @Test
    void testAddStudent(){
       List<Student> students= new ArrayList<>();
       CharacterCardwithStudents cardWithStudents= new CharacterCardwithStudents(CharacterCardName.DINER, 2, "Stringa descrizione di prova", students);
        cardWithStudents.addStudent(new Student(CharacterColor.PINK));
       cardWithStudents.addStudent(new Student(CharacterColor.RED));
       assertEquals(2, cardWithStudents.getStudents().size());
       assertEquals(CharacterColor.PINK,cardWithStudents.getStudents().get(0).getColor());
       assertEquals(CharacterColor.RED,cardWithStudents.getStudents().get(1).getColor());

   }
   @Test
   //TODO manca eccezione nel caso il colore richiesto non sia presente
    void testRemoveStudent(){
        List<Student> students= new ArrayList<>();
        CharacterCardwithStudents cardWithStudents= new CharacterCardwithStudents(CharacterCardName.DINER, 2, "Stringa descrizione di prova", students);
        cardWithStudents.addStudent(new Student(CharacterColor.PINK));
        cardWithStudents.addStudent(new Student(CharacterColor.RED));
        cardWithStudents.addStudent(new Student(CharacterColor.RED));
        cardWithStudents.addStudent(new Student(CharacterColor.GREEN));
        assertEquals(4, cardWithStudents.getStudents().size());
        cardWithStudents.removeStudent(CharacterColor.RED);
        assertEquals(3,cardWithStudents.getStudents().size());
        assertEquals(cardWithStudents.removeStudent(CharacterColor.RED).getColor(), CharacterColor.RED);






   }
}