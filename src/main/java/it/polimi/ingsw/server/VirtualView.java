package it.polimi.ingsw.server;

import it.polimi.ingsw.server.ConnectionMessage.*;

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

    private final AtomicBoolean active, closed;
    //private final AtomicBoolean ping_response;

    private final Socket socket;
    private final Server server;
    private GameHandler gameHandler;
    private final ObjectInputStream is;
    private final ObjectOutputStream os;

    //private Thread pinger;
    private Thread timer;
    /**
     * The constructor of the class.
     * It needs a socket and the server.
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
        //ping_response = new AtomicBoolean(false);
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
            active.set(true);

            //startPinger();

            while (active.get()) {
                Object clientMessage = is.readObject();
                stopTimer();
                if (!(clientMessage instanceof InfoMessage && ((InfoMessage) clientMessage).getString().equalsIgnoreCase("PING"))) {
                    if(clientMessage instanceof  InfoMessage && ((InfoMessage) clientMessage).getString().equalsIgnoreCase("QUIT"))
                        closeConnection(false,true);
                    else if (inGame)
                        gameHandler.manageMessage(this, (Message) clientMessage);
                    else if (!alreadySettings) {/*checkSettings((SettingsMessage) clientMessage);*/
                        server.addClientConnectionToQueue(this, ((SettingsMessage) clientMessage).getPlayersNumber(), ((SettingsMessage) clientMessage).isExpertMode());
                        this.alreadySettings = true;
                    }
                } /*else {
                    ping_response.set(true);
                }*/
            }
        } catch (IOException | ClassNotFoundException e) {
            closeConnection(false, true);
        }
    }
    /**
     * Method to send a message throw the output stream
     */
    public synchronized void sendMessage(Message message) {
            if (message instanceof AskActionMessage)
                startTimer();
            try {
                os.writeObject(message);
                os.flush();
                os.reset();
            } catch (IOException e) {
                closeConnection(false, false);
            }
    }

    /**
     * This method closes a connection after waiting for several seconds and not receiving an answer.
     * This method is called by the gameHandler to disconnects a client
     * @param timerEnded boolean that tells if the time is up.
     */
    public synchronized void closeConnection(boolean timerEnded, boolean quit) {
        if (!closed.get()) {
            inGame = false;
            closed.set(true);
            active.set(false);

            if (timerEnded) {
                sendMessage(new InfoMessage("TIMER_ENDED",false));
                gameHandler.endGame(clientId);
            } else stopTimer();

            if (quit && gameHandler!=null)
                gameHandler.endGame(clientId);

            sendMessage(new InfoMessage("CONNECTION_CLOSED",false));

            //per non avere SocketException lato client se cerca di inviare un messaggio dopo che
            //è stata chiusa la connessione
            try {
                Thread.sleep(10000);
            } catch (InterruptedException ignored) {
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

    /*
        private void startPinger() {
            pinger = new Thread(() -> {
                while (active.get()) {
                    try {
                        sendMessage(new InfoMessage("PING"));
                        Thread.sleep(5000);
                        if (ping_response.get()) {
                            System.out.println("PING_OK" + clientId);
                            ping_response.set(false);
                        } else closeConnection(false, true);
                    } catch (InterruptedException e) {
                        break;
                    }
                }
            });
            pinger.start();
        }
    */
    /**
     * This method creates and starts the Timer thread.
     */
    private void startTimer() {
        timer = new Thread(() -> {
            try {
                Thread.sleep((long)45 * 1000);
                //System.out.println("stop timer" + clientId);
                closeConnection(true, false);
            } catch (InterruptedException e) {
                //e.printStackTrace();
            }
        });
        timer.start();
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
