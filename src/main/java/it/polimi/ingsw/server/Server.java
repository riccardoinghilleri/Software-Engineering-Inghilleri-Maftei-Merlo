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
    private List<ClientConnection> twoPlayersNormal;
    private List<ClientConnection> twoPlayersExpert;
    private List<ClientConnection> threePlayersNormal;
    private List<ClientConnection> threePlayersExpert;

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
                ClientConnection clientConnection = new ClientConnection(clientSocket, this);
                executor.submit(clientConnection);
            }
        } catch (IOException e) {
            if (serverSocket != null && !serverSocket.isClosed()) {
                try {
                    serverSocket.close();
                } catch (IOException passBy) {
                }
            }
        }

    }


    public void addClientConnectionToQueue(ClientConnection client, int playersNumber, boolean expertMode) {
        lockQueue.lock(); //manca costrutto try  . Forse troppi controlli, si potrebbe migliorare
        if (playersNumber == 2) {
            if (!expertMode) {
                twoPlayersNormal.add(client);
                if (twoPlayersNormal.size() == 2) {
                    createGameHandler(1); // dopo aver raggiunto 2 giocatori,devo rimuovere connessioni dalla lista
                }
            }
            else {
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
        lockQueue.unlock();
    }



    public void createGameHandler(int queue){
         lockGames.lock(); //mancano i try e catch e in più non sicura sia giusto l'approccio
        switch (queue) {
            case 1 -> {
                GameHandler gameHandler1 = new GameHandler(currentGameId, false, twoPlayersNormal, this);
                activeGames.add(gameHandler1);
                twoPlayersNormal.clear();

            }
            case 2 -> {
                GameHandler gameHandler2 = new GameHandler(currentGameId, true, twoPlayersExpert, this);
                activeGames.add(gameHandler2);
                twoPlayersExpert.clear();

            }
            case 3 -> {
                GameHandler gameHandler3 = new GameHandler(currentGameId, false, threePlayersNormal, this);
                activeGames.add(gameHandler3);
                threePlayersNormal.clear();
            }
            case 4 -> {
                GameHandler gameHandler4 = new GameHandler(currentGameId, true, threePlayersExpert, this);
                activeGames.add(gameHandler4);
                threePlayersExpert.clear();
            }
        }
            currentGameId++;
            lockGames.unlock();

            //il parametro queue indica da quale coda estrarre i client
        //rimuove le connessioni dalla queue e le passa al gameHandler
        //aggiorna il currentGameId
    }

    private void removeGameHandler(GameHandler gameHandler){
        lockGames.lock();
        try {
            assert activeGames.contains(gameHandler);
            activeGames.remove(gameHandler);
        } finally {
            lockGames.unlock();
        }
    }
}
