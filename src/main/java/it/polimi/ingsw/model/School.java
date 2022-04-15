package it.polimi.ingsw.model;

import it.polimi.ingsw.model.enums.CharacterColor;
import it.polimi.ingsw.model.enums.PlayerColor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class School {
    private final String owner;
    private final List<Student> entrance;
    private final Map<CharacterColor,List<Student>> diningRoom;
    private final List<Tower> towers;
    private final PlayerColor playerColor;

    public School(String owner, PlayerColor playerColor,int playersNumber) {
        this.owner = owner;
        this.playerColor= playerColor;
        this.entrance=new ArrayList<>();
        this.diningRoom=new HashMap<>();
        this.towers=new ArrayList<>();
        if(playersNumber==2)
            for(int i=0;i<8;i++)
            towers.add(new Tower(owner,playerColor));
        else if (playersNumber==3)
            for(int i=0;i<6;i++)
            towers.add(new Tower(owner,playerColor));
        for(CharacterColor c : CharacterColor.values())
        {
            this.diningRoom.put(c, new ArrayList<Student>());
        }
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
        String students = "";
        for( Student s: entrance){
            students= students.concat(s.toString() + "\t");
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
                 "\nGREEN" + diningRoom.get(CharacterColor.GREEN).size();
    }
}
