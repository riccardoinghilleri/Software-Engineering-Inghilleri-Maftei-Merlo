package it.polimi.ingsw.client;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import it.polimi.ingsw.controller.Action;
import it.polimi.ingsw.server.ConnectionMessage.*;
import it.polimi.ingsw.server.model.CharacterCard;
import it.polimi.ingsw.server.model.CharacterCardwithStudents;
import it.polimi.ingsw.server.model.Student;
import it.polimi.ingsw.server.model.enums.CharacterColor;

public class Cli implements View {
    private final PrintStream printer;
    private final Scanner reader;
    private static int port;
    private static String address;

    private final ClientConnection connection;
    private boolean expertMode;

    private boolean alreadyAskedCard;

    public Cli() {
        reader = new Scanner(System.in);
        printer = new PrintStream(System.out);
        connection = new ClientConnection(this);
        alreadyAskedCard = false;
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
                " ███████╗ ██████═╗ ██╗     ██╗     ██╗   ██╗ ██████╗ ██   ██ ███████╗\n" +
                        " ██ ════╝ ██║  ██║ ██║    ████╗    ███╗  ██║   ██══╝  ██ ██╝ ██ ════╝\n" +
                        " ███████╗ ██████═╝ ██║   ██║ ██╗   ██║██╗██║   ██║     ██╝   ███████╗\n" +
                        " ██ ════╝ ██║ ██╗  ██║  ██ ██ ██╗  ██║  ███║   ██║    ██╝         ██║\n" +
                        " ███████╗ ██║  ██╗ ██║ ██╗     ██╗ ██║   ██║   ██║   ██╝     ███████║\n" +
                        " ╚══════╝ ╚═╝  ╚═╝ ╚═╝ ╚═╝     ╚═╝ ╚═╝   ╚═╝   ╚═╝   ╚╝      ╚══════╝\n");
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
        int playersNumber = checkParseInt();
        while (playersNumber != 2 && playersNumber != 3) {
            printer.println(">Invalid input. Try again. ");
            printer.println(">Choose number of players [2/3]: ");
            playersNumber = checkParseInt();
        }
        printer.println(">Do you want to play in ExpertMode? [y/n]: ");
        String response = reader.nextLine();
        while (!response.equalsIgnoreCase("y") && !response.equalsIgnoreCase("n")) {
            printer.println(">Invalid input. Try again. ");
            printer.println(">Do you want to play in ExpertMode? [y/n]:");
            response = reader.nextLine();
        }
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
            response = checkYNInput();
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
        if (!message.getAvailableChoices().contains(choice.toUpperCase())) {
            printer.println(">Invalid Input");
            setupMultipleChoice(message);
        } else connection.send(new SetupMessage(choice));
    }

    public void displayInfo(InfoMessage message) {
        printer.println(message.getString());
    }

    public void askAction(AskActionMessage message) {
        String response;
        int data;
        String parameter, temp;
        ActionMessage answer = new ActionMessage();
        switch (message.getAction()) {
            case CHOOSE_ASSISTANT_CARD:
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
                data = checkParseInt();
                while (!availablePriority.contains(data)) {
                    printer.println(">Invalid input. Please try again");
                    data = checkParseInt();
                }
                answer.setAction(Action.CHOOSE_ASSISTANT_CARD);
                answer.setData(data);
                connection.send(answer);
                break;
            case USE_CHARACTER_CARD:
                printer.println(">Do you want to use a Character Card? [y/n]");
                response = checkYNInput();
                answer.setAction(Action.USE_CHARACTER_CARD);
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
                    for (CharacterCard c : message.getCharacterCards()) {
                        if (c.getName().toString().equalsIgnoreCase(characterCardName)) {
                            manageCharacterCardChoice(message, c, answer);
                            break;
                        }
                    }
                }
                connection.send(answer);
                break;
            case DEFAULT_MOVEMENTS:
                alreadyAskedCard = false;
                printer.println(">These are your entrance's students:");
                List<CharacterColor> availableStudentsColors = new ArrayList<>();
                for (Student s : message.getSchool().getEntrance()) {
                    availableStudentsColors.add(s.getColor());
                    printer.println(s + "\t");
                }
                printer.println("Please choose the color of the student that you want to move.");
                parameter = reader.nextLine().toUpperCase();
                while (!availableStudentsColors.contains(CharacterColor.valueOf(parameter.toUpperCase()))) {
                    printer.println(">Invalid input. Please try again");
                    parameter = reader.nextLine().toUpperCase();
                }
                answer.setAction(Action.DEFAULT_MOVEMENTS);
                answer.setParameter(parameter);
                do {
                    printer.println(">Do you want to move your student to your DiningRoom" +
                            " or on an Island?");
                    temp = reader.nextLine();
                    if (temp.equalsIgnoreCase("Island")) {
                        for (int i = 1; i <= message.getIslands().size(); i++) {
                            printer.println("#" + i + "\t" + message.getIslands().get(i - 1));
                        }
                        printer.println(">Choose an Island: ");
                        data = checkParseInt();
                        while (data < 1 || data > message.getIslands().size()) {
                            printer.println(">Invalid input. Please try again");
                            data = checkParseInt();
                        }
                        answer.setData(data - 1);
                    }
                } while (!temp.equalsIgnoreCase("DiningRoom") && !temp.equalsIgnoreCase("Island"));
                connection.send(answer);
                break;
            case MOVE_MOTHER_NATURE:
                printer.println(">You can move mother nature " + message.getData() + " steps far.");
                printer.println(">Please choose how many steps you want mother nature do:");
                data = checkParseInt();
                while (data < 1 || data > message.getData()) {
                    printer.println(">Invalid input. Please try again.");
                    data = checkParseInt();
                }
                answer.setAction(Action.MOVE_MOTHER_NATURE);
                answer.setData(data);
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
                data = checkParseInt();
                while (!availableIndexClouds.contains(data)) {
                    printer.println(">Invalid input. Please try again.");
                    data = checkParseInt();
                }
                answer.setAction(Action.CHOOSE_CLOUD);
                answer.setData(data);
                connection.send(answer);
                break;
        }
    }

    private String checkYNInput() {
        String response = reader.nextLine();
        while (!response.equalsIgnoreCase("y") && !response.equalsIgnoreCase("n")) {
            printer.println(">Invalid input.Please try again.");
            printer.println(">Are you sure about your choice? [y/n]: ");
            response = reader.nextLine();
        }
        return response;
    }

    //Gestisce i parametri da settare in base alla character card
    private void manageCharacterCardChoice(AskActionMessage message, CharacterCard characterCard, ActionMessage answer) {
        int data;
        String parameter;
        boolean error = true;
        switch (characterCard.getName()) {
            case PRIEST: //isole e colore dello studente
                printer.println(">Choose the student that you want to move: ");
                do {
                    parameter = reader.nextLine().toUpperCase();
                    for (Student s : ((CharacterCardwithStudents) characterCard).getStudents()) {
                        if (s.getColor().toString().equalsIgnoreCase(parameter)) {
                            error = false;
                        }
                    }
                    if (error) {
                        printer.println(">Invalid input. Please try again.");
                    }
                } while (error);
                answer.setParameter(parameter);
                for (int i = 1; i <= message.getIslands().size(); i++) {
                    printer.println("#" + i + "\t" + message.getIslands().get(i - 1));
                }
                printer.println(">Choose an island: ");
                data = checkParseInt();
                while (data < 1 || data > message.getIslands().size()) {
                    printer.println(">Invalid input. Please try again");
                    data = checkParseInt();
                }
                answer.setData(data - 1);
                break;
            case DIPLOMAT: //Isole
                for (int i = 1; i <= message.getIslands().size(); i++) {
                    printer.println("#" + i + "\t" + message.getIslands().get(i - 1));
                }
                printer.println(">Choose an island: ");
                data = checkParseInt();
                while (data < 1 || data > message.getIslands().size()) {
                    printer.println(">Invalid input. Please try again");
                    data = checkParseInt();
                }
                answer.setData(data - 1);
                break;
            case HERBOLARIA: //isole
                for (int i = 1; i <= message.getIslands().size(); i++) {
                    printer.println("#" + i + "\t" + message.getIslands().get(i - 1));
                }
                printer.println(">Choose an island: ");
                data = checkParseInt();
                while (data < 1 || data > message.getIslands().size()) {
                    printer.println(">Invalid input. Please try again");
                    data = checkParseInt();
                }
                answer.setData(data - 1);
                break;
            case CLOWN://colori studenti della carta e studenti colori ingresso scuola
                printer.println("How many students do you want to change?");
                do {
                    data = checkParseInt();
                } while (data < 1 || data > 3);
                for (int i = 0; i < data; i++) {
                    printer.println(">Students on the Character Card: ");
                    for (Student s : ((CharacterCardwithStudents) characterCard).getStudents()) {
                        printer.println(s);
                    }
                    printer.println(">Choose the student that you want to move from the card: ");
                    error = true;
                    do {
                        parameter = reader.nextLine().toUpperCase();
                        for (Student s : ((CharacterCardwithStudents) characterCard).getStudents()) {
                            if (s.getColor().toString().equalsIgnoreCase(parameter)) {
                                error = false;
                            }
                        }
                        if (error) {
                            printer.println(">Invalid input. Please try again.");
                        }
                    } while (error);
                    answer.setParameter(parameter);
                    printer.println(">Students in your Entrance: ");
                    for (Student s : message.getSchool().getEntrance()) {
                        printer.println(s);
                    }
                    printer.println(">Choose the student that you want to move from your Entrance: ");
                    List<CharacterColor> availableStudentsColors = new ArrayList<>();
                    for (Student s : message.getSchool().getEntrance()) {
                        availableStudentsColors.add(s.getColor());
                        printer.println(s + "\t");
                    }
                    parameter = reader.nextLine().toUpperCase();
                    while (!availableStudentsColors.contains(CharacterColor.valueOf(parameter.toUpperCase()))) {
                        printer.println(">Invalid input. Please try again");
                        parameter = reader.nextLine().toUpperCase();
                    }
                    answer.setParameter(parameter);
                }
                break;
            case LUMBERJACK://colore a caso disponibile in character color
                printer.println("Choose a color: ");
                parameter = reader.nextLine().toUpperCase();
                while (!Arrays.asList(CharacterColor.values()).contains(CharacterColor.valueOf(parameter))) {
                    printer.println(">Invalid input. Please try again");
                    parameter = reader.nextLine().toUpperCase();
                }
                answer.setParameter(parameter);
                break;
            case PERFORMER://colori della carta e nell?ingresso
                printer.println("How many students do you want to change?");
                do {
                    data = checkParseInt();
                } while (data < 1 || data > 2);
                for (int i = 0; i < data; i++) {
                    printer.println(">This is your School: ");
                    printer.println(message.getSchool().toString());
                    printer.println(">Choose the student that you want to move from your Dining Room to your Entrance: ");
                    parameter = reader.nextLine().toUpperCase();
                    while (message.getSchool().getDiningRoom().get(CharacterColor.valueOf(parameter)).size() < 1) {
                        printer.println(">Invalid input. Please try again");
                        parameter = reader.nextLine().toUpperCase();
                    }
                    answer.setParameter(parameter);
                    printer.println(">Choose the student that you want to move from your Entrance to your Dining Room: ");
                    List<CharacterColor> availableStudentsColors = new ArrayList<>();
                    for (Student s : message.getSchool().getEntrance()) {
                        availableStudentsColors.add(s.getColor());
                    }
                    parameter = reader.nextLine().toUpperCase();
                    while (!availableStudentsColors.contains(CharacterColor.valueOf(parameter.toUpperCase()))) {
                        printer.println(">Invalid input. Please try again");
                        parameter = reader.nextLine().toUpperCase();
                    }
                    answer.setParameter(parameter);
                }
                break;
            case QUEEN://colori carta
                printer.println(">Choose the student that you want to move from the card: ");
                do {
                    parameter = reader.nextLine().toUpperCase();
                    for (Student s : ((CharacterCardwithStudents) characterCard).getStudents()) {
                        if (s.getColor().toString().equalsIgnoreCase(parameter)) {
                            error = false;
                        }
                    }
                    if (error) {
                        printer.println(">Invalid input. Please try again.");
                    }
                } while (error);
                answer.setParameter(parameter);
                break;
            case THIEF://colore casuale
                printer.println("Choose a color: ");
                parameter = reader.nextLine().toUpperCase();
                while (!Arrays.asList(CharacterColor.values()).contains(CharacterColor.valueOf(parameter))) {
                    printer.println(">Invalid input. Please try again");
                    parameter = reader.nextLine().toUpperCase();
                }
                answer.setParameter(parameter);
                break;
        }
    }

    private int checkParseInt() {
        int result = -1;
        boolean error;
        do {
            try {
                result = Integer.parseInt(reader.nextLine());
                error = false;
            } catch (NumberFormatException e) {
                System.out.println(">Invalid input: you have to insert a number. Please try again.");
                error = true;
            }
        } while (error);
        return result;
    }
}




