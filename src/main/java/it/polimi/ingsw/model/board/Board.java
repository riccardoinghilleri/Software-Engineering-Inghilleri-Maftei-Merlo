package it.polimi.ingsw.model.board;

import com.sun.source.tree.Scope;
import it.polimi.ingsw.model.*;
import it.polimi.ingsw.model.enums.CharacterColor;

import javax.swing.event.ChangeListener;
import java.util.*;

public abstract class Board {
    private int playersNumber;
    private int natureMotherPosition;
    private Cloud[] clouds;
    private List<Island> islands;
    private School[] schools;
    private List<Student> students;
    private Professor[] professors;
    //TODO metodo int getInfluencePlayer(String Player, int islandPosition) RITORNA L'INFLUENZA DI UN PLAYER SULL'ISOLA E NON CHI HA PIU INFLUENZA

    public int getNatureMotherPosition() {
        return natureMotherPosition;
    }

    public Cloud[] getClouds() {
        return clouds;
    }

    public List<Island> getIslands() {
        return islands;
    }

    public School[] getSchools() {
        return schools;
    }

    //TODO ECCEZIONE SE NICKNAME E' SBAGLIATO
    public School getSchoolByOwner(String nickname){
        int flag=0;
        for(int i=0;i<schools.length;i++)
        {
            if(nickname.equals(schools[i].getOwner()))
                flag=1;
            break;
        }
        return schools[flag];
    }
    //TODO ECCEZIONE SE NICKNAME E' SBAGLIATO - NON SO SE SERVE
    public Professor getProfessorByColor(String color){
        return professors[CharacterColor.valueOf(color).ordinal()];

    }

    public void setClouds(){
        for(int i=0;i< clouds.length;i++){
            if(playersNumber==2|| playersNumber==4)
                clouds[i].addStudents(removeRandomStudent(3));
            else clouds[i].addStudents(removeRandomStudent(4));
        }
    }
    public void setHall(){
        for(int i=0;i<schools.length;i++){
            if(playersNumber==2|| playersNumber==4)
                schools[i].addHallStudents(removeRandomStudent(7));
            else schools[i].addHallStudents(removeRandomStudent(9));
        }
    }

    public void moveStudent(String fromSchool,int toIsland, String color){
        islands.get(toIsland).addStudent(getSchoolByOwner(fromSchool).removeHallStudent(CharacterColor.valueOf(color)));
    }

    public void moveStudent(int fromCloud,String toSchool, String color){
        getSchoolByOwner(toSchool).addHallStudents(clouds[fromCloud].removeStudents());

    }

    public void moveNatureMother(int choosenSteps)
    {
        islands.get(natureMotherPosition).setNatureMother(false);
        natureMotherPosition+=choosenSteps%islands.size();
        islands.get(natureMotherPosition).setNatureMother(true);

    }

    //TODO ECCEZIONE SE L'OWNER NON VIENE SETTATO
    public void updateProfessor(CharacterColor color){
        String owner=null;
        int max=0;
        for(School s: schools){
            if(max<s.getClassroom().get(color).size()){
                max=s.getClassroom().get(color).size();
                owner=s.getOwner();
            }
        }
        professors[color.ordinal()].setOwner(owner);

    }

    public String getInfluence(int islandPosition){
        Map<CharacterColor,List<Student>> students=islands.get(islandPosition).getStudents();
        Map<String,Integer> owners = new HashMap<>();
        for (CharacterColor c: CharacterColor.values())
        {
            String owner=professors[c.ordinal()].getOwner();
            if(owners.containsKey(owner))
                owners.replace(owner,owners.get(owner)+students.get(owner).size());
            else owners.put(owner,students.get(owner).size());
            if(owner.equals(islands.get(islandPosition).getTowers().get(0).getOwner()))
                owners.replace(owner,owners.get(owner)+students.get(owner).size()+islands.get(islandPosition).getTowers().size());
        }
        String result= owners.entrySet().stream().max((entry1, entry2) -> entry1.getValue()>entry2.getValue() ? 1 : -1).get().getKey();

        return result;
    }


    private void moveTower(String fromSchool, int toIsland){
        islands.get(toIsland).addTower(getSchoolByOwner(fromSchool).removeTower());
    }

    private void moveTower(int fromIsland,String toSchool){
        getSchoolByOwner(toSchool).restockTower(islands.get(fromIsland).removeTowers());
    }

    // TODO ECCEZIONE QUANNO IL NUMERO DI STUDENTI E' 0 --> FINE DEL GIOCO
    private List<Student> removeRandomStudent (int num){
        List<Student> result= new ArrayList<>();
        for(int i=0;i < num;i++)
        {
            result.add(students.remove(students.size()-i));
        }
        return result;
    }
     private void checkNearIsland(int islandPosition){
        int flag=0;
        if(islands.get(islandPosition).getColorTower().equals(islands.get((islandPosition+1)%islands.size()).getColorTower()));
         {
             for(CharacterColor c : CharacterColor.values())
                 islands.get(islandPosition).addStudents(islands.get((islandPosition+1)%islands.size()).getStudents().get(c));
             islands.get(islandPosition).addTowers(islands.get((islandPosition+1)%islands.size()).getTowers());
             islands.remove((islandPosition+1)%islands.size());
         }
        if(islands.get(islandPosition).getColorTower().equals(islands.get((islandPosition-1)%islands.size()).getColorTower()));
         {
             for(CharacterColor c : CharacterColor.values())
                 islands.get(islandPosition).addStudents(islands.get((islandPosition+1)%islands.size()).getStudents().get(c));
             islands.get(islandPosition).addTowers(islands.get((islandPosition+1)%islands.size()).getTowers());
             islands.remove((islandPosition-1)%islands.size());
         }


     }





}
