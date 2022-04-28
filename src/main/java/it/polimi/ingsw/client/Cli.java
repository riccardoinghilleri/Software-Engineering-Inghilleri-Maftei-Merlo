package it.polimi.ingsw.client;

import java.io.PrintStream;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import it.polimi.ingsw.controller.Action;
import it.polimi.ingsw.server.ConnectionMessage.*;
import it.polimi.ingsw.server.model.enums.CharacterColor;

public class Cli implements Runnable{
    private PrintStream printer;
    private Scanner reader;
    private boolean activeGame;

    private static int port;
    private static String address;

    private ClientConnection connection;

    //private static ConnectionSettings settings;
    private boolean expertMode;

    public Cli() {
        reader = new Scanner(System.in);
        printer = new PrintStream(System.out);
        connection = new ClientConnection(this);
        Thread t = new Thread(connection);
        t.start();
        activeGame = true;
    }

    public static int getPort() {
        return port;
    }

    public static String getAddress() {
        return address;
    }

    public static void main(String[] args) {
        Pattern pattern = Pattern.compile("^(([0-9]|[1-9][0-9]|1[0-9][0-9]|2[0-4][0-9]|25[0-5])(\\.(?!$)|$)){4}$");
        System.out.println(
                        " ███████╗ ██████═╗ ██╗     ██╗     ██╗   ██╗ ██████╗ ██   ██ ███████╗\n" +
                        " ██ ════╝ ██║  ██║ ██║    ████╗    ███╗  ██║   ██══╝  ██ ██╝ ██ ════╝\n" +
                        " ███████╗ ██████═╝ ██║   ██║ ██╗   ██║██╗██║   ██║     ██╝   ███████╗\n" +
                        " ██ ════╝ ██║ ██╗  ██║  ██ ██ ██╗  ██║  ███║   ██║    ██╝         ██║\n" +
                        " ███████╗ ██║  ██╗ ██║ ██╗     ██╗ ██║   ██║   ██║   ██╝     ███████║\n" +
                        " ╚══════╝ ╚═╝  ╚═╝ ╚═╝ ╚═╝     ╚═╝ ╚═╝   ╚═╝   ╚═╝   ╚╝      ╚══════╝\n");
        System.out.println("Inghilleri Riccardo - Maftei Daniela - Merlo Manuela");
        //settings = new ConnectionSettings();
        Scanner scanner = new Scanner(System.in);
        System.out.println("Insert the server IP address");
        address = scanner.nextLine();
        Matcher m = pattern.matcher(address);
        while (!m.matches()) {
            System.out.println("Invalid input. Please try again");
            address = scanner.nextLine();
            m = pattern.matcher(address);
        }
        //settings.setAddress(address);
        System.out.println("Insert the server port");
        port = Integer.parseInt(scanner.nextLine());
        while (port < 1024 || port > 65535) {
            System.out.println("Invalid input. Please try again");
            port = Integer.parseInt(scanner.nextLine());
        }
        //settings.setPort(port);
        Cli cli = new Cli();
        Thread t=new Thread(cli);
        t.start();
        cli.setupGameSetting();
    }
    @Override
    public void run(){
        while(connection.isActive()){

        }
    }

    private void setupGameSetting() {
        System.out.println(">Choose number of players [2/3]: ");
        int playersNumber = Integer.parseInt(reader.nextLine());
        while (playersNumber != 2 && playersNumber != 3) {
            System.out.println(">Invalid input. Try again. ");
            System.out.println(">Choose number of players [2/3]: ");
            playersNumber = Integer.parseInt(reader.nextLine());
        }
        System.out.println(">Do you want to play in ExpertMode? [y/n]: ");
        String response = reader.nextLine();
        while (!response.equalsIgnoreCase("y") && !response.equalsIgnoreCase("n")) {
            System.out.println(">Invalid input. Try again. ");
            System.out.println(">Do you want to play in ExpertMode? [y/n]:");
            response = reader.nextLine();
        }
        this.expertMode = response.equalsIgnoreCase("y");
        connection.send(new SettingsMessage(playersNumber, this.expertMode));
    }

    public void setupNickname(NicknameMessage message) {
        String response;
        String nickname;
        System.out.println(message.getMessage());
        do {
            System.out.println(">Please choose a nickname: ");
            nickname = reader.nextLine();
            System.out.println(">Are you sure about your choice? [y/n]: ");
            response = checkYNInput();
        } while (response.equalsIgnoreCase("n"));
        connection.send(new SetupMessage(nickname));
    }

