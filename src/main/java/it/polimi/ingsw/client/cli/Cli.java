package it.polimi.ingsw.client.cli;

import java.io.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import it.polimi.ingsw.client.ClientConnection;
import it.polimi.ingsw.client.View;
import it.polimi.ingsw.constants.Constants;
import it.polimi.ingsw.enums.Action;
import it.polimi.ingsw.server.ConnectionMessage.*;
import it.polimi.ingsw.server.model.*;
import it.polimi.ingsw.enums.CharacterColor;

/**
 * Main CLI client class manages the game if the player decides to play with Command Line Interface.
 *
 * @author Riccardo Inghilleri, Manuela Merlo
 */
public class Cli implements View {
    //private final PrintStream printer;
    private final Scanner reader;
    private static int port;
    private static String address;
    private ClientConnection connection;
    private boolean expertMode;
    private boolean alreadyAskedCard;
    private boolean alreadyAskedMovements;
    private boolean print, displayedBoard;

    /**
     * The constructor of the class. It creates a new Cli instance.
     */
    public Cli() {
        print = false;
        displayedBoard = false;
        reader = new Scanner(System.in);
        //printer = new PrintStream(System.out);
        alreadyAskedCard = false;
        alreadyAskedMovements = false;
        InputController.setPrinter(System.out);
        InputController.setScanner(reader);
    }

    /**
     * Getter which returns the port chosen by the client for the connection
     */
    public int getPort() {
        return port;
    }

    /**
     * Getter which returns the address chosen by the client for the connection
     */
    public String getAddress() {
        return address;
    }

    /**
     * The main class of CLI client. It instantiates a new CLI class, running it.
     *
     * @param args of type String[] - the standard java main parameters.
     */
    public static void main(String[] args) {
        Constants.clearScreen();
        //PrintStream printer = new PrintStream(System.out);
        System.out.println(Constants.ERIANTYS);
        System.out.println("Inghilleri Riccardo - Maftei Daniela - Merlo Manuela\n");
        Cli cli = new Cli();
        cli.setupConnection();
    }

    /**
     * This method is called when a client instance has started. It asks player's nickname, Ip address and port.
     * It tries to establish a connection to the server through a socket.
     * If the connection fails, it displays a message on the CLI.
     */
    public void setupConnection() {
        System.out.println(">Insert the server IP address");
        address = reader.nextLine();
        Pattern pattern = Pattern.compile("^(([0-9]|[1-9][0-9]|1[0-9][0-9]|2[0-4][0-9]|25[0-5])(\\.(?!$)|$)){4}$");
        Matcher m = pattern.matcher(address);
        while (!m.matches()) {
            System.out.println(">Invalid input. Please try again");
            address = reader.nextLine();
            m = pattern.matcher(address);
        }
        System.out.println(">Insert the server port");
        boolean error;
        do {
            try {
                port = Integer.parseInt(reader.nextLine());
                error = false;
                while (port < 1024 || port > 65535) {
                    System.out.println(">Invalid input: you have to choose a number between 1024 and 65535. Please try again");
                    port = Integer.parseInt(reader.nextLine());
                    /*if (port < 1024 || port > 65535)
                        Constants.clearRowBelow(2);*/
                }
            } catch (NumberFormatException e) {
                System.out.println(">Invalid input: you have to insert a number. Please try again.");
                error = true;
            }
        } while (error);
        try {
            connection = new ClientConnection(this);
            Thread t = new Thread(connection);
            t.start();
            setupGameSetting();
        } catch (IOException e) {
            System.out.println("Error while opening the socket");
            setupConnection();
        }
    }

    public void enable(TurnMessage message) {
        if (message.isEnable())
            stopClearBuffer();
        else startClearBuffer();
    }

    /**
     * This method manages the choice of the players number and expert game, parsing the player's input
     * and forwarding the message throw the established clientConnection
     */
    private void setupGameSetting() {
        System.out.println(">Choose number of players [2/3/4]: ");
        int playersNumber = InputController.checkParseInt();
        while (playersNumber < 2 || playersNumber > 4) {
            System.out.println(">Invalid input. Try again. ");
            playersNumber = InputController.checkParseInt();
        }
        System.out.println(">Do you want to play in ExpertMode? [y/n]: ");
        String response = InputController.checkYNInput();
        this.expertMode = response.equalsIgnoreCase("y");
        Constants.clearScreen();
        connection.send(new SettingsMessage(playersNumber, this.expertMode));

    }

