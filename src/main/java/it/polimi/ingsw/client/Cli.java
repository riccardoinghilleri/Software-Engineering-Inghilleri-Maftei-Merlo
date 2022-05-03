package it.polimi.ingsw.client;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import it.polimi.ingsw.enums.Action;
import it.polimi.ingsw.server.ConnectionMessage.*;
import it.polimi.ingsw.server.model.CharacterCard;
import it.polimi.ingsw.server.model.CharacterCardwithStudents;
import it.polimi.ingsw.server.model.Island;
import it.polimi.ingsw.server.model.Student;
import it.polimi.ingsw.enums.CharacterColor;

public class Cli implements View {
    private final PrintStream printer;
    private final Scanner reader;
    private static int port;
    private static String address;

    private final ClientConnection connection;
    private boolean expertMode;

    private boolean alreadyAskedCard;
    private boolean alreadyAskedMovements;

    public Cli() {
        reader = new Scanner(System.in);
        InputController.setScanner(reader);
        printer = new PrintStream(System.out);
        InputController.setPrinter(printer);
        connection = new ClientConnection(this);
        alreadyAskedCard = false;
        alreadyAskedMovements = false;
        Thread t = new Thread(connection);
        t.start();
    }

    public static int getPort() {
        return port;
    }

    public static String getAddress() {
        return address;
    }

    public static void main(String[] args) {
        PrintStream printer = new PrintStream(System.out);
        Scanner scanner = new Scanner(System.in);
        Pattern pattern = Pattern.compile("^(([0-9]|[1-9][0-9]|1[0-9][0-9]|2[0-4][0-9]|25[0-5])(\\.(?!$)|$)){4}$");
        printer.println(
                        " ███████╗ ██████═╗ ██╗     ██╗     ██╗   ██╗ ████████╗ ██   ██ ███████╗\n" +
                        " ██╔════╝ ██╔══██║ ██║    ████╗    ███╗  ██║ ╚══██╔══╝  ██ ██╝ ██╔════╝\n" +
                        " ███████╗ ██████═╝ ██║   ██╔═██╗   ██╔██╗██║    ██║      ██╝   ███████╗\n" +
                        " ██╔════╝ ██╔══██╗ ██║  ██ ██ ██╗  ██║  ███║    ██║     ██╝    ╚════██║\n" +
                        " ███████╗ ██║  ██║ ██║ ██╔═════██╗ ██║   ██║    ██║    ██╝     ███████║\n" +
                        " ╚══════╝ ╚═╝  ╚═╝ ╚═╝ ╚═╝     ╚═╝ ╚═╝   ╚═╝    ╚═╝    ╚╝      ╚══════╝\n");
        printer.println("Inghilleri Riccardo - Maftei Daniela - Merlo Manuela\n");
        printer.println(">Insert the server IP address");
        address = scanner.nextLine();
        Matcher m = pattern.matcher(address);
        while (!m.matches()) {
            printer.println(">Invalid input. Please try again");
            address = scanner.nextLine();
            m = pattern.matcher(address);
        }
        printer.println(">Insert the server port");
        boolean error;
        do {
            try {
                port = Integer.parseInt(scanner.nextLine());
                error = false;
                while (port < 1024 || port > 65535) {
                    System.out.println(">Invalid input: you have to choose a number between 1024 and 65535. Please try again");
                    port = Integer.parseInt(scanner.nextLine());
                }
            } catch (NumberFormatException e) {
                System.out.println(">Invalid input: you have to insert a number. Please try again.");
                error = true;
            }
        } while (error);
        Cli cli = new Cli();
        cli.setupGameSetting();
    }

    private void setupGameSetting() {
        printer.println(">Choose number of players [2/3]: ");
        int playersNumber = InputController.checkParseInt();
        while (playersNumber != 2 && playersNumber != 3) {
            printer.println(">Invalid input. Try again. ");
            printer.println(">Choose number of players [2/3]: ");
            playersNumber = InputController.checkParseInt();
        }
        printer.println(">Do you want to play in ExpertMode? [y/n]: ");
        String response = InputController.checkYNInput();
        this.expertMode = response.equalsIgnoreCase("y");
        connection.send(new SettingsMessage(playersNumber, this.expertMode));
    }

