package it.polimi.ingsw.server;

import it.polimi.ingsw.server.ConnectionMessage.InfoMessage;
import it.polimi.ingsw.server.ConnectionMessage.Message;
import it.polimi.ingsw.server.ConnectionMessage.SettingsMessage;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketException;
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

    private AtomicBoolean active;
    private AtomicBoolean closed;

    private final Socket socket;
    private final Server server;
    private GameHandler gameHandler;
    private ObjectInputStream is;
    private ObjectOutputStream os;

    private Thread pinger;
    private Thread timer;
    /**
     * The constructor of the class.
     * It needs a socket and the server.
     */
    public VirtualView(Socket socket, Server server) {
        this.socket = socket;
        this.clientId = -1;
        this.inGame = false;
        this.server = server;
        this.alreadySettings = false;
        active = new AtomicBoolean(false);
        closed = new AtomicBoolean(false);
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
     * This method opens an output and input stream. and forwards the messages.
     */
    @Override
    public void run() {
        try {
            os = new ObjectOutputStream(socket.getOutputStream());
            is = new ObjectInputStream(socket.getInputStream());

            active.set(true);

            //startPinger();

            while (active.get()) {
                Object clientMessage = is.readObject();
                if (!(clientMessage instanceof InfoMessage && ((InfoMessage) clientMessage).getString().equalsIgnoreCase("PING"))) {
                    if (inGame)
                        gameHandler.manageMessage(this, (Message) clientMessage);
                    else if (!alreadySettings) {/*checkSettings((SettingsMessage) clientMessage);*/
                        server.addClientConnectionToQueue(this, ((SettingsMessage) clientMessage).getPlayersNumber(), ((SettingsMessage) clientMessage).isExpertMode());
                        this.alreadySettings = true;
                    }
                }
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            forcedClose();
        }
    }
    /**
     * Method to send a message throw the output stream
     */
    public void sendMessage(Message message) {
        //TODO potrebbe servire un lock per l'output stream
        //TODO inserire period e boolean timer
        /*if (timer)
            startTimer(period);*/
        try {
            os.writeObject(message);
            os.flush();
            os.reset();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    /**
     * This method closes forcefully the input stream, the output and the socket.
     * It also interrupts the timer thread     */

    private void forcedClose(){
        active.set(false);
        stopTimer();
        try {
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            os.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    /**
     * This method closes a connection after waiting for several seconds and not receiving an answer.
     * This method is called by the gameHandler to disconnects a client
     * @param timerEnded boolean that tells if the time is up.
     */
    public synchronized void closeConnection(boolean timerEnded) {
        if (closed.compareAndSet(false, true)) {

            active.set(false);
            stopTimer();

            if (timerEnded) {
                sendMessage(new InfoMessage("TIMER_ENDED"));
                gameHandler.endGame(clientId);
            }

            sendMessage(new InfoMessage("CONNECTION_CLOSED"));

            //TODO serve aspettare 10 secondi prima di chiudere tutto?
            try {
                Thread.sleep(10000);
            } catch (InterruptedException ignored) {
            }

            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                os.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    /*private void checkSettings(SettingsMessage message) {
        stopTimer();
        boolean error = false;
        if (message.getPlayersNumber() != 2 && message.getPlayersNumber() != 1 && !(message.getGameMode() != 2 || message.getGameMode() != 2)) {
            sendMessage(new ServerMessage("Incorret Number of player, please try again"), true, 10000);
            error = true;
        } else if (message.getGameMode() != 1 || message.getGameMode() != 2 && (message.getPlayersNumber() != 2 && message.getPlayersNumber() != 3)) {
            sendMessage(new ServerMessage("Incorret Game Mode, please try again"), true, 10000);
            error = true;
        } else {
            sendMessage(new ServerMessage("Incorret settings, please try again"), true, 10000);
            error = true;
        }
        if (!error) {
            int palyerNumber = message.getPlayersNumber() == 1 ? 2 : 3;
            boolean gameMode = message.getGameMode() == 1 ? true : false;
            server.addClientConnectionToQueue(this, message.getPlayersNumber(), gameMode);
        }
    }*/
    /**
     * This message tests the connection with the ping message
     */
    private void startPinger() {
        pinger = new Thread(() -> {
            while (active.get()) {
                try {
                    Thread.sleep(2500);
                    sendMessage(new InfoMessage("PING"));
                } catch (InterruptedException e) {
                    break;
                }
            }
        });
    }
    /**
     * This method creates and starts the Timer thread.
     */
    private void startTimer(int period) {
        timer = new Thread(() -> {
            try {
                Thread.sleep(period);
                closeConnection(true);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
    }

    /**
     * This method  interrupts the timer thread , after checking that the latter is alive.
     * It is used when a client disconnects
     */
    private void stopTimer() {
        if (timer != null && timer.isAlive()) {
            timer.interrupt();
            timer = null;
        }
    }

}
