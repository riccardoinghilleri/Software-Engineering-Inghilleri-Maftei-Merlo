package it.polimi.ingsw.server.model;

import it.polimi.ingsw.enums.CharacterColor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CloudTest {

   Cloud cloud;
   List<Student> studentsToAdd=new ArrayList<>();

   @BeforeEach
   void setUp() {
      cloud=new Cloud();
      studentsToAdd=new ArrayList<>();
      studentsToAdd.add(new Student(CharacterColor.PINK));
      studentsToAdd.add(new Student(CharacterColor.RED));
      studentsToAdd.add(new Student(CharacterColor.GREEN));
   }

   @Test
   public void TestingCorrectColour() {
      //TODO
   }
   @Test
   public void testAddStudents(){
      assertTrue(cloud.getStudents().isEmpty());
      cloud.addStudents(studentsToAdd);
      assertFalse(studentsToAdd.isEmpty());
      assertFalse(cloud.getStudents().isEmpty());
      assertEquals(3, cloud.getStudents().size());
      assertEquals(CharacterColor.PINK, cloud.getStudents().get(0).getColor());
      assertEquals(CharacterColor.RED, cloud.getStudents().get(1).getColor());
      assertEquals(CharacterColor.GREEN, cloud.getStudents().get(2).getColor());
   }
   @Test
   public void testRemoveStudents(){
      List<Student> removedStudents=new ArrayList<>();
      cloud.addStudents(studentsToAdd);
      removedStudents = cloud.removeStudents();
      assertTrue(cloud.getStudents().isEmpty());
      for(int i=0;i<studentsToAdd.size();i++){
         assertSame(studentsToAdd.get(i),removedStudents.get(i));
      }
   }

   @Test
   public void testToString() {
      cloud.addStudents(studentsToAdd);
      String result = "PINK RED GREEN ";
   }
}