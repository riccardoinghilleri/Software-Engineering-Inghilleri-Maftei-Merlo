package it.polimi.ingsw.client;

import it.polimi.ingsw.client.cli.Cli;
import it.polimi.ingsw.server.ConnectionMessage.*;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * ClientConnection class handles the connection between the client and the server.
 */
public class ClientConnection implements Runnable {

    private int clientId;
    private boolean lastPlayer;
    private final Socket socket;
    private final AtomicBoolean active;
    private final ObjectOutputStream os;
    private final ObjectInputStream is;
    private final View view;
    private Thread timer;

    /**
     * The constructor of the class. It creates a new ClientConnection instance.
     *
     * @param view of type View - View reference.
     * @throws IOException exception thrown if an error occurs
     */
    public ClientConnection(View view) throws IOException {
        String serverAddress = view.getAddress();
        int serverPort = view.getPort();
        this.active = new AtomicBoolean(true);
        this.lastPlayer = false;
        this.view = view;
        socket = new Socket(serverAddress, serverPort);
        is = new ObjectInputStream(socket.getInputStream());
        os = new ObjectOutputStream(socket.getOutputStream());
    }

    /**
     * Method used to send a message throw the OutStream.
     */
    public synchronized void send(Message message) {
        if (active.get()) {
            try {
                os.writeObject(message);
                os.flush();
                os.reset();
            } catch (IOException e) {
                System.err.println("Error while sending the message");
            }
        }
    }

    /**
     * The method run waits for a message until active is true, paying attention if the message is a ping message.
     */
    @Override
    public void run() {
        try {
            while (active.get()) {
                Object message = is.readObject();
                if (message instanceof ConnectionIdMessage) {
                    this.clientId = ((ConnectionIdMessage) message).getId();
                    if (((ConnectionIdMessage) message).isLastPlayer() != null)
                        this.lastPlayer = ((ConnectionIdMessage) message).isLastPlayer();
                } else
                    startMessageManager((Message) message);
            }
        } catch (IOException | ClassNotFoundException e) {
            closeConnection();
        }
    }

    public int getClientId() {
        return clientId;
    }

    public boolean isLastPlayer() {
        return this.lastPlayer;
    }

    /**
     * This method forwards the received message to the view.
     */
    private void manageMessage(Message message) {
        ((ServerMessage) message).forward(view);
    }

    public synchronized void closeConnection() {
        active.set(false);
        try {
            is.close();
            os.close();
            socket.close();
        } catch (IOException e) {
            System.out.println("THE CONNECTION HAS BEEN CLOSED");
            //e.printStackTrace();
        }
    }

    private void startMessageManager(Message message) {
        Thread t = new Thread(() -> {
            if (message instanceof InfoMessage && ((InfoMessage) message).getString().equalsIgnoreCase("PING")) {
                stopTimer();
                send(new InfoMessage("PING", false));
                startTimer();
            } else {
                manageMessage(message);
                if (message instanceof InfoMessage && ((InfoMessage) message).getString().equalsIgnoreCase("CONNECTION_CLOSED")) {
                    closeConnection();
                    if (view instanceof Cli) {
                        System.exit(0);
                    }
                }
            }
        });
        t.start();
    }

    /**
     * This method creates and starts the Timer thread.
     */

    private void startTimer() {
        timer = new Thread(() -> {
            try {
                Thread.sleep((long) 3 * 1000);
                System.out.println("Your connection is unstable. You has been disconnected.\nThanks for playing!");
                closeConnection();
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
