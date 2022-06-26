package it.polimi.ingsw.server.model;

import java.util.ArrayList;
import java.util.List;
import it.polimi.ingsw.enums.CharacterCardName;
import it.polimi.ingsw.enums.CharacterColor;

/**
 * This class extends the basic CharacterCard class.
 * It has a list of additional students which the player can move.
 */

public class CharacterCardwithStudents extends CharacterCard {
    private final List<Student> students;

    /**
     * Constructor calling the superConstructor for name, cost and description
     * @param name: name of the card
     * @param cost : usual cost of the characterCard to be played
     * @param description: since there 3 different cards with this extension, the description is essential for the player.
     * @param students : list of students on the card
     */
    public CharacterCardwithStudents(CharacterCardName name, int cost, String description, List<Student> students)
    {
        super(name, cost, description);
        this.students = new ArrayList<>(students);
    }

    /**
     * Getter of students
     * @return the list of the students. It is used in the removeStudent method.
     */
    public List<Student> getStudents() {
        return students;
    }

    /**
     * This method restocks a student on the card after it being played
     * since the rules require the player to add from 1 to 3 students
     * on the card.
     * @param student
     */
    public void addStudent(Student student)
    {
        students.add(student);
    }

    /**
     * It removes a student
     * @param studentColor the color of the student wanted to be removed from the card and then moved to another place
     * @return a student
     */
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
