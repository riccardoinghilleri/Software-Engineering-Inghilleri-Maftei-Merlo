package it.polimi.ingsw.server.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * This class represents a cloud on the boardGame. It has different number of students on it,
 * according to the playersNumber.
 */

public class Cloud implements Serializable {
    private final List<Student> students;

    /**Constructor Cloud creates a new Cloud instance.*/

    public Cloud() {
        students = new ArrayList<>();
    }

    /**
     * Method getStudents returns the list of students currently on the cloud.
     */
    public List<Student> getStudents() {
        return students;
    }

    /**
     * Method addStudents check if the cloud is empty.
     * If so, it adds a list of students, refilling the cloud.
     * @param students list used to refill the cloud
     */
    public void addStudents(List<Student> students) {
        if (this.students.isEmpty()) {
            this.students.addAll(students);
        }
    }

    /**
     * Method removeStudents is called when a player chooses the cloud.
     * It removes all the students on the cloud and
     * @return a list of them
     */
    public List<Student> removeStudents() {

        List<Student> tempStudents = new ArrayList<>(students);
        students.clear();
        return tempStudents;
    }

}