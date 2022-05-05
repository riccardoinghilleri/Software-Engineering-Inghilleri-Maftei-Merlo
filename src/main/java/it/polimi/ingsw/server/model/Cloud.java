package it.polimi.ingsw.server.model;

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

    public StringBuilder draw() {
        StringBuilder cloud = new StringBuilder();
        String top_bottom= "  • • • •  \n";
        cloud.append(top_bottom);
        int students_index=0;
        for(int i=0;i<2;i++) {
            for(int j=0;j<11;j++){
                if(j==0 || j==10) cloud.append("•");
                else if(i==0 && (j==3 || j==7)){
                    cloud.append(students.get(students_index));
                    students_index++;
                }
                else if(i==1 && students.size()%2==0 && (j==3 || j==7)){
                    cloud.append(students.get(students_index));
                    students_index++;
                } else if (i==1 && students.size()%2!=0 && j==5) {
                    cloud.append(students.get(students_index));
                    students_index++;
                }
                else cloud.append(" ");
            }
            cloud.append("\n");
        }
        cloud.append(top_bottom);
        return cloud;
    }
}


/*
                 ◌  ◌
               ◌ R  R ◌
               ◌ R    ◌
                 ◌  ◌

                •  •  •
              •  ●   ●  •
              •    ●    •
                •  •  •

                • • • •
              •  ●   ●  •
              •    ●    •
                • • • •







 */
