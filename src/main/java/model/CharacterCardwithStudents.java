package model;

import java.util.ArrayList;
import java.util.List;
import it.polimi.ingsw.model.enums.*;
import model.enums.CharacterCardName;
import model.enums.CharacterColor;

public class CharacterCardwithStudents extends CharacterCard {
    private List<Student> students;

    public CharacterCardwithStudents(CharacterCardName name, int cost, String description, List<Student> students)
    {
        super(name, cost, description);
        this.students = new ArrayList<Student>(students);
    }

    public List<Student> getStudents() {
        return students;
    }

    public void addStudent(Student student)
    {
        students.add(student);
    }

    public Student removeStudent(CharacterColor studentColor) {
        //TODO lanciare un'eccezione se non è presente uno studente di quel colore
        Student student = null;
        for(int i=0; i<students.size() && student==null; i++) {
            if(students.get(i).getColor().equals(studentColor)) {
                student = students.remove(i);
            }
        }
        return student;
    }

}
