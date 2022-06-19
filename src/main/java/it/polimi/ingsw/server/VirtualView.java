package it.polimi.ingsw.server;

import it.polimi.ingsw.server.ConnectionMessage.*;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

public class VirtualView implements Runnable {
    private int clientId;
    private boolean inGame;

    private boolean alreadySettings;

    private final AtomicBoolean active, closed, ping_response;


    private final Socket socket;
    private final Server server;
    private GameHandler gameHandler;
    private ObjectInputStream is;
    private ObjectOutputStream os;

    private Thread pinger;
    private Thread timer;

    public VirtualView(Socket socket, Server server) {
        this.socket = socket;
        this.clientId = -1;
        this.inGame = false;
        this.server = server;
        this.alreadySettings = false;
        active = new AtomicBoolean(false);
        closed = new AtomicBoolean(false);
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

    @Override
    public void run() {
        try {
            os = new ObjectOutputStream(socket.getOutputStream());
            is = new ObjectInputStream(socket.getInputStream());

            active.set(true);

            //startPinger();

            while (active.get()) {
                Object clientMessage = is.readObject();
                stopTimer();
                if (!(clientMessage instanceof InfoMessage && ((InfoMessage) clientMessage).getString().equalsIgnoreCase("PING"))) {
                    if (clientMessage instanceof InfoMessage && ((InfoMessage) clientMessage).getString().equalsIgnoreCase("QUIT"))
                        closeConnection(false, true);
                    else if (inGame)
                        gameHandler.manageMessage(this, (Message) clientMessage);
                    else if (!alreadySettings) {/*checkSettings((SettingsMessage) clientMessage);*/
                        server.addClientConnectionToQueue(this, ((SettingsMessage) clientMessage).getPlayersNumber(), ((SettingsMessage) clientMessage).isExpertMode());
                        this.alreadySettings = true;
                    }
                } else {
                    ping_response.set(true);
                }
            }
        } catch (IOException | ClassNotFoundException e) {
            closeConnection(false, true);
        }
    }

    public void sendMessage(Message message) {
        //TODO potrebbe servire un lock per l'output stream
        //TODO inserire period e boolean timer
        //(timer)
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

    public synchronized void closeConnection(boolean timerEnded, boolean quit) {
        if (!closed.get()) {
            inGame = false;
            closed.set(true);
            active.set(false);

            if (timerEnded) {

                sendMessage(new InfoMessage("TIMER_ENDED"));
                gameHandler.endGame(clientId);
            } else stopTimer();

            if (quit)
                gameHandler.endGame(clientId);

            sendMessage(new InfoMessage("CONNECTION_CLOSED"));

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
    private void startTimer() {
        timer = new Thread(() -> {
            try {
                Thread.sleep(10 * 1000);
                //System.out.println("stop timer" + clientId);
                closeConnection(true, false);
            } catch (InterruptedException e) {
                //e.printStackTrace();
            }
        });
        timer.start();
    }

    private void stopTimer() {
        if (timer != null && timer.isAlive()) {
            timer.interrupt();
            timer = null;
        }
    }
}
/*
final class ScheduledExecutorServiceDemo {
    private static final ScheduledExecutorService exec =
            Executors.newScheduledThreadPool(2);

    public static void main(String[] args) {
        // Schedule first task
        exec.scheduleAtFixedRate(() -> {
            // do stuff
        }, 0, 5000, TimeUnit.MILLISECONDS);

        // Schedule second task
        exec.scheduleAtFixedRate(() -> {
            // do stuff
        }, 0, 1, TimeUnit.MINUTES);
    }
}*/
