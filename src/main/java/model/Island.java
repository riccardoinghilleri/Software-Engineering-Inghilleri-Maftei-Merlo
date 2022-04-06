package model;

import model.enums.CharacterColor;
import model.enums.PlayerColor;

import java.util.*;
import java.lang.*;

public class Island {
    private boolean hasNatureMother;
    private Map<CharacterColor, List<Student>> students;
    private List<Tower> towers;
    private boolean NoEntryTiles; // prima c'era isLocked

    public Island(boolean hasNatureMother)
    {

        this.hasNatureMother = hasNatureMother;
        NoEntryTiles=false;
        towers=new ArrayList<>();
        students=new HashMap<>();

    }


    public Island(Student firstStudent) {
        this.hasNatureMother=false;
        NoEntryTiles=false;
        towers=new ArrayList<>();
        students=new HashMap<>();
        students.put(firstStudent.getColor(), new ArrayList<>());
        students.get(firstStudent.getColor()).add(firstStudent);


    }



    //---GETTER---//

    private boolean hasNatureMother() {
        return hasNatureMother;
    }

    public Map<CharacterColor, List<Student>> getStudents() {
        return students;
    }

    public List<Tower> getTowers() {
        return towers;
    }

    public boolean isLocked() {  //forse da cambiare nome al metodo
        return NoEntryTiles;
    } //prima era isLocked

    public PlayerColor getColorTower() {
        if(!towers.isEmpty()) return towers.get(0).getColor();
        return null;
    }

    //---SETTER---//

    public void setNatureMother(boolean hasNatureMother) {
        this.hasNatureMother = hasNatureMother;
    }

    public void setLock(boolean locked) {
        NoEntryTiles = locked;
    } //forse da cambiare nome al metodo

    public void addTower(Tower tower) {
        towers.add(tower);
    }

    public void addTowers(List<Tower> towers) {
        this.towers.addAll(towers);

    }

    public void addStudent(Student student) {
        if(!(students.containsKey(student.getColor())))
        {
            this.students.put(student.getColor(), new ArrayList<Student>());
        }
        students.get(student.getColor()).add(student);
    }

    public void addStudents(List<Student> students) {
        for( Student s: students)
        {
            addStudent(s);

        }

    }

    public List<Tower> removeTowers() {
        List<Tower> tempTowers = new ArrayList<>(towers);
        towers.clear();
        return tempTowers;

        }
    @Override
    public String toString() {
        String result;
        if(towers.isEmpty()){
            result ="Towers: NONE";
        }
        else{
            result=  "Towers: " +  towers.size() +towers.get(0).getColor();
        }

        return  result +
                "\nRedStudents: " + students.get(CharacterColor.RED).size() +
                "\nBlueStudents: " + students.get(CharacterColor.BLUE).size() +
                "\nYellowStudents: " + students.get(CharacterColor.YELLOW).size() +
                "\nPinkStudents: " + students.get(CharacterColor.PINK).size() +
                "\nGreenStudents: " + students.get(CharacterColor.GREEN).size() +
                "\nNatureMother: " +hasNatureMother();

    }
}
