package it.polimi.ingsw.client;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import it.polimi.ingsw.controller.Action;
import it.polimi.ingsw.server.ConnectionMessage.*;
import it.polimi.ingsw.server.model.Student;
import it.polimi.ingsw.server.model.enums.CharacterColor;

public class Cli implements View{
    private final PrintStream printer;
    private Scanner reader;
    private static int port;
    private static String address;

    private final ClientConnection connection;
    private boolean expertMode;

    public Cli() {
        reader = new Scanner(System.in);
        printer = new PrintStream(System.out);
        connection = new ClientConnection(this);
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
        port = Integer.parseInt(scanner.nextLine());
        while (port < 1024 || port > 65535) {
            printer.println(">Invalid input. Please try again");
            port = Integer.parseInt(scanner.nextLine());
        }
        Cli cli = new Cli();
        cli.setupGameSetting();
    }

    private void setupGameSetting() {
        printer.println(">Choose number of players [2/3]: ");
        int playersNumber = Integer.parseInt(reader.nextLine());
        while (playersNumber != 2 && playersNumber != 3) {
            printer.println(">Invalid input. Try again. ");
            printer.println(">Choose number of players [2/3]: ");
            playersNumber = Integer.parseInt(reader.nextLine());
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
        String nickname = null;
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
        printer.println(message.getString());
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
        if (expertMode && message.getAction() != Action.CHOOSE_ASSISTANT_CARD) {
            printer.println("Do you want to use a Character Card?");
            response = checkYNInput();
            if (response.equalsIgnoreCase("y")) {
                manageCharacterCardChoice();
                //TODO anche le character card con più mosse vanno gestite "1 volta", cioè le mosse vanno fatte tutte insieme. Togliere le charactercard movements dal controller
            }
        }
        int data;
        String firstParameter, secondParameter, temp;
        ActionMessage answer = new ActionMessage();
        switch (message.getAction()) {
            case CHOOSE_ASSISTANT_CARD:
                printer.println("Please choose your assistant card priority. These are the available choices:");
                List<Integer> availablePriority = new ArrayList<>();
                for (int i = 0; i < message.getAvailableAssistantCards().size(); i++) {
                    printer.println(">" + message.getAvailableAssistantCards().get(i).toString());
                    availablePriority.add(message.getAvailableAssistantCards().get(i).getPriority());
                }
                data = Integer.parseInt(reader.nextLine());
                while (!availablePriority.contains(data)) {
                    printer.println(">Invalid input. Please try again");
                    data = Integer.parseInt(reader.nextLine());
                }
                answer.setAction(Action.CHOOSE_ASSISTANT_CARD);
                answer.setData(data);
                connection.send(answer);
                break;
            case DEFAULT_MOVEMENTS:
                printer.println("Please choose the color of the student that you want to move.");
                printer.println(">These are your entrance's students:");
                List<CharacterColor> availableStudentsColors = new ArrayList<>();
                for (Student s : message.getAvailableStudents()) {
                    availableStudentsColors.add(s.getColor());
                    printer.println(s + "\t");
                }
                firstParameter = reader.nextLine().toUpperCase();
                while (!availableStudentsColors.contains(CharacterColor.valueOf(firstParameter.toUpperCase()))) {
                    printer.println(">Invalid input. Please try again");
                    firstParameter = reader.nextLine().toUpperCase();
                }
                answer.setAction(Action.DEFAULT_MOVEMENTS);
                answer.setFirstParameter(firstParameter);
                do {
                    printer.println(">Do you want to move your student to your DiningRoom" +
                            " or on an Island?");
                    temp = reader.nextLine();
                    if (temp.equalsIgnoreCase("DiningRoom")) {
                        connection.send(answer);
                        break;
                    } else if (temp.equalsIgnoreCase("Island")) {
                        for (int i = 1; i <= message.getData(); i++) {
                            printer.println("Island: #" + i);
                        }
                        printer.println(">Choose an Island: ");
                        data = Integer.parseInt(reader.nextLine());
                        while (data < 1 || data > message.getData()) {
                            printer.println(">Invalid input. Please try again");
                            data = Integer.parseInt(reader.nextLine());
                        }
                        answer.setData(data - 1);
                        connection.send(answer);
                        break;
                    }
                } while (!temp.equalsIgnoreCase("DiningRoom") && !temp.equalsIgnoreCase("Island"));
                break;

            case MOVE_MOTHER_NATURE:
                printer.println(">You can move mother nature " + message.getData() + " steps far.");
                printer.println(">Please choose how many steps you want mother nature do:");
                data = Integer.parseInt(reader.nextLine());
                while (data < 1 || data > message.getData()) {
                    printer.println(">Invalid input. Please try again.");
                    data = Integer.parseInt(reader.nextLine());
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
                data = Integer.parseInt(reader.nextLine());
                while (!availableIndexClouds.contains(data)) {
                    printer.println(">Invalid input. Please try again.");
                    data = Integer.parseInt(reader.nextLine());
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

    private void manageCharacterCardChoice() {
        printer.println(">These are the available cards:");
    }

}




