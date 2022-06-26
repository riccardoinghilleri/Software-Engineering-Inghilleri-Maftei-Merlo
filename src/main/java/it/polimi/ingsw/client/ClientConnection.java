package it.polimi.ingsw.client;

import it.polimi.ingsw.client.cli.Cli;
import it.polimi.ingsw.server.ConnectionMessage.*;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 * ClientConnection class handles the connection between the client and the server.
 *
 * @author
 */
public class ClientConnection implements Runnable {
    private final String serverAddress;
    private final int serverPort;

    private int clientId;
    private boolean lastPlayer;

    private final Socket socket;
    private boolean active;
    private final ObjectOutputStream os;
    private final ObjectInputStream is;
    private final View view;

    /**
     * The constructor of the class. It creates a new ClientConnection instance.
     * @param view of type View - View reference.
     * @throws IOException exception thrown if an error occurs
     */
    public ClientConnection(View view) throws IOException {
        this.serverAddress = view.getAddress();
        this.serverPort = view.getPort();
        this.active = true;
        this.lastPlayer = false;
        this.view = view;
        socket = new Socket(serverAddress, serverPort);
        is = new ObjectInputStream(socket.getInputStream());
        os = new ObjectOutputStream(socket.getOutputStream());
    }

    /**
     * Method used to send a message throw the OutStream.
     */
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

    /**
     * The method run waits for a message until active is true, paying attention if the message is a ping message.
     */
    @Override
    public void run() {
        try {
            while (active) {
                Object message = is.readObject();
                if (message instanceof ConnectionIdMessage) {
                    this.clientId = ((ConnectionIdMessage) message).getId();
                    if(((ConnectionIdMessage) message).isLastPlayer()!=null)
                        this.lastPlayer =((ConnectionIdMessage) message).isLastPlayer();
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

    public boolean isLastPlayer(){
        return this.lastPlayer;
    }


    /**
     * This method forwards the received message to the view.
     */
    private void manageMessage(Message message) {
        ((ServerMessage) message).forward(view);
    }

    public synchronized void closeConnection() {
        active = false;
        try {
            is.close();
        } catch (IOException e) {
            System.out.println("THE CONNECTION HAS BEEN CLOSED");
            e.printStackTrace();
        }
        try {
            os.close();
        } catch (IOException e) {
            System.out.println("THE CONNECTION HAS BEEN CLOSED");
            e.printStackTrace();
        }
        try {
            socket.close();
        } catch (IOException e) {
            System.out.println("THE CONNECTION HAS BEEN CLOSED");
            e.printStackTrace();
        }
    }

    private void startMessageManager(Message message) {
        Thread t = new Thread(() -> {
            if (message instanceof InfoMessage && ((InfoMessage) message).getString().equalsIgnoreCase("PING"))
                send(new InfoMessage("PING"));
            else {
                manageMessage(message);
                if (message instanceof InfoMessage && ((InfoMessage) message).getString().equalsIgnoreCase("CONNECTION_CLOSED")) {
                    closeConnection();
                    if (view instanceof Cli)
                        System.exit(0);
                }
            }
        });
        t.start();
    }
}
