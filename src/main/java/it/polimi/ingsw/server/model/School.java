package it.polimi.ingsw.server.model;

import it.polimi.ingsw.constants.Constants;
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

    /**
     * This method is the constructor.
     * It initializes the entrance with the correct number of towers according
     * to the nummber of players.
     * It initializes the diningRoom
     */
    public School(Player owner, PlayerColor playerColor, int playersNumber) {
        this.owner = owner;
        this.playerColor = playerColor;
        this.entrance = new ArrayList<>();
        this.diningRoom = new HashMap<>();
        this.towers = new ArrayList<>();
        this.professors = new ArrayList<>();
        if (playersNumber == 2)
            for (int i = 0; i < 8; i++)
                towers.add(new Tower(getOwnerId(), playerColor));
        else if (playersNumber == 3)
            for (int i = 0; i < 6; i++)
                towers.add(new Tower(getOwnerId(), playerColor));
        for (CharacterColor c : CharacterColor.values()) {
            this.diningRoom.put(c, new ArrayList<Student>());
        }
    }

    /**
     * //TODO a cosa serve questo metodo? asked by dani
     * @param school
     */
    public School(School school) { //TODO forse meglio implementare Cloneable
        this.owner = school.getOwner();
        this.playerColor = school.getTowerColor();
        this.entrance = new ArrayList<>(school.getEntrance());
        this.diningRoom = new HashMap<>(school.getDiningRoom());
        this.towers = new ArrayList<>(school.getTowers());
        this.professors = new ArrayList<>(school.getProfessors());
    }

    //---GETTER---//

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

    //---STUDENTSMOVEMENT---//

    /**
     * This method adds a students to the map of students of the dining room
     * @param student is the instance of the student that is about to be added
     */

    public void addDiningRoomStudent(Student student) {
        diningRoom.get(student.getColor()).add(student);
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
     * @param students is the instance of students list
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
        return student;
    }

    /**
     * This method moves a student from the entrance to the diningroom,
     * throw the use of 'removeEntranceStudent' and 'addDiningRoomStudent'.
     * It throws an exception if there is not a student of that color.
     * @param studentColor
     */
    public void fromEntrancetoDiningRoom(CharacterColor studentColor)
    //TODO  LANCIARE ECC SE NON è PRESENTE UNO STUDENTE DEL COLORE DESIDERATO
    {
        addDiningRoomStudent(removeEntranceStudent(studentColor));
    }

    //---TOWERSMOVEMENT---//

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
     * This method adds the professor passed as parameter to the list of professor
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
     * This methods finds the first professor in the list of professors
     * with the specified color
     */
    public Professor getProfessorByColor(CharacterColor color) {
        for (Professor professor : professors) {
            if (professor.getColor() == color) return professor;
        }
        return null;
    }

    /**
     * This method displays the school on the screen.
     * @param x
     * @param y
     * @return a StringBuilder
     */
    public StringBuilder draw(int x, int y) {
        StringBuilder box = new StringBuilder();
        box.append(Constants.cursorUp(y));
        String top_wall = "╔═════════════════════════════╗\n";
        String middle_wall = "╠═══╦═══════════════════╦═╦═══╣\n";
        String bottom_wall = "╚═══╩═══════════════════╩═╩═══╝\n";
        String vertical_wall = "║";
        Constants.moveObject(box,x,top_wall);
        int entrance_index = 0;
        int diningRoom_index = 0;
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
