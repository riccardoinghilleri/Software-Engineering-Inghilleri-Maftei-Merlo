package it.polimi.ingsw.server.model;

import it.polimi.ingsw.constants.Constants;
import it.polimi.ingsw.enums.CharacterColor;

import java.io.Serializable;
import java.util.*;
/**
 * This class is the actual game board that the user sees on the screen while playing in the normal mode.
 * It has several tasks:
 * - It manages the creation of the students, professor, islands, schools and cloud in its constructor method, calling the constructors
 * of each class.
 * - It  can manage the movements of the students(e.g. from the entrance to an island), towers, MotherNature and professors(e.g. when
 * the professor's owner changes).
 * - it calculates the influence
 * - it returns the winner if there is one.
 */

public class Board implements Serializable {
    private final int playersNumber;
    private int motherNaturePosition;
    private final Cloud[] clouds;
    private final List<Island> islands;
    private final School[] schools;
    private final List<Student> students;
    private final Professor[] professors; //è un salvataggio
    private final GameModel gameModel;

    //TODO metodo int getInfluencePlayer(String Player, int islandPosition) RITORNA L'INFLUENZA DI UN PLAYER SULL'ISOLA E NON CHI HA PIU INFLUENZA
    /**
     * This is the constructor of the Board Class.
     * @param gameModel it is a necessary for declaring the winner
     * @param players it is a list of the players involved in the game.
     */
    public Board(List<Player> players, GameModel gameModel) {
        this.gameModel = gameModel;
        this.playersNumber = players.size();
        this.motherNaturePosition = 0;
        this.students = new ArrayList<>();
        this.professors = new Professor[5];
        this.clouds = new Cloud[playersNumber];
        this.islands = new ArrayList<>();
        this.schools = new School[playersNumber];

        //--CREAZIONE STUDENTI--
        /**
         * Here are created 130 students, 26 of each different color and then are shuffled, ready to be chosen.
         */
        for (int j = 0; j < 26; j++) {
            this.students.add(new Student(CharacterColor.RED));
            this.students.add(new Student(CharacterColor.BLUE));
            this.students.add(new Student(CharacterColor.GREEN));
            this.students.add(new Student(CharacterColor.PINK));
            this.students.add(new Student(CharacterColor.YELLOW));
        }
        Collections.shuffle(students);

        //--CREAZIONE PROFESSORI--
        /**
         * Here are created 5 professors, one of each color.
         */
        for (CharacterColor c : CharacterColor.values()) {
            professors[c.ordinal()] = new Professor(c);
        }

        //--CREAZIONE NUVOLE--
        /**
         * Here are created the clouds according to the players number
         * (2 players -> 2 clouds, 3 players->3 clouds and finally 4 players-> 4 clouds)
         */

        for (int i = 0; i < playersNumber; i++) {
            clouds[i] = new Cloud();
        }

        //--CREAZIONE ISOLE--
        /**
         * Here are created the 12 Islands, paying attention to the first one and the sixth,
         * which respectively has and does not have MotherNature. The remaining needs only a student.
         */
        for (int i = 0; i < 12; i++) {
            if (i == 0)
                islands.add(new Island(true));
            else if (i == 6)
                islands.add(new Island(false));
            else
                islands.add(new Island(removeRandomStudent()));
        }

        //--CREAZIONE SCUOLE--
        /**
         * # schools are created according to the playersNumber.
         */
        for (Player p : players) {
            schools[p.getClientID()] = new School(p, p.getColor(), playersNumber);
        }
        setInitialEntrance();
    }

    public int getStudentsSize() {
        return students.size();
    }
    /**
     * @return the IdIsland on which MotherNature is when calling this method
     */
    public int getMotherNaturePosition() {
        return motherNaturePosition;
    }
    /**
     * @return a list of all the clouds available in the game
     */
    public Cloud[] getClouds() {
        return clouds;
    }
    /**
     * @return a list of all the islands available in the game
     */
    public List<Island> getIslands() {
        return islands;
    }
    /**
     * @return a list of all the schools available in the game
     */
    public School[] getSchools() {
        return schools;
    }

    //TODO ECCEZIONE SE NICKNAME E' SBAGLIATO
    /**
     * @param clientId it is the Id associated with a client
     * @return the school of the player with the passed clientId
     */
    public School getSchoolByOwnerId(int clientId) {
        return schools[clientId];
    }

