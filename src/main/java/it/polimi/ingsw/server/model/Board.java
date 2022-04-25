package it.polimi.ingsw.server.model;

import it.polimi.ingsw.server.model.enums.CharacterColor;
import java.util.*;


public class Board {
    private final int playersNumber;
    private int motherNaturePosition;
    private final Cloud[] clouds;
    private final List<Island> islands;
    private final School[] schools;
    private final List<Student> students;
    private final Professor[] professors;
    private final GameModel gameModel;

    //TODO metodo int getInfluencePlayer(String Player, int islandPosition) RITORNA L'INFLUENZA DI UN PLAYER SULL'ISOLA E NON CHI HA PIU INFLUENZA

    public Board(List<Player> players, GameModel gameModel) {
        this.gameModel = gameModel;
        this.playersNumber = players.size();
        this.motherNaturePosition = 0;
        this.students=new ArrayList<>();
        this.professors=new Professor[5];
        this.clouds = new Cloud[playersNumber];
        this.islands = new ArrayList<>();
        this.schools = new School[playersNumber];

        //--CREAZIONE STUDENTI--
        for (int j = 0; j< 26; j++) {
            this.students.add(new Student(CharacterColor.RED));
            this.students.add(new Student(CharacterColor.BLUE));
            this.students.add(new Student(CharacterColor.GREEN));
            this.students.add(new Student(CharacterColor.PINK));
            this.students.add(new Student(CharacterColor.YELLOW));
        }
        Collections.shuffle(students);

        //--CREAZIONE PROFESSORI--
        for (CharacterColor c : CharacterColor.values()) {
            professors[c.ordinal()] = new Professor(c);
        }

        //--CREAZIONE NUVOLE--
        for(int i=0;i<playersNumber;i++){
            clouds[i]=new Cloud();
        }

        //--CREAZIONE ISOLE--
        for (int i = 0; i < 12; i++) {
            if (i == 0)
                islands.add(new Island(true));
            else if (i == 6)
                islands.add(new Island(false));
            else
                islands.add(new Island(removeRandomStudent()));
        }

        //--CREAZIONE SCUOLE--
        for(Player p:players){
            schools[p.getClientID()-1] = new School(p.getNickname(),p.getColor(),playersNumber);
        }
        setInitialEntrance();
    }

    public int getStudentsSize() {
        return students.size();
    }

    public List<Integer> getAvailableClouds() {
        List<Integer> availableClouds = new ArrayList<>();
        for(int i=0; i< clouds.length;i++) {
            if(!clouds[i].getStudents().isEmpty()) {
                availableClouds.add(i);
            }
        }
        return availableClouds;
    }

