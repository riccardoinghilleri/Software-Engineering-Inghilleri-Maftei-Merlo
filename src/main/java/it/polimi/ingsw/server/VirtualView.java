package it.polimi.ingsw.server;

import it.polimi.ingsw.server.ConnectionMessage.*;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketException;
import java.util.concurrent.atomic.AtomicBoolean;

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
                    closeConnection(false,true);
                    else if (inGame)
                        gameHandler.manageMessage(this, (Message) clientMessage);
                    else if (!alreadySettings) {/*checkSettings((SettingsMessage) clientMessage);*/
                        server.addClientConnectionToQueue(this, ((SettingsMessage) clientMessage).getPlayersNumber(), ((SettingsMessage) clientMessage).isExpertMode());
                        this.alreadySettings = true;
                    }
                }
            }
        } catch (IOException | ClassNotFoundException e) {
            closeConnection(false,false);
        }
    }

    public void sendMessage(Message message) {
        //TODO potrebbe servire un lock per l'output stream
        //TODO inserire period e boolean timer
        //(timer)
        if(message instanceof AskActionMessage)
            startTimer(15000);
        try {
            os.writeObject(message);
            os.flush();
            os.reset();
        } catch (IOException e) {
            closeConnection(false,false);
        }
    }
    public synchronized void closeConnection(boolean timerEnded, boolean quit) {
        if (!closed.get()) {

            closed.set(true);
            active.set(false);

            if (timerEnded) {
                sendMessage(new InfoMessage("TIMER_ENDED"));
                gameHandler.endGame(clientId);
            }else stopTimer();

            if(quit)
                gameHandler.endGame(clientId);

            sendMessage(new InfoMessage("CONNECTION_CLOSED"));

            //per non avere SocketException lato client se cerca di inviare un messaggio dopo che
            //è stata chiusa la connessione
            try {
                Thread.sleep(30000);
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
        pinger.start();
    }

    private void startTimer(int period) {
        timer = new Thread(() -> {
            try {
                Thread.sleep(period);
                closeConnection(true,false);
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