    //TODO ECCEZIONE SE NICKNAME E' SBAGLIATO - NON SO SE SERVE
    /**
     * @param color is one of the available colors for professors and students
     * @return the professor with the color required
     */
    public Professor getProfessorByColor(CharacterColor color) {
        return professors[color.ordinal()];
    }
    /**
     * It files the clouds with the needed students number
     */
    public void setStudentsonClouds() {
        for (Cloud cloud : clouds) {
            if (playersNumber == 2 || playersNumber == 4)
                cloud.addStudents(removeRandomStudents(3));
            else cloud.addStudents(removeRandomStudents(4));
        }
    }
    /**
     * It files the Entrance of each school with the needed students number
     */
    private void setInitialEntrance() {
        for (School school : schools) {
            if (playersNumber == 2 || playersNumber == 4)
                school.addEntranceStudents(removeRandomStudents(7));
            else school.addEntranceStudents(removeRandomStudents(9));
        }
    }
    /**
     * It moves a student of a specified color to an island, removing it from the entrance of the school.
     * @param clientId It represents the player who made the choice
     * @param toIsland It is the IdIsland to which send the student
     * @param color The color of the student to move
     */
    public void moveStudent(int clientId, int toIsland, String color) {
        islands.get(toIsland).addStudent(getSchoolByOwnerId(clientId).removeEntranceStudent(CharacterColor.valueOf(color)));
    }
    /**
     * This method is called when at the begging of the turn a player chooses the cloud, therefore 'moves' all the students
     * from the cloud to his school's entrance
     * @param fromCloud the idCloud to consider
     * @param clientId It represents the player who made the choice
     */
    public void moveStudent(int fromCloud, int clientId) {
        getSchoolByOwnerId(clientId).addEntranceStudents(clouds[fromCloud].removeStudents());
    }

    /**
     * This method allows moving motherNature of a number of steps passed in 'chosenSteps'
     */
    public void moveMotherNature(int chosenSteps) {
        islands.get(motherNaturePosition).setMotherNature(false);
        motherNaturePosition = (motherNaturePosition + chosenSteps) % islands.size();
        islands.get(motherNaturePosition).setMotherNature(true);
    }
    /**
     * When a player gains the influence over a color, he gains the professor, therefore the latter needs to be moved and
     * to be changed the owner.
     * @param color parameter passed to know what professor to be moved
     */
    public void updateProfessor(CharacterColor color) {
        int oldOwner = professors[color.ordinal()].getOwner();
        int newOwner = -1;
        int max = 0;
        if (oldOwner!=-1) {
            max = getSchoolByOwnerId(oldOwner).getDiningRoom().get(color).size();
        }
        for (School s : schools) {
            if (max < s.getDiningRoom().get(color).size()) {
                max = s.getDiningRoom().get(color).size();
                newOwner = s.getOwnerId();
            }
        }
        if (newOwner != -1) {
            professors[color.ordinal()].setOwner(newOwner);
            //Per la grafica:
            if (oldOwner!=-1)
                getSchoolByOwnerId(newOwner).addProfessor(getSchoolByOwnerId(oldOwner).removeProfessor(color));
            else
                getSchoolByOwnerId(newOwner).addProfessor(professors[color.ordinal()]);
        }
    }
    /**
     * It calculates the influence according to the total number of professors and towers
     * @param islandPosition the id of the island the player want to know the influence
     * @return the player with the highest influence on the chosen island
     */
    //Calcolo dell'influenza tenendo conto di tutti i professori e le tower
    //Ritorna il player che ha piu influenza sull'isola scelta
    public int getTotalInfluence(int islandPosition) {
        int[] influence = new int[playersNumber];
        for (int i=0 ;i<playersNumber;i++) {
           influence[i]=0;
        }
        influence = getStudentInfluence(islandPosition, influence, Arrays.asList(CharacterColor.values()));
        // Aggiungo l'influenza delle torri
        if (!islands.get(islandPosition).getTowers().isEmpty()) {
            influence = getTowersInfluence(islandPosition, influence);
        }
        return getMaxInfluence(influence);
    }
    /**
     * It calculates the influence according to only the number of students on a specific island
     * @param islandPosition the Island considered in order to calculate the influence
     */
    //Calcolo influenza dei player data dagli studenti presenti sull'isola
    public int[] getStudentInfluence(int islandPosition, int[] influence, List<CharacterColor> characterColors) {
        Map<CharacterColor, List<Student>> students = islands.get(islandPosition).getStudents();
        int owner;
        for (CharacterColor c : characterColors) {
            owner = professors[c.ordinal()].getOwner();
            if (owner!=-1)
                influence[owner]+=students.get(c).size();
        }
        return influence;
    }
    //Calcolo influenza dei player data dalle torri presenti sull'isola
    /**
     * It calculates the influence according to only the number of towers on a specific island
     * @param islandPosition the Island considered in order to calculate the influence
     * @return an array of influences correspondingly to each player
     */

