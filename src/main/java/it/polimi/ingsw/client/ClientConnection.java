package it.polimi.ingsw.client;

import it.polimi.ingsw.constants.Constants;
import it.polimi.ingsw.server.ConnectionMessage.*;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketException;

/**
 * ConnectionSocket class handles the connection between the client and the server.
 *
 * @author
 */
public class ClientConnection implements Runnable {
    private final String serverAddress;
    private final int serverPort;

    private Socket socket;
    private boolean active;
    private ObjectOutputStream os;
    private ObjectInputStream is;


    private final View view;

    public ClientConnection(View view) throws IOException {
        this.serverAddress = Cli.getAddress();
        this.serverPort = Cli.getPort();
        active = true;
        this.view = view;
        socket = new Socket(serverAddress, serverPort);
        is = new ObjectInputStream(socket.getInputStream());
        os = new ObjectOutputStream(socket.getOutputStream());
    }

    public void send(Message message) {
        if (active) {
            try {
                os.reset();
                os.writeObject(message);
                os.flush();
            } catch (IOException e) {
                System.err.println("Error while sending the message");
            }
        }
    }

    @Override
    public void run() {
        try {
            while (active) {
                Object message = is.readObject();
                startMessageManager((Message) message);
            }
        } catch (IOException | ClassNotFoundException e) {
            closeConnection();
        }
    }

    private void manageMessage(Message message) {
        ((ServerMessage) message).forward(view);
    }

    public synchronized void closeConnection() {
        active = false;
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

    private void startMessageManager(Message message) {
        Thread t = new Thread(() -> {
            if (message instanceof InfoMessage && ((InfoMessage) message).getString().equalsIgnoreCase("PING"))
                send(new InfoMessage("PING"));
            else {
                manageMessage((Message)message);
                if (message instanceof InfoMessage && ((InfoMessage) message).getString().equalsIgnoreCase("CONNECTION_CLOSED")) {
                    closeConnection();
                    System.exit(0);
                }
            }
        });
        t.start();
    }
}