    /**
     * This method manages the choice of the player's nickname, parsing the player's input
     * and forwarding the message throw the established clientConnection.
     * If the nickname has been already chosen , it displays a message on the CLI.
     */
    public void setupNickname(NicknameMessage message) {
        Constants.clearScreen();
        System.out.println(Constants.SETUP_GAME);
        String response;
        String nickname;
        if (message.getAlreadyAsked())
            System.out.println("The nickname is not available!");
        do {
            System.out.println(">Please choose a nickname: ");
            nickname = reader.nextLine();
            System.out.println(">Are you sure about your choice? [y/n]: ");
            response = InputController.checkYNInput();
        } while (response.equalsIgnoreCase("n"));
        connection.send(new SetupMessage(nickname));
    }

    /**
     * This method is used to display the available colors and wizards from which the player can choose.
     *
     * @param message parameter used to send the list of colors and wizards.
     */
    //metodo utilizzando per la scelta dei colori e del wizard
    public synchronized void setupMultipleChoice(MultipleChoiceMessage message) {
        String question;
        if (message.getAvailableChoices().stream().distinct().count() == 1) {
            question = message.isColor() ? "the color" : "the wizard";
            System.out.println(">The Game has chosen " + question + " for you: " + message.getAvailableChoices().get(0));
        } else {
            question = message.isColor() ? "Please choose a color [" : "Please choose a wizard [";
            for (String s : message.getAvailableChoices().stream().distinct().collect(Collectors.toList()))
                question = question.concat(s + "/");
            question = question.substring(0, question.length() - 1);
            question = question.concat("]:");
            System.out.println(question);
            connection.send(new SetupMessage(InputController.checkString(message.getAvailableChoices())));
        }
        if (!message.isColor()) {
            startClearBuffer();
        }
    }

