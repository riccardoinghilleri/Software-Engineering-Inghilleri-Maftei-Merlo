package it.polimi.ingsw.server;

import it.polimi.ingsw.server.ConnectionMessage.ActionMessage;
import it.polimi.ingsw.server.ConnectionMessage.ClientMessage;
import it.polimi.ingsw.server.ConnectionMessage.ServerMessage;

import java.io.ObjectInputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.concurrent.atomic.AtomicBoolean;

public class ClientConnection implements Runnable{
    private int clientId;
    private boolean inGame;

    private AtomicBoolean active;
    private AtomicBoolean closed;

    private Socket socket;
    private Server server;
    private ObjectInputStream is;
    private OutputStream os;

    private Thread pinger;
    private Thread timer;

    public ClientConnection(Socket socket,Server server){
        this.socket=socket;
        this.clientId=-1;
        this.inGame= false;
        this.server=server;
    }

    @Override
    public void run(){

    }

    public void sendMessage(ServerMessage message){

    }
    public void closeConnection(){

    }

    private void checkSettings(ClientMessage actionMessage){

    }

    private void startPinger(){

    }
    private void startTimer(){

    }
    private void stopTimer(){

    }

}
