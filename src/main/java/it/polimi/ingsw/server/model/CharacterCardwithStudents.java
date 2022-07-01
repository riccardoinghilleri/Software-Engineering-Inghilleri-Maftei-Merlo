package it.polimi.ingsw.server.model;

import java.util.ArrayList;
import java.util.List;
import it.polimi.ingsw.enums.CharacterCardName;
import it.polimi.ingsw.enums.CharacterColor;

/**
 * This class extends the CharacterCard class.
 * It has a list of additional students which the player can move when using the CharacterCard.
 */

public class CharacterCardwithStudents extends CharacterCard {
    private final List<Student> students;

    /**
     * Constructor that call the superConstructor for name, cost and description.
     * @param name: name of the card
     * @param cost : usual cost of the characterCard to be played
     * @param description: description of the CharacterCard power.
     * @param students : list of students on the card.
     */
    public CharacterCardwithStudents(CharacterCardName name, int cost, String description, List<Student> students)
    {
        super(name, cost, description);
        this.students = new ArrayList<>(students);
    }

    /**
     * Getter of students
     * @return the list of the students.
     */
    public List<Student> getStudents() {
        return students;
    }

    /**
     * This method restocks a student on the card after it being played
     * since the rules require the player to add from 1 to 3 students
     * on the card.
     * @param student represents the student to be added to the list of students on the Character card.
     */
    public void addStudent(Student student)
    {
        students.add(student);
    }

    /**
     * It removes a student.
     * @param studentColor the color of the student to be removed from the card and then moved to another place.
     * @return the chosen Student
     */
    public Student removeStudent(CharacterColor studentColor) {
        Student student = null;
        for(int i=0; i<students.size() && student==null; i++) {
            if(students.get(i).getColor().equals(studentColor)) {
                student = students.remove(i);
            }
        }
        return student;
    }
}
