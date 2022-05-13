package it.polimi.ingsw.server.model;

import it.polimi.ingsw.constants.Constants;

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
        students = new ArrayList<Student>();
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

    public StringBuilder draw(int x, int y, int pos) {
        StringBuilder cloud = new StringBuilder();
        cloud.append(Constants.cursorUp(y));
        String top = "#" + pos + "• • • •  \n";
        String bottom = "  • • • •  \n";
        Constants.moveObject(cloud, x, top);
        int students_index = 0;
        for (int i = 0; i < 2; i++) {
            cloud.append(Constants.cursorRight(x));
            for (int j = 0; j < 11; j++) {
                if (!students.isEmpty()) {
                    if (j == 0 || j == 10) cloud.append("•");
                    else if (i == 0 && (j == 3 || j == 7)) {
                        cloud.append(students.get(students_index));
                        students_index++;
                    } else if (i == 1 && students.size() % 2 == 0 && (j == 3 || j == 7)) {
                        cloud.append(students.get(students_index));
                        students_index++;
                    } else if (i == 1 && students.size() % 2 != 0 && j == 5) {
                        cloud.append(students.get(students_index));
                        students_index++;
                    } else cloud.append(" ");
                } else {
                    if (j == 0 || j == 10) cloud.append("•");
                    else cloud.append(" ");
                }
            }
            cloud.append("\n");
        }
        Constants.moveObject(cloud, x, bottom);
        return cloud;
    }
}