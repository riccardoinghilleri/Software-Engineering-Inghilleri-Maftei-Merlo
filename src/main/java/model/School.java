package model;

import model.enums.CharacterColor;
import model.enums.PlayerColor;
import it.polimi.ingsw.model.board.*;
import model.board.Board;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class School {
    private final String owner;
    private List<Student> entrance;
    private Map<CharacterColor,List<Student>> diningRoom;
    private List<Tower> towers;
    private PlayerColor playerColor;
    private Board observerBoard;
    public School(String owner, PlayerColor playerColor,Board observerBoard) {
        this.owner = owner;
        this.playerColor= playerColor;
        this.entrance=new ArrayList<>();
        this.observerBoard=observerBoard;
    }

    //---GETTER---//

    public String getOwner() {
        return owner;
    }

    public List<Student> getEntrance() {
        return entrance;
    }

    public Map<CharacterColor, List<Student>> getDiningRoom() {
        return diningRoom;
    }

    public int getTowersNumber() {
        return towers.size();
    }

    public PlayerColor getTowerColor() {
        return playerColor;
    }

    //metodo che controlla se ho un studente di un determinato colore nella entrance
    public boolean hasEntranceStudentColor(String color) {
        for(Student s : entrance) {
            if(s.getColor().toString().equals(color)) {
                return true;
            }
        }
        return false;
    }

    //---STUDENTSMOVEMENT---//

    public void addDiningRoomStudent(Student student) {
        diningRoom.get(student.getColor()).add(student);
    }

    public void addEntranceStudent(Student student) {
        entrance.add(student);

    }

    public void addEntranceStudents(List<Student> students) {
        entrance.addAll(students);

    }

    public Student removeEntranceStudent(CharacterColor studentColor) {
        //TODO lanciare un'eccezione se non è presente nella entrance uno studente del colore desiderato
        Student student = null;
        for(int i=0; i<entrance.size() && student == null; i++) {
            if(entrance.get(i).getColor().equals(studentColor)) {
                student = entrance.remove(i);
            }
        }
        return student;}

    public Student removeDiningRoomStudent(CharacterColor studentColor) {
        //TODO  LANCIARE ECC SE NON è PRESENTE UNO STUDENTE DEL COLORE DESIDERATO
        Student student= null;
        if (diningRoom.containsKey(studentColor))
        {
            student= diningRoom.get(studentColor).remove(0);
        }
        return student;
    }

    public void fromEntrancetoDiningRoom(CharacterColor studentColor)
    //TODO  LANCIARE ECC SE NON è PRESENTE UNO STUDENTE DEL COLORE DESIDERATO
    {
        addDiningRoomStudent(removeEntranceStudent(studentColor));
        observerBoard.updateProfessor(studentColor);
    }

    //---TOWERSMOVEMENT---//

    public Tower removeTower() {
        return towers.remove(towers.size()-1);
    }

    public void restockTower(List<Tower> towers) {
      //TODO se towers.isEmpty() c'è un vincitore
        this.towers.addAll(towers);

    }

    @Override
    public String toString() {
        String professors =new String() ;
        for( CharacterColor c: CharacterColor.values()){
            if(observerBoard.getProfessorByColor(c.toString()).getOwner().equals(owner)){
                professors = professors + c.toString() + "\n";
            }
        }
        String students = new String();
        for( Student s: entrance){
            students= students + s.toString() + "\t";
        }

        return
                "Owner: " +getOwner() +
                "Towers: " + towers.size() + getTowerColor() +
                "\nEntrance: " +students +
                "\nDiningRoom:" +
                "\nRED:"+ diningRoom.get(CharacterColor.RED).size() +
                "\nBLUE:" + diningRoom.get(CharacterColor.BLUE).size() +
                "\nYElLOW"+ diningRoom.get(CharacterColor.YELLOW).size() +
                 "\nPINK" + diningRoom.get(CharacterColor.PINK).size() +
                 "\nGREEN" + diningRoom.get(CharacterColor.GREEN).size() +
                 "\n" + professors;

    }
}
