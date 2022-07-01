package it.polimi.ingsw.server.model;

import it.polimi.ingsw.enums.CharacterColor;

import java.io.Serializable;
import java.util.*;

/**
 * This class represents the actual game board.
 * It has several tasks:
 * - It manages the creation of students, professors, islands, schools and clouds in its constructor method, calling the constructors
 * of each class.
 * - It can manage the movements of the students (e.g. from the entrance to an island), towers, MotherNature and professors(e.g. when
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
    private List<Student> students;
    private final Professor[] professors;
    private final GameModel gameModel;

    /**
     * This is the constructor of the Board Class.
     * - It creates 130 students, 26 of each different color and then are shuffled.
     * - It creates 5 professors, one of each color.
     * - It creates the clouds according to the players number. (2 players -> 2 clouds, 3 players -> 3 clouds, 4 players -> 4 clouds).
     * - It creates the 12 Islands, paying attention to the first one and the sixth,
     * which respectively has and does not have MotherNature. The remaining needs only a student.
     * - It creates a school for each player.
     *
     * @param gameModel instance of gameModel: it is necessary in order to declare the winner.
     */
    public Board(GameModel gameModel) {
        this.gameModel = gameModel;
        this.playersNumber = gameModel.getPlayersNumber();
        this.motherNaturePosition = 0;
        this.students = new ArrayList<>();
        this.professors = new Professor[5];
        this.clouds = new Cloud[playersNumber];
        this.islands = new ArrayList<>();
        this.schools = new School[playersNumber];

        for (int j = 0; j < 26; j++) {
            this.students.add(new Student(CharacterColor.RED));
            this.students.add(new Student(CharacterColor.BLUE));
            this.students.add(new Student(CharacterColor.GREEN));
            this.students.add(new Student(CharacterColor.PINK));
            this.students.add(new Student(CharacterColor.YELLOW));
        }
        Collections.shuffle(students);

        for (CharacterColor c : CharacterColor.values()) {
            professors[c.ordinal()] = new Professor(c);
        }

        for (int i = 0; i < playersNumber; i++) {
            clouds[i] = new Cloud();
        }

        for (int i = 0; i < 12; i++) {
            if (i == 0)
                islands.add(new Island(true));
            else if (i == 6)
                islands.add(new Island(false));
            else
                islands.add(new Island(removeRandomStudent()));
        }

        for (Player p : gameModel.getPlayers()) {
            schools[p.getClientID()] = new School(p, p.getColor(), playersNumber);
        }
        setInitialEntrance();
    }

    /**
     * @return the gameModel
     */
    public GameModel getGameModel() {
        return gameModel;
    }

    /**
     * @return the number of students in the bag.
     */
    public int getStudentsSize() {
        return students.size();
    }

    /**
     * @return the island's id on which MotherNature is when this method is called.
     */
    public int getMotherNaturePosition() {
        return motherNaturePosition;
    }

    /**
     * @return an array of all the clouds available in the game.
     */
    public Cloud[] getClouds() {
        return clouds;
    }

    /**
     * @return the number of players in the game.
     */
    public int getPlayersNumber() {
        return playersNumber;
    }

    /**
     * @return a list of all the islands available in the game.
     */
    public List<Island> getIslands() {
        return islands;
    }

    /**
     * @return an array of all the schools available in the game.
     */
    public School[] getSchools() {
        return schools;
    }

    /**
     * @param clientId it is the id associated to each client.
     * @return the school of the player with the passed clientId.
     */
    public School getSchoolByOwnerId(int clientId) {
        return schools[clientId];
    }

    /**
     * @param color is one of the available colors for professors and students
     * @return the professor with the color required
     */
    public Professor getProfessorByColor(CharacterColor color) {
        return professors[color.ordinal()];
    }

    /**
     * It fills the clouds with the needed students number.
     */
    public boolean setStudentsonClouds() {
        if ((playersNumber == 2 && students.size() >= 6) || ((playersNumber == 3 || playersNumber == 4) && students.size() >= 12)) {
            for (Cloud cloud : clouds) {
                if (playersNumber == 2 || playersNumber == 4)
                    cloud.addStudents(removeRandomStudents(3));
                else cloud.addStudents(removeRandomStudents(4));
            }
            return true;
        }
        return false;
    }

    /**
     * It fills the Entrance of each school with the needed students number.
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
     *
     * @param clientId It represents the player who made the choice
     * @param toIsland It is the IdIsland to which send the student
     * @param color    The color of the student to move
     */
    public void moveStudent(int clientId, int toIsland, String color) {
        islands.get(toIsland).addStudent(getSchoolByOwnerId(clientId).removeEntranceStudent(CharacterColor.valueOf(color)));
    }

    /**
     * This method is called when, at the end of the turn, a player chooses the cloud, therefore 'moves' all the students
     * from the cloud to his school's entrance.
     *
     * @param fromCloud the idCloud to consider.
     * @param clientId  the player who made the choice.
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
     * When a player has the highest number of students of a color, he gains the professor.
     * Therefore, the latter needs to be moved and when another player gains more students of the same color
     * in the dining room, the owner need to be updated.
     *
     * @param color parameter passed to know what professor to be moved
     */
    public void updateProfessor(CharacterColor color) {
        int oldOwner = professors[color.ordinal()].getOwner();
        int newOwner = -1;
        int max = 0;
        if (oldOwner != -1) {
            max = getSchoolByOwnerId(oldOwner).getDiningRoom().get(color).size();
        }
        for (School s : schools) {
            if (max < s.getDiningRoom().get(color).size()) {
                max = s.getDiningRoom().get(color).size();
                newOwner = s.getOwnerId();
            }
        }
        if (newOwner != -1 && newOwner != oldOwner) {
            professors[color.ordinal()].setOwner(newOwner);
            //Per la grafica:
            if (oldOwner != -1)
                getSchoolByOwnerId(newOwner).addProfessor(getSchoolByOwnerId(oldOwner).removeProfessor(color));
            else
                getSchoolByOwnerId(newOwner).addProfessor(professors[color.ordinal()]);
        }
    }

    /**
     * It calculates the influence according to the total number of professors and towers
     *
     * @param islandPosition the id of the island the player want to know the influence
     * @return the player id with the highest influence on the chosen island
     */
    public int getTotalInfluence(int islandPosition) {
        int[] influence = new int[playersNumber];
        for (int i = 0; i < playersNumber; i++) {
            influence[i] = 0;
        }
        influence = getStudentInfluence(islandPosition, influence, Arrays.asList(CharacterColor.values()));
        if (!islands.get(islandPosition).getTowers().isEmpty()) {
            influence = getTowersInfluence(islandPosition, influence);
        }
        return getMaxInfluence(influence);
    }

    /**
     * It calculates the influence according to only the number of students on a specific island
     *
     * @param islandPosition  the Island considered in order to calculate the influence
     * @param influence       an array with the influence of each player
     * @param characterColors a list that contains all available colors
     * @return an array with the influence of each player.
     */
    public int[] getStudentInfluence(int islandPosition, int[] influence, List<CharacterColor> characterColors) {
        Map<CharacterColor, List<Student>> students = islands.get(islandPosition).getStudents();
        int owner;
        for (CharacterColor c : characterColors) {
            owner = professors[c.ordinal()].getOwner();
            if (owner != -1)
                influence[owner] += students.get(c).size();
        }
        if (playersNumber == 4) {
            int[] teamInfluence = new int[2];
            teamInfluence[0] = influence[0] + influence[2];
            teamInfluence[1] = influence[1] + influence[3];
            return teamInfluence;
        }
        return influence;
    }

    /**
     * It calculates the influence according to only the number of towers on a specific island
     *
     * @param islandPosition the Island considered in order to calculate the influence
     * @param influence      an array with the influence of each player
     * @return an array with the influence of each player.
     */

    public int[] getTowersInfluence(int islandPosition, int[] influence) {
        int owner;
        if (!islands.get(islandPosition).getTowers().isEmpty()) {
            owner = islands.get(islandPosition).getTowers().get(0).getOwner();
            influence[owner] += islands.get(islandPosition).getTowers().size();
        }
        return influence;
    }

    /**
     * This method which receives in input an array of influences, return the player with the highest influence.
     *
     * @param influence an array with the influence of each player
     */

    public int getMaxInfluence(int[] influence) {
        int max = 0;
        int result = -1;
        for (int i = 0; i < influence.length; i++) {
            if (influence[i] > max) {
                max = influence[i];
                result = i;
            } else if (influence[i] == max)
                result = -1;
        }
        return result;
    }

    /**
     * It moves a tower from the school to a specified Island or from the island to a school.
     *
     * @param clientId    It represents the player who made the choice
     * @param island      It is the IdIsland to which send a tower or remove all the towers.
     * @param destination The destination: island or school.
     */

    public void moveTower(int clientId, int island, String destination) {
        if (destination.equalsIgnoreCase("island"))
            islands.get(island).addTower(getSchoolByOwnerId(clientId).removeTower());
        else
            getSchoolByOwnerId(clientId).restockTower(islands.get(island).removeTowers());
    }

    /**
     * It removes a random number of student from the bag and put them into a list.
     *
     * @param studentsNumber the number of students which are removed.
     * @return the chosen students into an array
     */
    public List<Student> removeRandomStudents(int studentsNumber) {
        List<Student> result = new ArrayList<>();
        for (int i = 0; i < studentsNumber; i++) {
            result.add(students.remove(students.size() - 1 - i));
        }
        return result;
    }

    /**
     * Method which removes randomly a student from all the students still available
     *
     * @return a student
     */

    public Student removeRandomStudent() {
        return students.remove(students.size() - 1);
        //TODO se gli studenti non sono abbastanza le nuvole devono rimanere vuote
    }

    /**
     * This method is called whenever a tower is added to an Island, whose position is passed as a parameter.
     * It checks if the 2 near islands to the one considered have a tower with same color, also checking if the
     * near island has a tower.
     * If so, it moves all the students of the near islands on the one considered throw islandPosition
     *
     * @param islandPosition the island whose neighboring islands need to be checked
     * @param diplomat       boolean to checks if someone has chosen this island when playing the diplomat card.
     */
    public void checkNearIsland(int islandPosition, boolean diplomat) {
        boolean move = false;
        if (islands.get(islandPosition).getTowers().isEmpty())
            return;
        if (!(islands.get((islandPosition + 1) % islands.size()).getTowers().isEmpty()) &&
                islands.get(islandPosition).getColorTower().equals(islands.get((islandPosition + 1) % islands.size()).getColorTower())) {
            for (CharacterColor c : CharacterColor.values()) {
                islands.get(islandPosition).addStudents(islands.get((islandPosition + 1) % islands.size()).getStudents().get(c));
            }
            islands.get(islandPosition).addTowers(islands.get((islandPosition + 1) % islands.size()).getTowers());
            if (islands.get((islandPosition + 1) % islands.size()).hasNoEntryTile())
                for (int i = 0; i < islands.get((islandPosition + 1) % islands.size()).getNoEntryTile(); i++)
                    islands.get(islandPosition).setNoEntryTile(true);
            islands.remove((islandPosition + 1) % islands.size());

            if (islandPosition == islands.size()) {
                if (motherNaturePosition == 0 && diplomat)
                    move = true;
                islandPosition--;
            }
            if (!diplomat) {
                islands.get(islandPosition).setMotherNature(true);
                motherNaturePosition = islandPosition;
            } else if (motherNaturePosition > islandPosition || move) { // se uso diplomat e mother nature è successiva all'isola che ho rimosso, la sposto indietro di 1
                if (motherNaturePosition < islands.size())
                    islands.get(motherNaturePosition).setMotherNature(false);
                motherNaturePosition = move ? islandPosition : motherNaturePosition - 1;
                islands.get(motherNaturePosition).setMotherNature(true);
            }
        }
        move = false;
        //if (islandPosition == islands.size()) islandPosition--;
        int position = islandPosition - 1;
        if (position == -1)
            position = islands.size() - 1;
        if (!islands.get(position).getTowers().isEmpty() &&
                islands.get(islandPosition).getColorTower().equals(islands.get(position).getColorTower())) {
            for (CharacterColor c : CharacterColor.values()) {
                islands.get(islandPosition).addStudents(islands.get(position).getStudents().get(c));
            }
            if (islands.get((position)).hasNoEntryTile())
                for (int i = 0; i < islands.get(position).getNoEntryTile(); i++)
                    islands.get(islandPosition).setNoEntryTile(true);
            islands.get(islandPosition).addTowers(islands.get(position).getTowers());
            islands.remove(position);
            if (position == islands.size()) {
                move = (motherNaturePosition == position);
                position--;
            }
            //posizione madre natura: zero se islandPosition è zero altrimenti position
            if (!diplomat) {
                islands.get(islandPosition == 0 ? islandPosition : position).setMotherNature(true);
                motherNaturePosition = islandPosition == 0 ? islandPosition : position;
            } else if (motherNaturePosition > position) {
                if (motherNaturePosition < islands.size())
                    islands.get(motherNaturePosition).setMotherNature(false);
                motherNaturePosition = move ? 0 : motherNaturePosition - 1;
                islands.get(motherNaturePosition).setMotherNature(true);
            }
        }
    }

    /**
     * It checks if there is a winner according to the number of towers left in each school and
     * who has the highest number of professors.
     * Then it is passed to the gameModel.
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
                if (p.getOwner() != -1)
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

    public void removeStudentsForTesting(int players) {
        if(players==2)
                this.students=this.students.subList(0,11);
        else this.students=this.students.subList(0,15);
        }

}