    /**
     * Method used to print a message as a string on the CLI
     *
     * @param message type of message to display.
     */
    public synchronized void displayInfo(InfoMessage message) {
        if (!displayedBoard && message.waitBoard()) {
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.println(message.getString());
        displayedBoard = false;
    }

    /**
     * Method used to display the board after being updated.
     *
     * @param message : parameter of updateBoard type, used to return the board and to draw it.
     */
    public synchronized void displayBoard(UpdateBoard message) {
        Constants.clearScreen();
        //System.out.println(message.getBoard().draw(1, 1));
        if (message.getBoard().getGameModel().isExpertGame()) {
            System.out.println(ReducedModel.drawExpert((BoardExpert) message.getBoard(), 1, 1));
        } else {
            System.out.println(ReducedModel.draw(message.getBoard(), 1, 1));
        }
        displayedBoard = true;
        notify();
    }

    /**
     * This method manages the actions that a player can do.
     * It uses a switch-case constructor according to the action contained in the
     *
     * @param message -> type of message-> AskActionMessage.
     *                <p>
     *                After the player has chosen an element the boolean 'alreadyAsked' is set to false for the specified player.
     */
    public synchronized void askAction(AskActionMessage message) {
        stopClearBuffer();
        if (!displayedBoard) {
            try {
                wait();
            } catch (InterruptedException e) {}
        }
        displayedBoard = false;
        String response;
        String temp;
        ActionMessage answer = new ActionMessage();
        switch (message.getAction()) {
            case CHOOSE_ASSISTANT_CARD:
                alreadyAskedMovements = false;
                List<Integer> availablePriority = new ArrayList<>();
                for (int i = 0; i < message.getAvailableAssistantCards().size(); i++) {
                    availablePriority.add(message.getAvailableAssistantCards().get(i).getPriority());
                }
                if (!alreadyAskedCard) {
                    System.out.println(">Please choose your assistant card priority\n");
                    for (int i = 0; i < message.getAvailableAssistantCards().size(); i++) {
                        if (i == 0)
                            System.out.println(ReducedModel.draw(message.getAvailableAssistantCards().get(i), -1, 0)); //TODO strano -1
                        else
                            System.out.println(ReducedModel.draw(message.getAvailableAssistantCards().get(i), i * 10 + i, 7));
                        availablePriority.add(message.getAvailableAssistantCards().get(i).getPriority());
                    }
                    alreadyAskedCard = true;
                }
                answer.setData(InputController.checkInt(availablePriority));
                answer.setAction(Action.CHOOSE_ASSISTANT_CARD);
                connection.send(answer);
                startClearBuffer();
                break;
            case CHOOSE_CHARACTER_CARD:
                alreadyAskedMovements = false;
                System.out.println(">Do you want to use a Character Card? [y/n/getDESC]:");
                response = InputController.checkString(List.of("Y", "N", "GETDESC"));
                answer.setAction(Action.CHOOSE_CHARACTER_CARD);
                if (response.equalsIgnoreCase("getDESC")) {
                    for (CharacterCard card : message.getCharacterCards())
                        System.out.println("> " + card.getName() + ": " + card.getDescription());
                    System.out.println(">Do you want to use a Character Card? [y/n]:");
                    response = InputController.checkYNInput();
                }
                if (response.equalsIgnoreCase("n")) {
                    answer.setCharacterCardName(null);
                } else if (response.equalsIgnoreCase("y")) {
                    List<String> characterCards = new ArrayList<>();
                    for (CharacterCard characterCard : message.getCharacterCards()) {
                        characterCards.add(characterCard.getName().toString());
                    }
                    System.out.println(">Write the name of the card that you want to use: ");
                    answer.setCharacterCardName(InputController.checkString(characterCards));
                }
                connection.send(answer);
                break;
            case USE_CHARACTER_CARD:
                manageCharacterCardChoice(message);
                break;
            case DEFAULT_MOVEMENTS:
                alreadyAskedCard = false;
                String parameter;
                answer.setAction(Action.DEFAULT_MOVEMENTS);
                do {
                    parameter = chooseStudentColor(message.getSchool().getEntrance(), true,
                            ">Please choose the color of the student that you want to move from your Entrance [green/red/yellow/pink/blue]:");
                    answer.setParameter(parameter);
                    System.out.println(">Do you want to move your student to your DiningRoom" +
                            " or on an Island? [diningroom/island]:");
                    temp = InputController.checkString(List.of("DININGROOM", "ISLAND"));
                    if (temp.equalsIgnoreCase("DININGROOM")) {
                        if (message.getSchool().getDiningRoom().get(CharacterColor.valueOf(parameter)).size() == 10) {
                            System.out.println("You can't move this student to the diningRoom.");
                        }
                    }
                } while (temp.equalsIgnoreCase("DININGROOM") && message.getSchool().getDiningRoom().get(CharacterColor.valueOf(parameter)).size() == 10);

                if (temp.equalsIgnoreCase("Island")) {
                    answer.setData(chooseIsland(message.getIslands()) - 1);
                }
                connection.send(answer);
                break;
            case MOVE_MOTHER_NATURE:
                System.out.println(">Please choose how many steps you want mother nature do:");
                answer.setData(InputController.checkRange(1, message.getData()));
                answer.setAction(Action.MOVE_MOTHER_NATURE);
                connection.send(answer);
                break;
            case CHOOSE_CLOUD:
                List<Integer> availableIndexClouds = new ArrayList<>();
                for (int i = 0; i < message.getClouds().length; i++) {
                    if (!(message.getClouds())[i].getStudents().isEmpty()) {
                        availableIndexClouds.add(i + 1);
                    }
                }
                System.out.println(">Please choose your cloud");
                answer.setData(InputController.checkInt(availableIndexClouds) - 1);
                answer.setAction(Action.CHOOSE_CLOUD);
                connection.send(answer);
                startClearBuffer();
                break;
        }
    }

    /**
     * This method manages all the parameters that need to be set according to the character Card(
     * e.g. island, student color.)
     * It saves the answer in an Action message and forwards it.
     */
    //Gestisce i parametri da settare in base alla character card
    private void manageCharacterCardChoice(AskActionMessage message) {
        ActionMessage answer = new ActionMessage();
        CharacterCard characterCard = message.getChosenCharacterCard();
        answer.setAction(Action.USE_CHARACTER_CARD);
        answer.setCharacterCardName(characterCard.getName().toString().toUpperCase());
        String parameter;
        switch (characterCard.getName()) {
            case PRIEST: //isole e colore dello studente sulla carta
                answer.setParameter(chooseStudentColor(((CharacterCardwithStudents) characterCard).getStudents(),
                        false, ">Please choose the color of the student that you want to move from the Card [green/red/yellow/pink/blue]:"));
                answer.setData(chooseIsland(message.getIslands()) - 1);
                break;
            case DIPLOMAT: //Isole
            case HERBOLARIA: //isole
                answer.setData(chooseIsland(message.getIslands()) - 1);
                break;
            case CLOWN: //colori studenti della carta e studenti colori ingresso scuola
                if (!alreadyAskedMovements) {
                    System.out.println("How many students do you want to change?");
                    alreadyAskedMovements = true;
                    answer.setData(InputController.checkRange(1, 3));
                }
                //System.out.println(">Students on the Character Card: ");
                else {
                    answer.setParameter(chooseStudentColor(((CharacterCardwithStudents) characterCard).getStudents(),
                            true, ">Please choose the color of the student that you want to move from the Card [green/red/yellow/pink/blue]:"));
                    answer.setParameter(chooseStudentColor(message.getSchool().getEntrance(), true,

                            ">Please choose the color of the student that you want to move from your Entrance [green/red/yellow/pink/blue]:"));
                }
                break;
            case LUMBERJACK://colore a caso disponibile in character color
            case THIEF://colore casuale
                System.out.println("Choose a color [green/red/yellow/pink/blue]:");
                answer.setParameter(InputController.checkString(List.of("RED,PINK,GREEN,YELLOW,BLUE")));
                break;
            case PERFORMER://colori della carta e nell'ingresso
                if (!alreadyAskedMovements) {
                    System.out.println("How many students do you want to change?");
                    answer.setData(InputController.checkRange(1, Math.min(message.getSchool().getNumDiningRoomStudents(), 2)));
                    alreadyAskedMovements = true;
                } else {
                    answer.setParameter(chooseStudentColor(message.getSchool().getEntrance(), false,
                            ">Choose the student that you want to move from your Entrance to your Dining Room [green/red/yellow/pink/blue]:"));
                    System.out.println(">Choose the student that you want to move from your Dining Room to your Entrance [green/red/yellow/pink/blue]:");
                    parameter = reader.nextLine().toUpperCase();
                    while (message.getSchool().getDiningRoom().get(CharacterColor.valueOf(parameter)).size() < 1) {
                        System.out.println(">Invalid input. Please try again");
                        parameter = reader.nextLine().toUpperCase();
                    /*if (message.getSchool().getDiningRoom().get(CharacterColor.valueOf(parameter)).size() < 1)
                        Constants.clearRowBelow(2);*/
                    }
                    answer.setParameter(parameter);
                }
                break;
            case QUEEN: //colori carta
                answer.setParameter(chooseStudentColor(((CharacterCardwithStudents) characterCard).getStudents(),
                        false, ">Please choose the color of the student that you want to move from the Card [green/red/yellow/pink/blue]:"));
                break;
        }
        connection.send(answer);
    }

    /**
     * This method returns the id of the chosen island, checking that the input is in the established range
     *
     * @param islands list of island from which to choose the desired one
     * @return the island id
     */
    private int chooseIsland(List<Island> islands) {
        System.out.println(">Choose an island: ");
        return InputController.checkRange(1, islands.size());
    }

    private String chooseStudentColor(List<Student> students, boolean enablePrint, String message) {
        List<String> availableStudentsColors = new ArrayList<>();
        for (Student s : students) {
            availableStudentsColors.add(s.getColor().toString());
        }
        System.out.println(message);
        return InputController.checkString(availableStudentsColors);
    }

    private Thread clearBuffer;
    private final InputStreamReader inputStreamReader = new InputStreamReader(System.in);

    private void startClearBuffer() {
        synchronized (this) {
            print = true;
            clearBuffer = new Thread(() -> {
                String result;
                Scanner scanner = new Scanner(System.in);
                while (true) {
                    try {
                        if (inputStreamReader.ready()) {
                            result = scanner.nextLine();
                            if (result != null && result.equalsIgnoreCase("quit"))
                                connection.send(new InfoMessage("QUIT", false));
                            else if (result != null && print)
                                System.out.println("It is not your turn.Please wait.");
                        }
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            });
            clearBuffer.start();
        }
    }

    private synchronized void stopClearBuffer() {
        if (clearBuffer != null && clearBuffer.isAlive()) {
            clearBuffer.interrupt();
            clearBuffer = null;
            print = false;
        }
    }
}

