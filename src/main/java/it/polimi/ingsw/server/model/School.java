package it.polimi.ingsw.server.model;

import it.polimi.ingsw.constants.Constants;
import it.polimi.ingsw.enums.CharacterColor;
import it.polimi.ingsw.enums.PlayerColor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class School implements Serializable {
    private final Player owner;
    private final List<Student> entrance;
    private final Map<CharacterColor, List<Student>> diningRoom;
    private final List<Tower> towers;
    public final List<Professor> professors;
    private final PlayerColor playerColor;

    public School(Player owner, PlayerColor playerColor, int playersNumber) {
        this.owner = owner;
        this.playerColor = playerColor;
        this.entrance = new ArrayList<>();
        this.diningRoom = new HashMap<>();
        this.towers = new ArrayList<>();
        this.professors = new ArrayList<>();
        if (playersNumber == 2 || (playersNumber==4 && (owner.getClientID()==0 || owner.getClientID()==1)))
            for (int i = 0; i < 8; i++)
                towers.add(new Tower(getOwnerId(), playerColor));
        else if (playersNumber == 3)
            for (int i = 0; i < 6; i++)
                towers.add(new Tower(getOwnerId(), playerColor));
        for (CharacterColor c : CharacterColor.values()) {
            this.diningRoom.put(c, new ArrayList<>());
        }
    }

    public School(School school) { //TODO forse meglio implementare Cloneable
        this.owner = school.getOwner();
        this.playerColor = school.getTowerColor();
        this.entrance = new ArrayList<>(school.getEntrance());
        this.diningRoom = new HashMap<>(school.getDiningRoom());
        this.towers = new ArrayList<>(school.getTowers());
        this.professors = new ArrayList<>(school.getProfessors());
    }

    //---GETTER---//


    public List<Professor> getProfessors() {
        return professors;
    }

    public List<Tower> getTowers() {
        return towers;
    }

    public Player getOwner(){
        return owner;
    }

    public int getOwnerId() {
        return owner.getClientID();
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
        for (Student s : entrance) {
            if (s.getColor().toString().equals(color)) {
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
        Student student = null;
        for (int i = 0; i < entrance.size() && student == null; i++) {
            if (entrance.get(i).getColor().equals(studentColor)) {
                student = entrance.remove(i);
            }
        }
        return student;
    }

    public Student removeDiningRoomStudent(CharacterColor studentColor) {
        //TODO  LANCIARE ECC SE NON è PRESENTE UNO STUDENTE DEL COLORE DESIDERATO
        Student student = null;
        if (diningRoom.containsKey(studentColor)) {
            student = diningRoom.get(studentColor).remove(0);
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
        return towers.remove(towers.size() - 1);
    }

    public void restockTower(List<Tower> towers) {
        //TODO se towers.isEmpty() c'è un vincitore
        this.towers.addAll(towers);

    }

    public void addProfessor(Professor professor) {
        professors.add(professor);
    }

    public Professor removeProfessor(CharacterColor color) {
        for (int i = 0; i < professors.size(); i++) {
            if (professors.get(i).getColor() == color) {
                return professors.remove(i);
            }
        }
        return null;
    }

    public Professor getProfessorByColor(CharacterColor color) {
        for (Professor professor : professors) {
            if (professor.getColor() == color) return professor;
        }
        return null;
    }

    public StringBuilder draw(int x, int y) {
        StringBuilder box = new StringBuilder();
        box.append(Constants.cursorUp(y));
        String top_wall = "╔═════════════════════════════╗\n";
        String middle_wall = "╠═══╦═══════════════════╦═╦═══╣\n";
        String bottom_wall = "╚═══╩═══════════════════╩═╩═══╝\n";
        String vertical_wall = "║";
        Constants.moveObject(box,x,top_wall);
        int entrance_index = 0;
        int diningRoom_index;
        int towers_index = 0;
        int owner_index = 0;
        box.append(Constants.cursorRight(x));
        for (int j = 0; j < 31; j++) {
            if (j == 0 || j == 30) box.append(vertical_wall);
            else if (j > (29 - owner.getNickname().length()) / 2 && owner_index < owner.getNickname().length()) {
                box.append(owner.getNickname().charAt(owner_index));
                owner_index++;
            } else box.append(" ");
        }
        box.append("\n");
        Constants.moveObject(box,x,middle_wall);
        for (int i = 1; i < 6; i++) {
            diningRoom_index = 0;
            box.append(Constants.cursorRight(x));
            for (int j = 0; j < 31; j++) {
                if (j == 0 || j == 4 || j == 24 || j == 26 || j == 30)
                    box.append(vertical_wall);
                else if ((j == 1 || j == 3) && entrance_index < entrance.size()) {
                    box.append(entrance.get(entrance_index));
                    entrance_index++;
                } else if ((j > 4 && j < 24 && j % 2 != 0)
                        && diningRoom_index < diningRoom.get(CharacterColor.values()[i - 1]).size()) {
                    box.append(diningRoom.get(CharacterColor.values()[i - 1]).get(diningRoom_index));
                    diningRoom_index++;
                } else if (j == 25 && getProfessorByColor(CharacterColor.values()[i - 1]) != null) {
                    box.append(getProfessorByColor(CharacterColor.values()[i - 1]));
                } else if (j > 26 && j % 2 != 0 && towers_index < towers.size()) {
                    box.append(towers.get(towers_index));
                    towers_index++;
                } else box.append(" ");
            }
            box.append("\n");
        }
        Constants.moveObject(box,x,bottom_wall);
        return box;
    }
}
