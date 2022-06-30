package it.polimi.ingsw.server.model;

import it.polimi.ingsw.enums.CharacterColor;
import it.polimi.ingsw.enums.PlayerColor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This class represents the element 'school' on the board.
 * It has a map of students in the diningRoom, a list of towers left on the class , list of professors
 * and also a list of students in the entrance.
 * It manages all the possible movements that students, towers and professors can do in the school.
 */
public class School implements Serializable {
    private final Player owner;
    private final List<Student> entrance;
    private final Map<CharacterColor, List<Student>> diningRoom;
    private final List<Tower> towers;
    public final List<Professor> professors;
    private final PlayerColor playerColor;

    private int numDiningRoomStudents;

    /**
     * This method is the constructor.
     * It initializes the entrance with the correct number of towers according
     * to the number of players.
     * It initializes the diningRoom.
     */
    public School(Player owner, PlayerColor playerColor, int playersNumber) {
        this.owner = owner;
        this.playerColor = playerColor;
        this.entrance = new ArrayList<>();
        this.diningRoom = new HashMap<>();
        this.towers = new ArrayList<>();
        this.professors = new ArrayList<>();
        this.numDiningRoomStudents=0;
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

    /**
     * Method of the class which requires a instance of the school itself.
     * It collects all the data of an already existing school
     * @param school instance of school.
     */
    public School(School school) { //TODO forse meglio implementare Cloneable
        this.owner = school.getOwner();
        this.playerColor = school.getTowerColor();
        this.entrance = new ArrayList<>(school.getEntrance());
        this.diningRoom = new HashMap<>(school.getDiningRoom());
        this.towers = new ArrayList<>(school.getTowers());
        this.professors = new ArrayList<>(school.getProfessors());
        this.numDiningRoomStudents= school.getNumDiningRoomStudents();
    }


    /**
     * @return a list of all professors in the school.
     */
    public List<Professor> getProfessors() {
        return professors;
    }

    /**
     * @return a list of all towers in the school.
     */
    public List<Tower> getTowers() {
        return towers;
    }

    /**
     * @return the owner of the school, so a Player.
     */
    public Player getOwner(){
        return owner;
    }

    /**
     * @return the owner of the school, throw the ClientId.
     */
    public int getOwnerId() {
        return owner.getClientID();
    }

    public int getNumDiningRoomStudents(){
        return numDiningRoomStudents;
    }

    /**
     * @return the list of the students left in the entrance.
     */
    public List<Student> getEntrance() {
        return entrance;
    }

    /**
     * @return the map of the students in the dining room
     */
    public Map<CharacterColor, List<Student>> getDiningRoom() {
        return diningRoom;
    }

    /**
     * @return the number of the towers left in the school
     */
    public int getTowersNumber() {
        return towers.size();
    }

    /**
     * The tower color is the same as the player color.
     * @return the player color
     */
    public PlayerColor getTowerColor() {
        return playerColor;
    }

    /**
     * Method hasEntranceStudentColor checks if the entrance has a student of the specified color
     * @return true if there is a student as required, false vice-versa
     */
    public boolean hasEntranceStudentColor(String color) {
        for (Student s : entrance) {
            if (s.getColor().toString().equals(color)) {
                return true;
            }
        }
        return false;
    }

    /**
     * This method adds a student to the map of students of the dining room
     * @param student is the instance of the student that is about to be added
     */

    public void addDiningRoomStudent(Student student) {
        diningRoom.get(student.getColor()).add(student);
        numDiningRoomStudents++;
    }
    /**
     * This method adds a single student to the list of students of the entrance
     * @param student is the instance of the student that is about to be moved
     */
    public void addEntranceStudent(Student student) {
        entrance.add(student);
    }
    /**
     * This method adds an entire list of students to the entrance
     * @param students is the instance of students list.
     */
    public void addEntranceStudents(List<Student> students) {
        entrance.addAll(students);
    }
    /**
     * This method removes a student of the specified color in the parameter 'studentColor' from the entrance.
     * @return a student
     */
    public Student removeEntranceStudent(CharacterColor studentColor) {
        Student student = null;
        for (int i = 0; i < entrance.size() && student == null; i++) {
            if (entrance.get(i).getColor().equals(studentColor)) {
                student = entrance.remove(i);
            }
        }
        return student;
    }
    /**
     * This method removes a student of the specified color in the parameter 'studentColor' from the dining room.
     * It throws an exception if there is not a student of that color.
     * @return a student
     */
    public Student removeDiningRoomStudent(CharacterColor studentColor) {
        //TODO  LANCIARE ECC SE NON è PRESENTE UNO STUDENTE DEL COLORE DESIDERATO
        Student student = null;
        if (diningRoom.containsKey(studentColor)) {
            student = diningRoom.get(studentColor).remove(0);
        }
        numDiningRoomStudents--;
        return student;
    }

    /**
     * This method moves a student from the entrance to the diningRoom,
     * throw the use of 'removeEntranceStudent' and 'addDiningRoomStudent'.
     * It throws an exception if there is not a student of that color.
     * @param studentColor color of the student
     */
    public void fromEntrancetoDiningRoom(CharacterColor studentColor)
    //TODO  LANCIARE ECC SE NON è PRESENTE UNO STUDENTE DEL COLORE DESIDERATO
    {
        addDiningRoomStudent(removeEntranceStudent(studentColor));
    }

    /**
     * This method removes a tower from the list of towers in the school.
     * @return a tower
     */
    public Tower removeTower() {
        return towers.remove(towers.size() - 1);
    }

    /**
     * This method refills the school with a list of towers.
     */
    public void restockTower(List<Tower> towers) {
        //TODO se towers.isEmpty() c'è un vincitore
        this.towers.addAll(towers);

    }

    /**
     * This method adds the professor passed as parameter to the list of professor.
     */
    public void addProfessor(Professor professor) {
        professors.add(professor);
    }

    /**
     * This method remove the first professor of the specified color in the input from the list of professors.
     */
    public Professor removeProfessor(CharacterColor color) {
        for (int i = 0; i < professors.size(); i++) {
            if (professors.get(i).getColor() == color) {
                return professors.remove(i);
            }
        }
        return null;
    }

    /**
     * This method finds the first professor in the list of professors with the specified color.
     */
    public Professor getProfessorByColor(CharacterColor color) {
        for (Professor professor : professors) {
            if (professor.getColor() == color) return professor;
        }
        return null;
    }
}