    public int[] getTowersInfluence(int islandPosition, int[] influence) {
        int owner = islands.get(islandPosition).getTowers().get(0).getOwner();
        influence[owner]+= islands.get(islandPosition).getTowers().size();
        return influence;
    }

    /**
     * This method which receives in input an array of influences, return the highest value of influence,
     * so the player with the highest influence.
     */
    //Data una Mappa<String,Integer> restituisce il player con piu influenza
    public int getMaxInfluence(int[] influence) {
        int max = 0;
        int result = -1;
        for (int i=0;i<playersNumber;i++) {
            if (influence[i]> max) {
                max = influence[i];
                result = i;
            } else if (influence[i] == max)
                result = -1;
        }
        return result;
    }
    /**
     * It moves a tower from the school to a specified Island
     */

    public void moveTower(int clientId, int island, String destination) {
        if(destination.equalsIgnoreCase("island"))
        islands.get(island).addTower(getSchoolByOwnerId(clientId).removeTower());
        else getSchoolByOwnerId(clientId).restockTower(islands.get(island).removeTowers());
    }

    /**
     * It removes a random number of student and put them into an array
     * @param studentsNumber the number of students which are removed from the total number available
     * @return the students chosen into an array
     */
    public List<Student> removeRandomStudents(int studentsNumber) {
        List<Student> result = new ArrayList<>();
        for (int i = 0; i < studentsNumber; i++) {
            result.add(students.remove(students.size() - 1 - i));
        }
        return result;
    }

    /**
     * Method which removes randomly a student from all the students still available and not chosen yet
     * @return a student
     */

    public Student removeRandomStudent() {
        return students.remove(students.size() - 1);
        //TODO se gli studenti non sono abbastanza le nuvole devono rimanere vuote
    }
    /**
     * This method is called whenever a tower is added to an Island, whose position is passed as a parameter.
     * It checks is the 2 near islands to the one considered have a tower with same color, also checking if the
     * near island has a tower.
     * If so, it moves all the students of the near islands on the one considered throw islandPosition
     * @param islandPosition the island whose neighboring islands need to be chcked
     */
    public void checkNearIsland(int islandPosition) {
        if(islands.get(islandPosition).getTowers().isEmpty())
            return;
        //controllo che l'isola adiacente successiva abbia delle torri
        if (!islands.get((islandPosition + 1) % islands.size()).getTowers().isEmpty() &&
                islands.get(islandPosition).getColorTower().equals(islands.get((islandPosition + 1) % islands.size()).getColorTower())) {
            for (CharacterColor c : CharacterColor.values()) {
                islands.get(islandPosition).addStudents(islands.get((islandPosition + 1) % islands.size()).getStudents().get(c));
            }
            islands.get(islandPosition).addTowers(islands.get((islandPosition + 1) % islands.size()).getTowers());
            islands.remove((islandPosition + 1) % islands.size());
        }
        int position = islandPosition - 1;
        if (position == -1)
            position = islands.size() - 1;
        if (!islands.get(position).getTowers().isEmpty() &&
                islands.get(islandPosition).getColorTower().equals(islands.get(position).getColorTower())) {
            for (CharacterColor c : CharacterColor.values()) {
                islands.get(islandPosition).addStudents(islands.get(position).getStudents().get(c));
            }
            islands.get(islandPosition).addTowers(islands.get(position).getTowers());
            islands.remove(position);
            islands.get(motherNaturePosition).setMotherNature(false);
            islands.get(position).setMotherNature(true);
            motherNaturePosition=position;
        } else {
            islands.get(motherNaturePosition).setMotherNature(false);
            islands.get(islandPosition).setMotherNature(true);
            motherNaturePosition=islandPosition;
        }
    }
    /**
     * It checks if there is a winner according to the number of towers left in each school and
     * who has the highest number of professors.
     * Finally it is passed to the gameModel
     */
    public void findWinner() {
        Player winner = gameModel.getPlayerById(0);
        int min_tower = getSchools()[0].getTowersNumber();
        for (int i = 1; i < playersNumber; i++) {
            if (getSchools()[i].getTowersNumber() < min_tower) {
                min_tower = getSchools()[i].getTowersNumber();
                winner = gameModel.getPlayerById(i);
            } else if (getSchools()[i].getTowersNumber() == min_tower) {
                winner = null;
            }
        }
        if (winner == null) {
            int[] professors = new int[playersNumber];
            for (Professor p : this.professors) {
                professors[p.getOwner()]++;
            }
            int max = professors[0];
            winner = gameModel.getPlayerById(0);
            for (int i = 1; i < playersNumber; i++) {
                if (max < professors[i]) {
                    max = professors[i];
                    winner = gameModel.getPlayerById(i);
                } else if (max == professors[i])
                    winner = null;
            }
        }
        gameModel.setWinner(winner);
    }

