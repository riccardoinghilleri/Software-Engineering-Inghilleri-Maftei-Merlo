package it.polimi.ingsw.server.model;

import it.polimi.ingsw.enums.CharacterColor;
import it.polimi.ingsw.enums.PlayerColor;
import it.polimi.ingsw.constants.Constants;

import java.io.Serializable;
import java.util.*;
import java.lang.*;

public class Island implements Serializable {
    private boolean hasMotherNature;
    private Map<CharacterColor, List<Student>> students;
    private List<Tower> towers;
    private boolean NoEntryTile; // prima c'era isLocked

    public Island(boolean hasMotherNature) {
        this.hasMotherNature = hasMotherNature;
        NoEntryTile = false;
        towers = new ArrayList<>();
        students = new HashMap<>();
        for (CharacterColor c : CharacterColor.values()) {
            this.students.put(c, new ArrayList<>());
        }
    }


    public Island(Student firstStudent) {
        this.hasMotherNature = false;
        NoEntryTile = false;
        towers = new ArrayList<>();
        students = new HashMap<>();
        for (CharacterColor c : CharacterColor.values()) {
            this.students.put(c, new ArrayList<>());
        }
        students.put(firstStudent.getColor(), new ArrayList<>());
        students.get(firstStudent.getColor()).add(firstStudent);
    }

    //---GETTER---//

    public boolean hasMotherNature() {
        return hasMotherNature;
    }

    public Map<CharacterColor, List<Student>> getStudents() {
        return students;
    }

    public List<Tower> getTowers() {
        return towers;
    }

    public boolean hasNoEntryTile() {  //forse da cambiare nome al metodo
        return NoEntryTile;
    } //prima era isLocked

    public PlayerColor getColorTower() {
        if (!towers.isEmpty()) return towers.get(0).getColor();
        return null;
    }

    //---SETTER---//

    public void setMotherNature(boolean hasMotherNature) {
        this.hasMotherNature = hasMotherNature;
    }

    public void setNoEntryTile(boolean locked) {
        NoEntryTile = locked;
    } //forse da cambiare nome al metodo

    public void addTower(Tower tower) {
        towers.add(tower);
    }

    public void addTowers(List<Tower> towers) {
        this.towers.addAll(towers);
    }

    public void addStudent(Student student) {
        this.students.get(student.getColor()).add(student);
    }

    public void addStudents(List<Student> students) {
        for (Student s : students) {
            addStudent(s);
        }
    }

    public List<Tower> removeTowers() {
        List<Tower> tempTowers = new ArrayList<>(towers);
        towers.clear();
        return tempTowers;

    }

    public StringBuilder draw() {
        StringBuilder box = new StringBuilder();
        int towers_index = 0;
        String horizontal_wall = "═══════════════";
        String to_right_wall = "/";
        String to_left_wall = "\\";
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 21; j++) {
                if (i == 0 || i == 5) {
                    if (j == 3) box.append(horizontal_wall);
                    else box.append(" ");
                } else if ((i == 1 && j == 1) || (i == 2 && j == 0) || (i == 3 && j == 20) || (i == 4 && j == 19)) {
                    box.append(to_right_wall);
                } else if ((i == 1 && j == 19) || (i == 2 && j == 20) || (i == 3 && j == 0) || (i == 4 && j == 1)) {
                    box.append(to_left_wall);
                } else if (i == 1) {
                    if (j == 8) {
                        box.append(Constants.getAnsi(CharacterColor.BLUE));
                        box.append(students.get(CharacterColor.BLUE).size());
                        box.append(Constants.ANSI_RESET);
                    } else if (j == 12) {
                        box.append(Constants.getAnsi(CharacterColor.PINK));
                        box.append(students.get(CharacterColor.PINK).size());
                        box.append(Constants.ANSI_RESET);
                    } else box.append(" ");
                } else if (i == 2) {
                    if (j == 6) {
                        box.append(Constants.getAnsi(CharacterColor.GREEN));
                        box.append(students.get(CharacterColor.GREEN).size());
                        box.append(Constants.ANSI_RESET);
                    } else if (j == 10) {
                        box.append(Constants.getAnsi(CharacterColor.YELLOW));
                        box.append(students.get(CharacterColor.YELLOW).size());
                        box.append(Constants.ANSI_RESET);
                    } else if (j == 14) {
                        box.append(Constants.getAnsi(CharacterColor.RED));
                        box.append(students.get(CharacterColor.RED).size());
                        box.append(Constants.ANSI_RESET);
                    } else box.append(" ");
                } else if (i == 3) {
                    if (j > 4 && j%2!=0 && j > (21 - towers.size()) / 2 && towers_index < towers.size()) {
                        box.append(towers.get(towers_index));
                        towers_index++;
                    } else box.append(" ");
                } else if (i == 4) {
                    if (j == 8 && hasMotherNature) box.append("M");
                    else if (j == 12 && hasNoEntryTile()) box.append("X");
                    else box.append(" ");
                }
            }
            box.append("\n");
        }
        return box;
    }


    @Override
    public String toString() {
        String result;
        if (towers.isEmpty()) {
            result = "Towers: NONE\nStudents: ";
        } else {
            result = "Towers: " + towers.size() + " " + towers.get(0).getColor() + "\nStudents: ";
        }
        for (CharacterColor key : students.keySet()) {
            for (Student student : students.get(key)) {
                result = result.concat(student.toString() + "\t");
            }
        }
        if (hasNoEntryTile()) result = result.concat("\nNo Entry Tile!");
        return result +
                "\nMotherNature: " + hasMotherNature();
    }
}
/*╗╝ ╦ ╩ ═║╚ ╘ ╛ ╒ ╕ ┤

        ╔════════════════════╗    ╒════════╕       ╒════════════════════╕
        ║      nickname      ║    │PRIORITY│       │     PERFROMER      │
        ╠════════════════════╣    │   1    │       ├────────────────────┤
        ║ priority :         ║    │ STEPS  │       │ DESCRIZIONE ..     │
        ╠════════════════════╣    │   2    │       │                    │
        ║ coins:             ║    ╘════════╛       │                    │
        ╚════════════════════╝                     │                    │
                                                   │                    │
                                                   ├────────────────────┤
                                                   │     ● ● ● ● ● ●    │
                                                   ╘════════════════════╛

           ════════════════
         /      B   P       \
        /     R   Y   G      \
       /                      \
       \    TOWERS:        /
        \        ­         /
         \        X         /
           ════════════════


            •  •  •
         •           •
        •   R  B  G   •
         •           •
            •  •  •    */