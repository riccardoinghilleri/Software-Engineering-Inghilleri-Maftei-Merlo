package it.polimi.ingsw.server;

import it.polimi.ingsw.client.Cli;
import it.polimi.ingsw.server.ConnectionMessage.InfoMessage;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.locks.ReentrantLock;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Server implements Runnable {
    private static int port;
    private int currentGameId;

    private ServerSocket serverSocket;

    private final List<GameHandler> activeGames;
    private final List<VirtualView> twoPlayersNormal;
    private final List<VirtualView> twoPlayersExpert;
    private final List<VirtualView> threePlayersNormal;
    private final List<VirtualView> threePlayersExpert;

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
    }

    public static void main(String[] args) {
        System.out.println(
                        " ███████╗ ██████═╗ ██╗     ██╗     ██╗   ██╗ ██████╗ ██   ██ ███████╗\n" +
                        " ██ ════╝ ██║  ██║ ██║    ████╗    ███╗  ██║   ██══╝  ██ ██╝ ██ ════╝\n" +
                        " ███████╗ ██████═╝ ██║   ██║ ██╗   ██║██╗██║   ██║     ██╝   ███████╗\n" +
                        " ██ ════╝ ██║ ██╗  ██║  ██ ██ ██╗  ██║  ███║   ██║    ██╝         ██║\n" +
                        " ███████╗ ██║  ██╗ ██║ ██╗     ██╗ ██║   ██║   ██║   ██╝     ███████║\n" +
                        " ╚══════╝ ╚═╝  ╚═╝ ╚═╝ ╚═╝     ╚═╝ ╚═╝   ╚═╝   ╚═╝   ╚╝      ╚══════╝\n");
        System.out.println("Inghilleri Riccardo - Maftei Daniela - Merlo Manuela");
        Scanner scanner = new Scanner(System.in);
        System.out.println("Welcome to the Eriantys' server.");
        System.out.println("Insert the server port");
        port = Integer.parseInt(scanner.nextLine());
        while (port < 1024 || port > 65535) {
            System.out.println("Invalid input. Please try again");
            port = Integer.parseInt(scanner.nextLine());
        }
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
                System.out.println("Connection done.");
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
        lockQueue.lock(); //Forse troppi controlli, si potrebbe migliorare
        try {
            if (playersNumber == 2) {
                if (!expertMode) {
                    twoPlayersNormal.add(client);
                    if (twoPlayersNormal.size() == 2) {
                        createGameHandler(1);
                    }
                } else {
                    twoPlayersExpert.add(client);
                    if (twoPlayersExpert.size() == 2) {
                        createGameHandler(2);
                    }
                }
            } else {
                if (!expertMode) {
                    threePlayersNormal.add(client);
                    if (threePlayersNormal.size() == 3) {
                        createGameHandler(3);
                    }
                } else {
                    threePlayersExpert.add(client);
                    if (threePlayersExpert.size() == 3) {
                        createGameHandler(4);
                    }
                }
            }
        } finally {
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
            lockGames.unlock();
        }
    }
}
