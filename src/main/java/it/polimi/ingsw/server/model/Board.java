package it.polimi.ingsw.server.model;

import it.polimi.ingsw.constants.Constants;
import it.polimi.ingsw.enums.CharacterColor;

import java.io.Serializable;
import java.util.*;

public class Board implements Serializable {
    private final int playersNumber;
    private int motherNaturePosition;
    private final Cloud[] clouds;
    private final List<Island> islands;
    private final School[] schools;
    private final List<Student> students;
    private final Professor[] professors; //è un salvataggio
    private final GameModel gameModel;
    private List<Integer> lastRemovedIslands;

    //TODO metodo int getInfluencePlayer(String Player, int islandPosition) RITORNA L'INFLUENZA DI UN PLAYER SULL'ISOLA E NON CHI HA PIU INFLUENZA

    public Board(List<Player> players, GameModel gameModel) {
        this.gameModel = gameModel;
        this.playersNumber = players.size();
        this.motherNaturePosition = 0;
        this.students = new ArrayList<>();
        this.professors = new Professor[5];
        this.clouds = new Cloud[playersNumber];
        this.islands = new ArrayList<>();
        this.schools = new School[playersNumber];
        this.lastRemovedIslands = new ArrayList<>();

        //--CREAZIONE STUDENTI--
        for (int j = 0; j < 26; j++) {
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
        for (int i = 0; i < playersNumber; i++) {
            clouds[i] = new Cloud();
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
        for (Player p : players) {
            schools[p.getClientID()] = new School(p, p.getColor(), playersNumber);
        }
        setInitialEntrance();
    }

    public List<Integer> getLastRemovedIslands() {
        return lastRemovedIslands;
    }

    public void setLastRemovedIslands(List<Integer> lastRemovedIslands) {
        this.lastRemovedIslands = lastRemovedIslands;
    }

    public int getStudentsSize() {
        return students.size();
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
    public School getSchoolByOwnerId(int clientId) {
        return schools[clientId];
    }

    //TODO ECCEZIONE SE NICKNAME E' SBAGLIATO - NON SO SE SERVE
    public Professor getProfessorByColor(CharacterColor color) {
        return professors[color.ordinal()];
    }

    public void setStudentsonClouds() {
        for (Cloud cloud : clouds) {
            if (playersNumber == 2 || playersNumber == 4)
                cloud.addStudents(removeRandomStudents(3));
            else cloud.addStudents(removeRandomStudents(4));
        }
    }

    private void setInitialEntrance() {
        for (School school : schools) {
            if (playersNumber == 2 || playersNumber == 4)
                school.addEntranceStudents(removeRandomStudents(7));
            else school.addEntranceStudents(removeRandomStudents(9));
        }
    }

    public void moveStudent(int clientId, int toIsland, String color) {
        islands.get(toIsland).addStudent(getSchoolByOwnerId(clientId).removeEntranceStudent(CharacterColor.valueOf(color)));
    }

    public void moveStudent(int fromCloud, int clientId) {
        getSchoolByOwnerId(clientId).addEntranceStudents(clouds[fromCloud].removeStudents());
    }

    public void moveMotherNature(int chosenSteps) {
        islands.get(motherNaturePosition).setMotherNature(false);
        motherNaturePosition = (motherNaturePosition + chosenSteps) % islands.size();
        islands.get(motherNaturePosition).setMotherNature(true);
    }

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
        if (newOwner != -1) {
            professors[color.ordinal()].setOwner(newOwner);
            //Per la grafica:
            if (oldOwner != -1)
                getSchoolByOwnerId(newOwner).addProfessor(getSchoolByOwnerId(oldOwner).removeProfessor(color));
            else
                getSchoolByOwnerId(newOwner).addProfessor(professors[color.ordinal()]);
        }
    }

    //Calcolo dell'influenza tenendo conto di tutti i professori e le tower
    //Ritorna il player che ha piu influenza sull'isola scelta
    public int getTotalInfluence(int islandPosition) {
        int[] influence = new int[playersNumber];
        for (int i = 0; i < playersNumber; i++) {
            influence[i] = 0;
        }
        influence = getStudentInfluence(islandPosition, influence, Arrays.asList(CharacterColor.values()));
        // Aggiungo l'influenza delle torri
        if (!islands.get(islandPosition).getTowers().isEmpty()) {
            influence = getTowersInfluence(islandPosition, influence);
        }
        return getMaxInfluence(influence);
    }

    //Calcolo influenza dei player data dagli studenti presenti sull'isola
    public int[] getStudentInfluence(int islandPosition, int[] influence, List<CharacterColor> characterColors) {
        Map<CharacterColor, List<Student>> students = islands.get(islandPosition).getStudents();
        int owner;
        for (CharacterColor c : characterColors) {
            owner = professors[c.ordinal()].getOwner();
            if (owner != -1)
                influence[owner] += students.get(c).size();
        }
        return influence;
    }
    //Calcolo influenza dei player data dalle torri presenti sull'isola

    public int[] getTowersInfluence(int islandPosition, int[] influence) {
        int owner = islands.get(islandPosition).getTowers().get(0).getOwner();
        influence[owner] += islands.get(islandPosition).getTowers().size();
        return influence;
    }

    //Data una Mappa<String,Integer> restituisce il player con piu influenza
    public int getMaxInfluence(int[] influence) {
        int max = 0;
        int result = -1;
        for (int i = 0; i < playersNumber; i++) {
            if (influence[i] > max) {
                max = influence[i];
                result = i;
            } else if (influence[i] == max)
                result = -1;
        }
        return result;
    }

    //Muove una torre dalla scuola all'isola indicata
    public void moveTower(int clientId, int island, String destination) {
        if (destination.equalsIgnoreCase("island"))
            islands.get(island).addTower(getSchoolByOwnerId(clientId).removeTower());
        else
            getSchoolByOwnerId(clientId).restockTower(islands.get(island).removeTowers());
    }

    //Muove una torre da una isola alla scuola

    //Restituisce una lista di num studenti casuali
    public List<Student> removeRandomStudents(int studentsNumber) {
        List<Student> result = new ArrayList<>();
        for (int i = 0; i < studentsNumber; i++) {
            result.add(students.remove(students.size() - 1 - i));
        }
        return result;
    }
    //Restituisce uno studente casuale

    public Student removeRandomStudent() {
        return students.remove(students.size() - 1);
        //TODO se gli studenti non sono abbastanza le nuvole devono rimanere vuote
    }

    //Chiamo questo metodo ogni volta che viene aggiunta una torre su un'isola, la cui posizione viene passata come parametro
    //Controlla se le isole adiacenti a quella indicata hanno una torre delle stesso colore.
    //In questo caso, sposta tutti gli elementi delle isole adiacenti in quella indicata
    public void checkNearIsland(int islandPosition) {
        lastRemovedIslands.clear();
        if (islands.get(islandPosition).getTowers().isEmpty())
            return;
        //controllo che l'isola adiacente successiva abbia delle torri
        if (!islands.get((islandPosition + 1) % islands.size()).getTowers().isEmpty() &&
                islands.get(islandPosition).getColorTower().equals(islands.get((islandPosition + 1) % islands.size()).getColorTower())) {
            for (CharacterColor c : CharacterColor.values()) {
                islands.get(islandPosition).addStudents(islands.get((islandPosition + 1) % islands.size()).getStudents().get(c));
            }
            islands.get(islandPosition).addTowers(islands.get((islandPosition + 1) % islands.size()).getTowers());
            if (((islandPosition + 1) % islands.size()) == 0 || ((islandPosition + 1) % islands.size()) == Math.ceil((double) islands.size() / 2.0))
                lastRemovedIslands.add(islandPosition);
            else lastRemovedIslands.add((islandPosition + 1) % islands.size());
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
            if (position == 0 || position == Math.ceil((double) islands.size() / 2.0))
                lastRemovedIslands.add(islandPosition);
            else lastRemovedIslands.add(position);
            islands.remove(position);
            islands.get(motherNaturePosition).setMotherNature(false);
            islands.get(position).setMotherNature(true);
            motherNaturePosition = position;
        } else {
            islands.get(motherNaturePosition).setMotherNature(false);
            islands.get(islandPosition).setMotherNature(true);
            motherNaturePosition = islandPosition;
        }
    }

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

    public StringBuilder draw(int x, int y) {
        StringBuilder board = new StringBuilder();
        int high = !gameModel.isExpertGame ? 27 : 33;
        int distance;
        int cont = 0; //Si potrebbe togliere, ma l'ultimo for diventa illeggibile
        //Stampo cornice
        board.append(Constants.boardFrame(x, y, gameModel.isExpertGame));
        board.append(Constants.cursorUp(high));
        //Stampo players
        for (Player player : gameModel.getPlayers()) {
            int coin = gameModel.isExpertGame() ? ((BoardExpert) gameModel.getBoard()).getPlayerCoins(player.getClientID()) : -1;
            if (gameModel.getCurrentPlayer().getClientID() == player.getClientID())
                board.append(player.draw(2 + x, 0, coin, true));
            else
                board.append(player.draw(2 + x, 0, coin, false));
            board.append(Constants.cursorDown(1));
        }
        if (gameModel.isExpertGame)
            board.append(Constants.cursorUp(playersNumber * 11 - 2));
        else board.append(Constants.cursorUp(playersNumber * 9));
        //board.append(Constants.cursorRight(34));
        //111
        distance = (111 - (int) Math.ceil(((float) islands.size() - 2.0) / 2.0) * 21) / (1 + (int) Math.ceil(((float) islands.size() - 2.0) / 2.0)); //TODO potrebbe non essere divisibile e avere un resto
        //stampa prima fila di isole
        for (int i = 0; i < Math.ceil(((float) islands.size() - 2.0) / 2.0); i++) {
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
        distance = (111 - (int) Math.floor(((float) islands.size() - 2.0) / 2.0) * 21)
                / (1 + (int) Math.floor(((float) islands.size() - 2.0) / 2.0));
        //stampo ultima fila di isole
        for (int i = islands.size() - 1; i >= Math.ceil(((float) islands.size() - 2.0) / 2.0) + 2; i--) {
            board.append(islands.get(i).draw(x + 37 + distance * (cont + 1) + cont * 21, 0, i + 1));
            board.append(Constants.cursorUp(5));
            cont++;
        }
        if (gameModel.isExpertGame) {
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
