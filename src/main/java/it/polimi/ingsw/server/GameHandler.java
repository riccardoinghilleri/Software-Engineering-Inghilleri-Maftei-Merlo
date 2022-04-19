package it.polimi.ingsw.server;

import it.polimi.ingsw.controller.Controller;
import it.polimi.ingsw.server.ConnectionMessage.*;
import it.polimi.ingsw.server.model.GameModel;

import java.util.List;

public class GameHandler {
    private int gameId;
    private int playersNumber;
    private boolean expertMode;
    private int currentClientConnection;
    private GameModel gameModel;
    private List<String> stillAvailableColors;
    private List<String> stillAvailableWizards;
    private int turnNumber;
    private GameHandlerPhase phase;

    private List<ClientConnection> clients;
    private Server server;
    private Controller controller;

    public GameHandler(int gameId,boolean expertMode,List<ClientConnection> clients,Server server){

    }
    public void manageGameMessage(ClientConnection client, ClientMessage message){

    }
    public void setupNickname(NicknameMessage Client){

    }
    public void setupColor(SetupMessage message){

    }
    public void setupWizard(SetupMessage message){

    }
    public void endGame( int winner){

    }
    public void resetGame(){

    }
    public void sendAllExcept(int clientId, ServerMessage message){

    }

    public void send(int clientId, ServerMessage message){

    }
}
