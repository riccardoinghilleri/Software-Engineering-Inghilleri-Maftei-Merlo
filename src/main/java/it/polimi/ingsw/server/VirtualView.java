package it.polimi.ingsw.server;

import it.polimi.ingsw.server.ConnectionMessage.*;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * This class communicates with the ClientConnection through the socket created by the server.
 * It forwards messages based on the value of an inGame attribute either to the server or gameHandler.
 * if inGame == false, the messages are forwarded to the server, otherwise to the gameHandler.
 */
public class VirtualView implements Runnable {
    private int clientId;
    private boolean inGame;

    private boolean alreadySettings;
    private int playersNumber;
    private boolean isExpertMode;

    private final AtomicBoolean active, closed;
    private final AtomicBoolean ping_response;

    private final Socket socket;
    private final Server server;
    private GameHandler gameHandler;
    private final ObjectInputStream is;
    private final ObjectOutputStream os;

    /**
     * Constructor of the class.
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

    /**
     * For testing
     */
    public  VirtualView(){
        this.socket = null;
        this.clientId = -1;
        this.inGame = false;
        this.server = null;
        this.alreadySettings = false;
        active = new AtomicBoolean(false);
        closed = new AtomicBoolean(false);
        os = null;
        is = null;
        ping_response = new AtomicBoolean(false);
    }

    public void setGameHandler(GameHandler gameHandler) {
        this.gameHandler = gameHandler;
    }

    /**
     * @return the clientId associated with the client.
     */
    public int getClientId() {
        return clientId;
    }

    /**
     * The method set the clientId.
     * @param clientId id that has to be associated to the client.
     */
    public void setClientId(int clientId) {
        this.clientId = clientId;
    }


    /**
     * This method set the attributed that specifies if the virtualView
     * has been already added to a game.
     */
    public void setInGame(boolean inGame) {
        this.inGame = inGame;
    }

    /**
     * This method opens an output and input stream and
     * forwards the messages to the GameHandler or to the server.
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
                        this.playersNumber=((SettingsMessage) clientMessage).getPlayersNumber();
                        this.isExpertMode=((SettingsMessage) clientMessage).isExpertMode();
                        server.addClientConnectionToQueue(this,playersNumber ,isExpertMode);
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
     * Method that sends a message through the output stream.
     */
    public synchronized void sendMessage(Message message) {
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
     * This method closes a connection when the current client wants to disconnect from
     * the game or when another client decided to  leave  the match, and when the
     * game end.
     *
     * @param quit boolean that tells if the current client decided to quit game.
     */
    public synchronized void closeConnection(boolean quit) {
        if (!closed.get()) {
            inGame = false;
            closed.set(true);
            sendMessage(new InfoMessage("CONNECTION_CLOSED", false));
            active.set(false);

            if (quit && gameHandler != null)
                gameHandler.endGame(clientId);
            else if(!inGame)
                server.removeClientConnection(this,playersNumber,isExpertMode);
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
     * If a client does not send a response before the predetermined time period
     * expires, the connection is closed.
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