    public int getMotherNaturePosition() {
        return motherNaturePosition;
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
    public School getSchoolByOwner(String nickname) {
        int flag=-1;
        for (int i = 0; i < schools.length && flag==-1; i++) {
            if (nickname.equalsIgnoreCase(schools[i].getOwner()))
               flag=i;
        }
        return schools[flag];
    }

    //TODO ECCEZIONE SE NICKNAME E' SBAGLIATO - NON SO SE SERVE
    public Professor getProfessorByColor(CharacterColor color) {
        return professors[color.ordinal()];
    }

    public void setStudentsonClouds() {
        for(Cloud cloud : clouds) {
            if(playersNumber==2 || playersNumber==4)
                cloud.addStudents(removeRandomStudents(3));
            else cloud.addStudents(removeRandomStudents(4));
        }
    }

    private void setInitialEntrance() {
        for(School school : schools) {
            if(playersNumber==2 || playersNumber==4)
                school.addEntranceStudents(removeRandomStudents(7));
            else school.addEntranceStudents(removeRandomStudents(9));
        }
    }

    public void moveStudent(String fromSchool, int toIsland, String color) {
        islands.get(toIsland).addStudent(getSchoolByOwner(fromSchool).removeEntranceStudent(CharacterColor.valueOf(color)));
    }

    public void moveStudent(int fromCloud, String toSchool) {
        getSchoolByOwner(toSchool).addEntranceStudents(clouds[fromCloud].removeStudents());
    }

    public void moveMotherNature(int chosenSteps) {
        islands.get(motherNaturePosition).setMotherNature(false);
        motherNaturePosition = (motherNaturePosition + chosenSteps) % islands.size();
        islands.get(motherNaturePosition).setMotherNature(true);
    }

    public void updateProfessor(CharacterColor color) {
        String owner = professors[color.ordinal()].getOwner();
        int max=0;
        if(!owner.equalsIgnoreCase("NONE")) {
            max = getSchoolByOwner(owner).getDiningRoom().get(color).size();
        }
        for(School s: schools){
            if(max<s.getDiningRoom().get(color).size()){
                max=s.getDiningRoom().get(color).size();
                owner=s.getOwner();
            }
        }
        professors[color.ordinal()].setOwner(owner);
    }

    //Calcolo dell'influenza tenendo conto di tutti i professori e le tower
    //Ritorna il player che ha piu influenza sull'isola scelta
    public String getTotalInfluence(int islandPosition) {
        Map<String,Integer> owners = new HashMap<>();
        for(Player player : gameModel.getPlayers()) {
            owners.put(player.getNickname(),0);
        }
        owners = getStudentInfluence(islandPosition,owners,Arrays.asList(CharacterColor.values()));
        // Aggiungo l'influenza delle torri
        if(!islands.get(islandPosition).getTowers().isEmpty())
        {
            owners = getTowersInfluence(islandPosition, owners);
        }
        return getMaxInfluence(owners);
    }

    //Calcolo influenza dei player data dagli studenti presenti sull'isola
    public Map<String,Integer> getStudentInfluence(int islandPosition, Map<String,Integer> owners, List<CharacterColor> characterColors) {
        Map<CharacterColor,List<Student>> students=islands.get(islandPosition).getStudents();
        String owner;
        for (CharacterColor c: characterColors)
        {
            owner = professors[c.ordinal()].getOwner();
            if(!owner.equals("NONE"))
                owners.replace(owner,owners.get(owner)+students.get(c).size());
        }
        return owners;
    }
    //Calcolo influenza dei player data dalle torri presenti sull'isola

    public Map<String,Integer> getTowersInfluence(int islandPosition, Map<String,Integer> owners) {
        String owner = islands.get(islandPosition).getTowers().get(0).getOwner();
        owners.replace(owner,owners.get(owner)+islands.get(islandPosition).getTowers().size());
        return owners;
    }

    //Data una Mappa<String,Integer> restituisce il player con piu influenza
    public String getMaxInfluence(Map<String, Integer> owners) {
        int max=0;
        String result="NONE";
        for(String s : owners.keySet())
        {
            if(owners.get(s)>max) {
                max = owners.get(s);
                result = s;
            }
            else if(owners.get(s)==max)
                result="NONE";
        }
        return result;
    }

    //Muove una torre dalla scuola all'isola indicata
    public void moveTower(String fromSchool, int toIsland) {
        islands.get(toIsland).addTower(getSchoolByOwner(fromSchool).removeTower());
    }

    //Muove una torre da una isola alla scuola
    public void moveTower(int fromIsland,String toSchool) {
        getSchoolByOwner(toSchool).restockTower(islands.get(fromIsland).removeTowers());
    }

    //Restituisce una lista di num studenti casuali
    public List<Student> removeRandomStudents (int studentsNumber) {
        List<Student> result= new ArrayList<>();
        for(int i=0;i < studentsNumber;i++)
        {
            result.add(students.remove(students.size()-1-i));
        }
        return result;
    }
    //Restituisce uno studente casuale

    public Student removeRandomStudent() {
        return students.remove(students.size()-1);
        //TODO se gli studenti non sono abbastanza le nuvole devono rimanere vuote
    }

    //Chiamo questo metodo ogni volta che viene aggiunta una torre su un'isola, la cui posizione viene passata come parametro
    //Controlla se le isole adiacenti a quella indicata hanno una torre delle stesso colore.
    //In questo caso, sposta tutti gli elementi delle isole adiacenti in quella indicata
     public void checkNearIsland(int islandPosition) {
         //controllo che l'isola adiacente successiva abbia delle torri
         if(!islands.get((islandPosition+1)%islands.size()).getTowers().isEmpty() &&
                 islands.get(islandPosition).getColorTower().equals(islands.get((islandPosition+1)%islands.size()).getColorTower()))
         {
             for(CharacterColor c : CharacterColor.values()) {
                 islands.get(islandPosition).addStudents(islands.get((islandPosition + 1) % islands.size()).getStudents().get(c));
             }
             islands.get(islandPosition).addTowers(islands.get((islandPosition+1)%islands.size()).getTowers());
             islands.remove((islandPosition+1)%islands.size());
         }
         int position=islandPosition-1;
         if(position==-1)
             position=islands.size()-1;
         if(!islands.get(position).getTowers().isEmpty() &&
                islands.get(islandPosition).getColorTower().equals(islands.get(position).getColorTower()))
         {
             for(CharacterColor c : CharacterColor.values()) {
                 islands.get(islandPosition).addStudents(islands.get(position).getStudents().get(c));
             }
             islands.get(islandPosition).addTowers(islands.get(position).getTowers());
             islands.remove(position);
         }
     }
     public void findWinner() {
         Player winner = gameModel.getPlayerByNickname(getSchools()[0].getOwner());
         int min_tower = getSchools()[0].getTowersNumber();
         for (int i = 1; i < playersNumber; i++) {
             if (getSchools()[i].getTowersNumber() < min_tower) {
                 min_tower = getSchools()[i].getTowersNumber();
                 winner = gameModel.getPlayerByNickname(getSchools()[i].getOwner());
             } else if (getSchools()[i].getTowersNumber() == min_tower) {
                 winner = null;
             }
         }
         if(winner==null){
             int[] professors = new int[playersNumber];
             for(Professor p : this.professors){
                 professors[gameModel.getPlayerByNickname(p.getOwner()).getClientID()]++;
             }
             int max=professors[0];
             winner=gameModel.getPlayerById(0);
             for(int i=1;i<playersNumber;i++){
                 if(max<professors[i]){
                     max=professors[i];
                     winner=gameModel.getPlayerById(i);
                 }else if(max==professors[i])
                     winner=null;
             }
         }
         gameModel.setWinner(winner);
     }

     @Override
    public String toString(){
        String result;
        int i=0;
        result = "Clouds:\n";
        for(Cloud c: clouds) {
            result = result.concat("Cloud # " + i +": ");
            result = result.concat(c.toString() + "\n");
            i++;
        }
        result = result.concat("Schools:\n");
        for(School s: schools)
            result=result.concat(s.toString() + "\n");
        result = result.concat("Professors:\n");
        for(CharacterColor c : CharacterColor.values())
            result=result.concat(c.toString() +" Professor: "+ getProfessorByColor(c).getOwner() + "\n");
        return result;
    }
}
