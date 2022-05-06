package it.polimi.ingsw.server.model;

import it.polimi.ingsw.constants.Constants;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Cloud implements Serializable {
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
                    cloud.append(" ");
                }
            }
            cloud.append("\n");
        }
        Constants.moveObject(cloud, x, bottom);
        return cloud;
    }
}