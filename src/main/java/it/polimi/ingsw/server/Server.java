package it.polimi.ingsw.server;

import java.net.ServerSocket;
import java.util.List;
import java.util.concurrent.ExecutorService;
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

    public Server(int port){

    }

    public void StartSever(){

    }
    public void addClientConnectionToQueue(ClientConnection client,int playersNumber,boolean expertMode){

    }
    public void createGameHandler(int queue){
        //il parametro queue indica da quale coda estrarre i client
        //rimuove le connessioni dalla queue e le passa al gameHandler
        //aggiorna il currentGameId
    }

    public void removeGameHandler(GameHandler gameHandler){

    }
}
