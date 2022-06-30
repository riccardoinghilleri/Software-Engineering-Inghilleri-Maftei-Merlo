package it.polimi.ingsw.server;

import it.polimi.ingsw.controller.GameHandler;
import it.polimi.ingsw.server.ConnectionMessage.*;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * This class  communicates with the ClientConnection through the socket created by the server.
 * It forwards messages based on the value of an inGame attribute either to the server or gameHandler.
 * if inGame == false, the messages are forwarded to the server, else to the gameHandler.
 */
public class VirtualView implements Runnable {
    private int clientId;
    private boolean inGame;

    private boolean alreadySettings;

    private final AtomicBoolean active, closed;
    private final AtomicBoolean ping_response;

    private final Socket socket;
    private final Server server;
    private GameHandler gameHandler;
    private final ObjectInputStream is;
    private final ObjectOutputStream os;

    //private Thread timer;

    /**
     * The constructor of the class.
     * @param server instance of server object
     * @param socket instance of socket object
     */
    public VirtualView(Socket socket, Server server) throws IOException {
        this.socket = socket;
        this.clientId = -1;
        this.inGame = false;
        this.server = server;
        this.alreadySettings = false;
        active = new AtomicBoolean(false);
        closed = new AtomicBoolean(false);
        os = new ObjectOutputStream(socket.getOutputStream());
        is = new ObjectInputStream(socket.getInputStream());
        ping_response = new AtomicBoolean(false);
    }

    public void setGameHandler(GameHandler gameHandler) {
        this.gameHandler = gameHandler;
    }

    public int getClientId() {
        return clientId;
    }

    public void setClientId(int clientId) {
        this.clientId = clientId;
    }

    public void setInGame(boolean inGame) {
        this.inGame = inGame;
    }

    /**
     * This method opens an output and input stream and forwards the messages to the GameHandler or to the server
     */
    @Override
    public void run() {
        try {
            active.set(true);
            startPinger();
            while (active.get()) {
                Object clientMessage = is.readObject();
                //stopTimer();
                if (!(clientMessage instanceof InfoMessage && ((InfoMessage) clientMessage).getString().equalsIgnoreCase("PING"))) {
                    if (clientMessage instanceof InfoMessage && ((InfoMessage) clientMessage).getString().equalsIgnoreCase("QUIT"))
                        closeConnection(true);
                    else if (inGame)
                        gameHandler.manageMessage(this, (Message) clientMessage);
                    else if (!alreadySettings && clientMessage instanceof SettingsMessage) {
                        server.addClientConnectionToQueue(this, ((SettingsMessage) clientMessage).getPlayersNumber(), ((SettingsMessage) clientMessage).isExpertMode());
                        this.alreadySettings = true;
                    }
                } else {
                    ping_response.set(true);
                }
            }
        } catch (IOException | ClassNotFoundException e) {
            closeConnection(true);
        }
    }

    /**
     * Method to send a message throw the output stream
     */
    public synchronized void sendMessage(Message message) {
            /*if (message instanceof AskActionMessage)
                startTimer();*/
        if (active.get()) {
            try {
                os.writeObject(message);
                os.flush();
                os.reset();
            } catch (IOException e) {
                closeConnection(false);
            }
        }
    }

    /**
     * This method closes a connection when a client wants to disconnect at any moment during the game.
     * This method is called by the gameHandler to disconnects a client
     *
     * @param quit boolean that tells  if the client has sent a quit message.
     */
    public synchronized void closeConnection(boolean quit) {
        if (!closed.get()) {
            inGame = false;
            closed.set(true);
            sendMessage(new InfoMessage("CONNECTION_CLOSED", false));
            active.set(false);
            /*
            if (noPing) {
                //sendMessage(new InfoMessage("TIMER_ENDED",false));
                gameHandler.endGame(clientId);
            } //else stopTimer();*/
            if (quit && gameHandler != null)
                gameHandler.endGame(clientId);
            //per non avere SocketException lato client se cerca d'inviare un messaggio dopo che
            //è stata chiusa la connessione
            try {
                Thread.sleep((long) 10 * 1000);
            } catch (InterruptedException e) {
                //e.printStackTrace();
            }

            try {
                is.close();
                os.close();
                socket.close();
            } catch (IOException e) {
                //e.printStackTrace();
            }
        }

    }

    /**
     * This method start the Ping thread.
     * If a client doesn't send an answer before the period of time established , the connection is closed
     */
    private void startPinger() {
        Thread pinger = new Thread(() -> {
            while (active.get()) {
                try {
                    sendMessage(new InfoMessage("PING", false));
                    Thread.sleep((long) 2 * 1000);
                    if (ping_response.get()) {
                        ping_response.set(false);
                    } else closeConnection(true);
                } catch (InterruptedException e) {
                    break;
                }
            }
        });
        pinger.start();
    }

}
