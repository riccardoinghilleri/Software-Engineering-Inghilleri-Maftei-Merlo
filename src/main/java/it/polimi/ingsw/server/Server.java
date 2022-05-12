package it.polimi.ingsw.server;

import it.polimi.ingsw.constants.Constants;
import it.polimi.ingsw.server.ConnectionMessage.InfoMessage;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.ReentrantLock;

public class Server implements Runnable {
    private static int port;
    private int currentGameId;

    private ServerSocket serverSocket;

    private final List<GameHandler> activeGames;
    private final List<VirtualView> twoPlayersNormal;
    private final List<VirtualView> twoPlayersExpert;
    private final List<VirtualView> threePlayersNormal;
    private final List<VirtualView> threePlayersExpert;
    private final List<VirtualView> fourPlayersNormal;
    private final List<VirtualView> fourPlayersExpert;

    //pool di thread che gestisce le connessioni dei client
    private final ExecutorService executor;

    //TODO per accedere alle code e alla lista dei Game potrebbe essere necessario lockare
    //un client potrebbe chiamare il metodo addClientConnection che accede ad una coda e il server potrebbe chiamare createGame
    ReentrantLock lockQueue = new ReentrantLock(true);
    ReentrantLock lockGames = new ReentrantLock(true);

    public Server() {
        this.executor = Executors.newCachedThreadPool(); //creo pool di thread concorrenti
        this.currentGameId = 1;
        this.activeGames = new ArrayList<>();
        this.twoPlayersNormal = new ArrayList<>();
        this.twoPlayersExpert = new ArrayList<>();
        this.threePlayersNormal = new ArrayList<>();
        this.threePlayersExpert = new ArrayList<>();
        this.fourPlayersNormal = new ArrayList<>();
        this.fourPlayersExpert = new ArrayList<>();
    }

    public static void main(String[] args) {
        Constants.clearScreen();
        System.out.println(Constants.ERIANTYS);
        System.out.println("Inghilleri Riccardo - Maftei Daniela - Merlo Manuela\n");
        Scanner scanner = new Scanner(System.in);
        System.out.println(">Welcome to the Eriantys' server.");
        System.out.println(">Insert the server port ");
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
        System.out.println(">Waiting for players...");
        Server server = new Server();
        Thread t = new Thread(server);
        t.start();
    }

    @Override
    public void run() {
        try {
            serverSocket = new ServerSocket(port);
            while (true) {
                Socket clientSocket = serverSocket.accept();
                VirtualView virtualView = new VirtualView(clientSocket, this);
                this.executor.execute(virtualView);
            }
        } catch (IOException e) {
            if (serverSocket != null && !serverSocket.isClosed()) {
                try {
                    serverSocket.close();
                } catch (IOException passBy) {
                    passBy.printStackTrace();
                }
            }
        }
    }

    public void addClientConnectionToQueue(VirtualView client, int playersNumber, boolean expertMode) {
        lockQueue.lock();
        try {
            if (playersNumber == 2) {
                if (!expertMode) {
                    twoPlayersNormal.add(client);
                    client.sendMessage(new InfoMessage(Constants.WAITING));
                    if (twoPlayersNormal.size() == 2)
                        createGameHandler(1);
                } else {
                    twoPlayersExpert.add(client);
                    client.sendMessage(new InfoMessage(Constants.WAITING));
                    if (twoPlayersExpert.size() == 2)
                        createGameHandler(2);
                }
            } else if (playersNumber == 3) {
                if (!expertMode) {
                    threePlayersNormal.add(client);
                    client.sendMessage(new InfoMessage(Constants.WAITING));
                    if (threePlayersNormal.size() == 3)
                        createGameHandler(3);
                } else {
                    threePlayersExpert.add(client);
                    client.sendMessage(new InfoMessage(Constants.WAITING));
                    if (threePlayersExpert.size() == 3)
                        createGameHandler(4);
                }
            } else {
                if (!expertMode) {
                    fourPlayersNormal.add(client);
                    client.sendMessage(new InfoMessage(Constants.WAITING));
                    if (fourPlayersNormal.size() == 4)
                        createGameHandler(5);
                } else {
                    threePlayersExpert.add(client);
                    client.sendMessage(new InfoMessage(Constants.WAITING));
                    if (fourPlayersExpert.size() == 4)
                        createGameHandler(6);
                }
            }
        } finally {
            System.out.println(this);
            lockQueue.unlock();
        }
    }

    public void createGameHandler(int queue) {
        lockGames.lock();
        try {
            switch (queue) {
                case 1:
                    activeGames.add(new GameHandler(currentGameId, false, twoPlayersNormal, this));
                    twoPlayersNormal.clear();
                    break;
                case 2:
                    activeGames.add(new GameHandler(currentGameId, true, twoPlayersExpert, this));
                    twoPlayersExpert.clear();
                    break;
                case 3:
                    activeGames.add(new GameHandler(currentGameId, false, threePlayersNormal, this));
                    threePlayersNormal.clear();
                    break;
                case 4:
                    activeGames.add(new GameHandler(currentGameId, true, threePlayersExpert, this));
                    threePlayersExpert.clear();
                    break;
                case 5:
                    activeGames.add(new GameHandler(currentGameId, false, fourPlayersNormal, this));
                    fourPlayersNormal.clear();
                    break;
                case 6:
                    activeGames.add(new GameHandler(currentGameId, true, fourPlayersExpert, this));
                    fourPlayersExpert.clear();
                    break;
            }
            currentGameId++;
        } finally {
            lockGames.unlock();
        }
        //il parametro queue indica da quale coda estrarre i client
        //rimuove le connessioni dalla queue e le passa al gameHandler
        //aggiorna il currentGameId
    }

    public void removeGameHandler(GameHandler gameHandler) {
        lockGames.lock();
        try {
            assert activeGames.contains(gameHandler);
            activeGames.remove(gameHandler);
        } finally {
            System.out.println(this);
            lockGames.unlock();
        }
    }

    @Override
    public String toString() {
        Constants.clearScreen();
        //Constants.clearScreen();
        System.out.println(Constants.SERVER_STATUS);
        //System.out.println("Inghilleri Riccardo - Maftei Daniela - Merlo Manuela\n");
        return "Queues' Status: \n" +
                "1) TwoPlayersNormal: " + twoPlayersNormal.size() + " players\n" +
                "2) TwoPlayersExpert: " + twoPlayersExpert.size() + " players\n" +
                "3) ThreePlayersNormal: " + threePlayersNormal.size() + " players\n" +
                "4) ThreePlayersExpert: " + threePlayersExpert.size() + " players\n" +
                "5) FourPlayersNormal: " + fourPlayersNormal.size() + " players\n" +
                "6) FourPlayersExpert: " + fourPlayersExpert.size() + " players\n" +
                "Active Games: " + activeGames.size();
    }
}