    //metodo utilizzando per la scelta dei colori e del wizard
    public void setupMultipleChoice(MultipleChoiceMessage message) {
        String choice;
        String response;
        System.out.println(message.getString());
        System.out.println(">These are the available choices:");
        for (String s : message.getAvailableChoices())
            System.out.println(s + "\t");
        choice = reader.nextLine();
        if (!message.getAvailableChoices().contains(choice)) {
            System.out.println(">InvalidInput");
            setupMultipleChoice(message);
        } else connection.send(new SetupMessage(choice));
    }

    public void displayInfo(InfoMessage message) {
        System.out.println(message.getString());
    }

    public void askAction(AskActionMessage message) {
        String response;
        if (expertMode) {
            System.out.println("Do you want to use a Character Card?");
            response = checkYNInput();
            if (response.equalsIgnoreCase("y")) {
                manageCharacterCardChoice();
                //TODO anche le character card con più mosse vanno gestite "1 volta", cioè le mosse vanno fatte tutte insieme. Togliere le charactercard movements dal controller
            }
        }
        AskActionMessage m = (AskActionMessage) message;
        int data;
        String firstParameter, secondParameter, temp;
        ActionMessage answer = new ActionMessage();
        switch (m.getAction()) {
            case CHOOSE_ASSISTANT_CARD:
                System.out.println("Please choose your assistant card.");
                System.out.println(">These are the available choices:");
                for (int i = 0; i < m.getAvailability().size(); i++)
                    System.out.println("> " + i + ": " + m.getAvailability().get(i));
                data = reader.nextInt();
                while (data < 1 || data > m.getAvailability().size()) {
                    System.out.println(">Invalid input. Please try again");
                    data = reader.nextInt();
                }
                answer.setAction(Action.CHOOSE_ASSISTANT_CARD);
                answer.setData(data);
                connection.send(answer);
                break;
            case DEFAULT_MOVEMENTS:
                System.out.println("Please choose the color of the student that you want to move.");
                System.out.println(">These are your entrance's students:");
                for (Object s : m.getAvailability())
                    System.out.println(s.toString() + "\t");
                firstParameter = reader.nextLine();
                while (!m.getAvailability().contains(CharacterColor.valueOf(firstParameter))) {
                    System.out.println(">Invalid input. Please try again");
                    firstParameter = reader.nextLine();
                }
                answer.setAction(Action.DEFAULT_MOVEMENTS);
                answer.setFirstParameter(firstParameter);
                do {
                    System.out.println(">Do you want to move your student to your DiningRoom" +
                            "or on an Island?");
                    temp = reader.nextLine();
                    if (temp.equalsIgnoreCase("DiningRoom")) {
                        connection.send(answer);
                        break;
                    } else if (temp.equalsIgnoreCase("Island")) {
                        System.out.println(">Choose an Island: ");
                        data = reader.nextInt();
                        while (data < 1 || data > m.getData()) {
                            System.out.println(">Invalid input. Please try again");
                            data = reader.nextInt();
                        }
                        answer.setData(data - 1);
                        connection.send(answer);
                        break;
                    }
                } while (!temp.equalsIgnoreCase("DiningRoom") && !temp.equalsIgnoreCase("Island"));
                break;

            case MOVE_MOTHER_NATURE:
                System.out.println("Please choose how many steps you want nature mother do");
                System.out.println(">You can move mother nature" + m.getData() + " steps far.");
                System.out.println(">Please, make your choice:");
                data = reader.nextInt();
                while (data < 1 || data > m.getData()) {
                    System.out.println(">Invalid input. Please try again");
                    data = reader.nextInt();
                }
                answer.setAction(Action.MOVE_MOTHER_NATURE);
                answer.setData(data);
                connection.send(answer);
                break;
            case CHOOSE_CLOUD:
                System.out.println("Please choose your cloud.");
                System.out.println(">These are the available choices:");
                for (Object i : m.getAvailability())
                    System.out.println(i + "\t");
                data = reader.nextInt();
                while (!m.getAvailability().contains(data)) {
                    System.out.println(">Invalid input. Please try again");
                    data = reader.nextInt();
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
            System.out.println(">Invalid input.Please try again.");
            System.out.println(">Are you sure about your choice? [y/n]: ");
            response = reader.nextLine();
        }
        return response;
    }

    private void manageCharacterCardChoice() {
        System.out.println(">These are the available cards:");
    }
}