    /**
     * This mwthod is used to display all the board on the screen
     */

    public StringBuilder draw(int x, int y) {
        StringBuilder board = new StringBuilder();
        int high = !gameModel.isExpertGame ? 27 : 33;
        int distance;
        int cont = 0; //Si potrebbe togliere, ma l'ultimo for diventa illeggibile
        //Stampo cornice
        board.append(Constants.boardFrame(x, y, gameModel.isExpertGame));
        board.append(Constants.cursorUp(high));
        //Stampo players
        for(Player player: gameModel.getPlayers()){
            int coin = gameModel.isExpertGame()? ((BoardExpert)gameModel.getBoard()).getPlayerCoins(player.getClientID()) : -1;
            if (gameModel.getCurrentPlayer().getClientID() == player.getClientID())
                board.append(player.draw(2 + x, 0, coin, true));
            else
                board.append(player.draw(2 + x, 0, coin, false));
            board.append(Constants.cursorDown(1));
        }
        if (gameModel.isExpertGame)
            board.append(Constants.cursorUp(playersNumber * 11 -2));
        else board.append(Constants.cursorUp(playersNumber * 9 ));
        //board.append(Constants.cursorRight(34));
        //111
        distance = (111 - (int)Math.ceil(((float)islands.size() - 2.0) / 2.0) * 21) / (1 + (int)Math.ceil(((float)islands.size() - 2.0) / 2.0)); //TODO potrebbe non essere divisibile e avere un resto
        //stampa prima fila di isole
        for (int i = 0; i < Math.ceil(((float)islands.size() - 2.0) / 2.0); i++) {
            board.append(islands.get(i + 1).draw(x + 36 + distance * (i + 1) + i * 21, 0, i + 2));
            board.append(Constants.cursorUp(5));
        }
        board.append(Constants.cursorDown(6));
        board.append(islands.get(0).draw(x + 19, 0, 1)); //stampo isola 0
        board.append(Constants.cursorUp(4));
        //103 larghezza nuvole
        distance = ((103 - playersNumber * 11) / (playersNumber + 1));
        for (int i = 0; i < playersNumber; i++) {
            board.append(clouds[i].draw(x + 40 + distance * (i + 1) + i * 11, 0, i + 1));
            board.append(Constants.cursorUp(3));
        }
        board.append(Constants.cursorUp(1));
        board.append(islands.get(islands.size() / 2 + 1).draw(143 + x, 0, islands.size() / 2 + 2));
        board.append(Constants.cursorDown(1));
        distance = (111 - (int)Math.floor(((float)islands.size() - 2.0) / 2.0) * 21)
                / (1 + (int)Math.floor(((float)islands.size() - 2.0) / 2.0));
        //stampo ultima fila di isole
        for (int i = islands.size() - 1; i >= Math.ceil(((float)islands.size() - 2.0) / 2.0) + 2; i--) {
            board.append(islands.get(i).draw(x + 37 + distance * (cont + 1) + cont * 21, 0, i + 1));
            board.append(Constants.cursorUp(5));
            cont++;
        }
        if(gameModel.isExpertGame){
            board.append(Constants.cursorDown(8));
        } else board.append(Constants.cursorDown(6));
        if (playersNumber == 2) {
            distance = 15;
            board.append(schools[0].draw(53 + x, 0));
            board.append(Constants.cursorUp(8));
            board.append(schools[1].draw(x + 53 + distance + 31, 0));
        } else {
            distance = 3;
            board.append(schools[0].draw(42 + x, 0));
            board.append(Constants.cursorUp(8));
            board.append(schools[1].draw(42 + x + distance + 31, 0));
            board.append(Constants.cursorUp(8));
            board.append(schools[2].draw(42 + x + distance * 2 + 31 * 2, 0));
        }
        board.append(Constants.cursorDown(2));
        return board;
    }



    @Override
    public String toString() {
        String result;
        int i = 0;
        result = "Clouds:\n";
        for (Cloud c : clouds) {
            result = result.concat("Cloud # " + i + ": ");
            result = result.concat(c.toString() + "\n");
            i++;
        }
        result = result.concat("Schools:\n");
        for (School s : schools)
            result = result.concat(s.toString() + "\n");
        result = result.concat("Professors:\n");
        for (CharacterColor c : CharacterColor.values())
            result = result.concat(c.toString() + " Professor: " + getProfessorByColor(c).getOwner() + "\n");
        return result;
    }
}
