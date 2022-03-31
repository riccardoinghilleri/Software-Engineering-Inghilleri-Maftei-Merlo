package it.polimi.ingsw.model;

import it.polimi.ingsw.model.enums.CharacterColor;
import it.polimi.ingsw.model.enums.PlayerColor;
import it.polimi.ingsw.model.board.*;
import java.util.List;
import java.util.Map;

public class School {
    private final String owner;
    private List<Student> hall;
    private Map<CharacterColor,List<Student>> classroom;
    private List<Tower> towers;
    private PlayerColor playerColor;

    public School(String owner, PlayerColor playerColor, List<Student> hall, Board observerBoard) {
        this.owner = owner;
        //...
    }

    //---GETTER---//

    public String getOwner() {
        return owner;
    }

    public List<Student> getHall() {
        return hall;
    }

    public Map<CharacterColor, List<Student>> getClassroom() {
        return classroom;
    }

    public int getTowersNumber() {
        return towers.size();
    }

    public PlayerColor getTowerColor() {
        return playerColor;
    }

    //---STUDENTSMOVEMENT---//

    public void fromHalltoClassroom(CharacterColor studentColor) {

    }

    public void addClassroomStudent(Student student) {

    }

    public void addHallStudent(Student student) {

    }

    public void addHallStudents(List<Student> students) {

    }

    public Student removeHallStudent(CharacterColor studentColor) {
        //TODO lanciare un'eccezione se non è presente nella hall uno studente del colore desiderato
        Student student = null;
        for(int i=0; i<hall.size() && student == null; i++) {
            if(hall.get(i).getColor().equals(studentColor)) {
                student = hall.remove(i);
            }
        }
        return student;
    }

    public Student removeClassroomStudent(CharacterColor studentColor) {
        return hall.get(0); //TODO da fare, è sbagliato
    }

    //---TOWERSMOVEMENT---//

    public Tower removeTower() {
        return towers.remove(towers.size()-1);
    }

    public void restockTower(List<Tower> towers) {

    }

    private void notifyBoard() {

    }

}