    public void setupNickname(NicknameMessage message) {
        String response;
        String nickname;
        if (message.getAlreadyAsked())
            printer.println("The nickname is not available!");
        do {
            printer.println(">Please choose a nickname: ");
            nickname = reader.nextLine();
            printer.println(">Are you sure about your choice? [y/n]: ");
            response = InputController.checkYNInput();
        } while (response.equalsIgnoreCase("n"));
        connection.send(new SetupMessage(nickname));
    }

    //metodo utilizzando per la scelta dei colori e del wizard
    public void setupMultipleChoice(MultipleChoiceMessage message) {
        String choice;
        printer.println(">These are the available choices:");
        for (String s : message.getAvailableChoices())
            printer.println(s + "\t");
        choice = reader.nextLine().toUpperCase();
        while (!message.getAvailableChoices().contains(choice.toUpperCase())) {
            printer.println(">Invalid Input.Please try again.");
            choice = reader.nextLine().toUpperCase();
        }
        connection.send(new SetupMessage(choice));
    }

    public void displayInfo(InfoMessage message) {
        printer.println(message.getString());
    }

    public void askAction(AskActionMessage message) {
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
                    printer.println(">Please choose your assistant card priority. These are the available choices:");
                    for (int i = 0; i < message.getAvailableAssistantCards().size(); i++) {
                        printer.println(">" + message.getAvailableAssistantCards().get(i).toString());
                        availablePriority.add(message.getAvailableAssistantCards().get(i).getPriority());
                    }
                    alreadyAskedCard = true;
                }
                answer.setData(InputController.checkInt(availablePriority));
                answer.setAction(Action.CHOOSE_ASSISTANT_CARD);
                connection.send(answer);
                break;
            case CHOOSE_CHARACTER_CARD:
                alreadyAskedMovements=false;
                printer.println(">Do you want to use a Character Card? [y/n]");
                response = InputController.checkYNInput();
                answer.setAction(Action.CHOOSE_CHARACTER_CARD);
                if (response.equalsIgnoreCase("n")) {
                    answer.setCharacterCardName(null);
                } else {
                    printer.println(">These are the available Character Cards:");
                    List<String> characterCards = new ArrayList<>();
                    for (CharacterCard characterCard : message.getCharacterCards()) {
                        characterCards.add(characterCard.getName().toString());
                        printer.println(characterCard);
                    }
                    printer.println(">Make your choice: ");
                    String characterCardName = reader.nextLine().toUpperCase();
                    while (!characterCards.contains(characterCardName)) {
                        printer.println(">Invalid input. Please try again");
                        characterCardName = reader.nextLine().toUpperCase();
                    }
                    answer.setCharacterCardName(characterCardName);
                }
                connection.send(answer);
                break;
            case USE_CHARACTER_CARD:
                manageCharacterCardChoice(message);
                break;
            case DEFAULT_MOVEMENTS:
                alreadyAskedCard = false;
                answer.setAction(Action.DEFAULT_MOVEMENTS);
                printer.println(">These are your entrance's students:");
                answer.setParameter(chooseStudentColor(message.getSchool().getEntrance(), true,
                        ">Please choose the color of the student that you want to move from your Entrance:"));
                printer.println(">Do you want to move your student to your DiningRoom" +
                        " or on an Island?");
                temp = reader.nextLine();
                while (!temp.equalsIgnoreCase("DiningRoom") && !temp.equalsIgnoreCase("Island")){
                    printer.println(">Invalid input. Please, try again!");
                    temp = reader.nextLine();
                }
                if (temp.equalsIgnoreCase("Island")) {
                    answer.setData(chooseIsland(message.getIslands()) - 1);
                }
                connection.send(answer);
                break;
            case MOVE_MOTHER_NATURE:
                printer.println(">You can move mother nature " + message.getData() + " steps far.");
                printer.println(">Please choose how many steps you want mother nature do:");
                answer.setData(InputController.checkRange(1, message.getData()));
                answer.setAction(Action.MOVE_MOTHER_NATURE);
                connection.send(answer);
                break;
            case CHOOSE_CLOUD:
                printer.println(">These are the available clouds:");
                List<Integer> availableIndexClouds = new ArrayList<>();
                for (int i = 0; i < message.getClouds().length; i++) {
                    if (!(message.getClouds())[i].getStudents().isEmpty()) {
                        availableIndexClouds.add(i);
                        printer.println("CLOUD #" + i + "\n" + (message.getClouds())[i] + "\n");
                    }
                }
                printer.println(">Please choose your cloud.");
                answer.setData(InputController.checkInt(availableIndexClouds));
                answer.setAction(Action.CHOOSE_CLOUD);
                connection.send(answer);
                break;
        }
    }


    //Gestisce i parametri da settare in base alla character card
    private void manageCharacterCardChoice(AskActionMessage message) {
        ActionMessage answer = new ActionMessage();
        CharacterCard characterCard = message.getChosenCharacterCard();
        answer.setAction(Action.USE_CHARACTER_CARD);
        answer.setCharacterCardName(characterCard.getName().toString());
        String parameter;
        switch (characterCard.getName()) {
            case PRIEST: //isole e colore dello studente
                answer.setParameter(chooseStudentColor(((CharacterCardwithStudents) characterCard).getStudents(),
                        false, ">Please choose the color of the student that you want to move from the Card:"));
                answer.setData(chooseIsland(message.getIslands()) - 1);
                break;
            case DIPLOMAT: //Isole
            case HERBOLARIA: //isole
                answer.setData(chooseIsland(message.getIslands()) - 1);
                break;
            case CLOWN://colori studenti della carta e studenti colori ingresso scuola
                if (!alreadyAskedMovements) {
                    printer.println("How many students do you want to change?");
                    alreadyAskedMovements=true;
                    answer.setData(InputController.checkRange(1,3));
                }
                printer.println(">Students on the Character Card: ");
                answer.setParameter(chooseStudentColor(((CharacterCardwithStudents) characterCard).getStudents(),
                        true, ">Please choose the color of the student that you want to move from the Card:"));
                printer.println(">These are your entrance's students:");
                answer.setParameter(chooseStudentColor(message.getSchool().getEntrance(), true,
                        ">Please choose the color of the student that you want to move from your Entrance:"));
                break;
            case LUMBERJACK://colore a caso disponibile in character color
            case THIEF://colore casuale
                printer.println("Choose a color: ");
                parameter = reader.nextLine().toUpperCase();
                while (!Arrays.asList(CharacterColor.values()).contains(CharacterColor.valueOf(parameter))) {
                    printer.println(">Invalid input. Please try again");
                    parameter = reader.nextLine().toUpperCase();
                }
                answer.setParameter(parameter);
                break;
            case PERFORMER://colori della carta e nell?ingresso
                if (!alreadyAskedMovements) {
                    printer.println("How many students do you want to change?");
                    answer.setData(InputController.checkRange(1,2));
                }
                printer.println(">This is your School: ");
                printer.println(message.getSchool().toString());
                printer.println(">Choose the student that you want to move from your Dining Room to your Entrance: ");
                parameter = reader.nextLine().toUpperCase();
                while (message.getSchool().getDiningRoom().get(CharacterColor.valueOf(parameter)).size() < 1) {
                    printer.println(">Invalid input. Please try again");
                    parameter = reader.nextLine().toUpperCase();
                }
                answer.setParameter(parameter);
                answer.setParameter(chooseStudentColor(message.getSchool().getEntrance(), false,
                        ">Choose the student that you want to move from your Entrance to your Dining Room: "));
                break;
            case QUEEN://colori carta
                answer.setParameter(chooseStudentColor(((CharacterCardwithStudents) characterCard).getStudents(),
                        false, ">Please choose the color of the student that you want to move from the Card:"));
                break;
        }
        connection.send(answer);
    }

    private int chooseIsland(List<Island> islands) {
        for (int i = 1; i <= islands.size(); i++) {
            printer.println("#" + i + "\t" + islands.get(i - 1));
        }
        printer.println(">Choose an island: ");
        return InputController.checkRange(1, islands.size());
    }

    private String chooseStudentColor(List<Student> students, boolean enablePrint, String message) {
        List<String> availableStudentsColors = new ArrayList<>();
        for (Student s : students) {
            availableStudentsColors.add(s.getColor().toString());
            if (enablePrint)
                printer.println(s + "\t");
        }
        printer.println(message);
        String parameter = reader.nextLine().toUpperCase();
        while (!availableStudentsColors.contains(parameter)) {
            printer.println(">Invalid input. Please try again");
            parameter = reader.nextLine().toUpperCase();
        }
        return parameter;
    }
}
