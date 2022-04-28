package it.polimi.ingsw.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.ReentrantLock;

public class Server {
    private int port;
    private int currentGameId;

    private ServerSocket serverSocket;

    private List<GameHandler> activeGames;
    private List<VirtualView> twoPlayersNormal;
    private List<VirtualView> twoPlayersExpert;
    private List<VirtualView> threePlayersNormal;
    private List<VirtualView> threePlayersExpert;

    //pool di thread che gestisce le connessioni dei client
    private ExecutorService executor;

    //TODO per accedere alle code e alla lista dei Game potrebbe essere necessario Lockare
    //un client potrebbe chiamare il metodo addClientConnection che accede ad una coda e il server potrebbe chiammare createGame
    ReentrantLock lockQueue = new ReentrantLock(true);
    ReentrantLock lockGames = new ReentrantLock(true);

    public Server(int port) {
        this.port = port;
        this.executor = Executors.newCachedThreadPool(); //creo pool di thread concorrenti
        this.currentGameId = 1;
        this.activeGames = new ArrayList<>();
        this.twoPlayersNormal = new ArrayList<>();
        this.twoPlayersExpert = new ArrayList<>();
        this.threePlayersNormal = new ArrayList<>();
        this.threePlayersExpert = new ArrayList<>();
    }

    public void StartSever() {
        try {
            serverSocket = new ServerSocket(port);
        } catch (IOException e) {
            return;
        }
        try {
            while (true) {
                Socket clientSocket = serverSocket.accept();
                VirtualView virtualView = new VirtualView(clientSocket, this);
                executor.submit(virtualView);
            }
        } catch (IOException e) {
            if (serverSocket != null && !serverSocket.isClosed()) {
                try {
                    serverSocket.close();
                } catch (IOException passBy) { }
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
        }
        finally {  lockQueue.unlock();}
    }


    public void createGameHandler(int queue){
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
        }
        finally {lockGames.unlock();}

        //il parametro queue indica da quale coda estrarre i client
        //rimuove le connessioni dalla queue e le passa al gameHandler
        //aggiorna il currentGameId
    }

    public void removeGameHandler(GameHandler gameHandler){
        lockGames.lock();
        try {
            assert activeGames.contains(gameHandler);
            activeGames.remove(gameHandler);
        } finally {
            lockGames.unlock();
        }
    }
}
