package it.polimi.ingsw.client;

import it.polimi.ingsw.server.ConnectionMessage.*;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.logging.SocketHandler;

/**
 * ConnectionSocket class handles the connection between the client and the server.
 *
 * @author
 */
public class ClientConnection implements Runnable{
    private final String serverAddress;
    private final int serverPort;

    private Socket socket;
    private boolean active;

    private ObjectOutputStream os;
    private ObjectInputStream is;

    private Cli cli;
    public ClientConnection(Cli cli) {
        this.serverAddress = Cli.getAddress();
        this.serverPort = Cli.getPort();
        active=true;
        this.cli=cli;
        try {
            socket = new Socket(serverAddress, serverPort);
        } catch (IOException e) {
            System.out.println("Error while opening the socket");
        }
    }


    public void send(Message message) {
        try {
            os.reset();
            os.writeObject(message);
            os.flush();
        } catch (IOException e) {
            System.err.println("Error during send process.");
            System.err.println(e.getMessage());
        }
    }

    @Override
    public void run() {
        try {
                is = new ObjectInputStream(socket.getInputStream());
                os = new ObjectOutputStream(socket.getOutputStream());
            while (active) {
                Object message = is.readObject();
                if (message instanceof InfoMessage && ((InfoMessage)message).getString().equalsIgnoreCase("PING")){
                    send(new InfoMessage("PING"));
                }
                else manageMessage((Message)message);
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
    public void manageMessage(Message message) {
        if (message instanceof NicknameMessage) {
            cli.setupNickname((NicknameMessage)message);
        } else if (message instanceof MultipleChoiceMessage) {
            cli.setupMultipleChoice((MultipleChoiceMessage) message);
        } else if (message instanceof InfoMessage) {
            cli.displayInfo((InfoMessage) message);
        } else if (message instanceof AskActionMessage) {
            cli.askAction((AskActionMessage) message);
        }
    }

    public boolean isActive() {
        return active;
    }
}