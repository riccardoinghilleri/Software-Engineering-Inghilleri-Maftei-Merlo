package it.polimi.ingsw.server;

import it.polimi.ingsw.server.ConnectionMessage.InfoMessage;
import it.polimi.ingsw.server.ConnectionMessage.Message;
import it.polimi.ingsw.server.ConnectionMessage.SettingsMessage;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.concurrent.atomic.AtomicBoolean;

public class ClientConnection implements Runnable {
    private int clientId;
    private boolean inGame;

    private AtomicBoolean active;
    private AtomicBoolean closed;

    private Socket socket;
    private Server server;
    private GameHandler gameHandler;
    private ObjectInputStream is;
    private ObjectOutputStream os;

    private Thread pinger;
    private Thread timer;

    public ClientConnection(Socket socket, Server server, GameHandler gameHandler) {
        this.socket = socket;
        this.clientId = -1;
        this.inGame = false;
        this.server = server;
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

            startPinger();

            sendMessage(new InfoMessage("Please choose the game's settings: "));

            while (active.get()) {

                Object clientMessage = is.readObject();

                if (!(clientMessage instanceof InfoMessage && ((InfoMessage)clientMessage).getString().equalsIgnoreCase("PING"))){
                    if (inGame)
                        gameHandler.manageMessage(this,(Message)clientMessage);
                    else /*checkSettings((SettingsMessage) clientMessage);*/
                        server.addClientConnectionToQueue(this,((SettingsMessage)clientMessage).getPlayersNumber(), ((SettingsMessage)clientMessage).isExpertMode());
                }
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            closeConnection(false);
        }
    }

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

    private void stopTimer() {
        if (timer != null && timer.isAlive()) {
            timer.interrupt();
            timer = null;
        }
    }

}
