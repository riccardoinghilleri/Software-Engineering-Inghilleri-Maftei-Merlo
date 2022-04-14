package it.polimi.ingsw.model;

import it.polimi.ingsw.model.enums.CharacterColor;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

class CloudTest {
   @Test

   public void TestingCorrectColour() {
      //TODO
   }
   @Test
   public void testAddStudents(){
      Cloud cloud= new Cloud();
      assertTrue(cloud.getStudents().isEmpty());
      List<Student> studentsToAdd=new ArrayList<>();
      studentsToAdd.add(new Student(CharacterColor.PINK));
      studentsToAdd.add(new Student(CharacterColor.RED));
      studentsToAdd.add(new Student(CharacterColor.GREEN));
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
      Cloud cloud= new Cloud();
      CharacterColor color= CharacterColor.values()[new Random().nextInt(CharacterColor.values().length)];
      List<Student> students= new ArrayList<>();
      for(int i=0;i<4;i++)
      {
         students.add(new Student(color));
      }
      cloud.addStudents(students);
      students.clear();
      assertTrue(students.isEmpty());
      students = cloud.removeStudents();
      assertTrue(cloud.getStudents().isEmpty());
      assertEquals(4 ,students.size());
   }






}