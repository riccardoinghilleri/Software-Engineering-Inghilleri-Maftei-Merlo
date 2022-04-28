package it.polimi.ingsw.server.model;

import java.util.ArrayList;
import java.util.List;

public class Cloud {
    private final List<Student> students;

    public Cloud() {
        students = new ArrayList<Student>();
    }

    public List<Student> getStudents() {
        return students;
    }

    public void addStudents(List<Student> students) {
        if (this.students.isEmpty()) {
            this.students.addAll(students);
        }
    }

    public List<Student> removeStudents() {

        List<Student> tempStudents = new ArrayList<>(students);
        students.clear();
        return tempStudents;
    }

    @Override
    public String toString() {
        String result = ""; //TODO sistemare indice da stampare
        for (Student s : students) {
            result = result.concat(s.toString() + " ");
        }
        return result;
    }
}
