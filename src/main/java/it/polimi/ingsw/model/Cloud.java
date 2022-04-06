package it.polimi.ingsw.model;

import java.util.ArrayList;
import java.util.List;

public class Cloud {
    List<Student> students;

    public Cloud() {
        students = new ArrayList<Student>();
    }

    public List<Student> getStudents() {
        return students;
    }

    public void addStudents(List<Student> students) {
        if(this.students.isEmpty()) {
            this.students.addAll(students);
        }
    }

    public List<Student> removeStudents() {
        List<Student> tempStudents = students;
        students.clear();
        return tempStudents;
    }
}
