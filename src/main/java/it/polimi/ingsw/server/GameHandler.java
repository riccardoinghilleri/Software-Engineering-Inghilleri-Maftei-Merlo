package it.polimi.ingsw.server;

import it.polimi.ingsw.enums.Action;
import it.polimi.ingsw.controller.Controller;
import it.polimi.ingsw.server.ConnectionMessage.*;
import it.polimi.ingsw.enums.GameHandlerPhase;
import it.polimi.ingsw.server.model.BoardExpert;
import it.polimi.ingsw.server.model.GameModel;
import it.polimi.ingsw.server.model.Player;
import it.polimi.ingsw.enums.PlayerColor;
import it.polimi.ingsw.enums.Wizard;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;

import java.util.List;

public class GameHandler implements PropertyChangeListener {
    private final int gameId; //TODO non so se serve
    private final int playersNumber;
    private final boolean expertMode;
    private int currentClientConnection;
    private final GameModel gameModel;

    private int turnNumber;

    //Servono per le istanze multiple dei vari games
    private final List<PlayerColor> availableColors;
    private final List<Wizard> availableWizards;

    private GameHandlerPhase phase;
    private final List<VirtualView> clients;
    private final Server server;//TODO forse meglio listener
    private final Controller controller;

    public GameHandler(int gameId, boolean expertMode, List<VirtualView> clients, Server server) {
        this.gameId = gameId;
        this.playersNumber = clients.size();
        this.currentClientConnection = 0;
        this.expertMode = expertMode;
        this.clients = new ArrayList<>(clients);
        for (int i = 0; i < clients.size(); i++) { //setto i client ID
            clients.get(i).setClientId(i);
            clients.get(i).setGameHandler(this);
            clients.get(i).setInGame(true);
        }
        this.server = server;
        this.phase = GameHandlerPhase.SETUP_NICKNAME;
        this.gameModel = new GameModel(expertMode);
        this.controller = new Controller(this.gameModel, this);
        this.turnNumber = 0;
        this.availableColors = PlayerColor.getColors(playersNumber);
        this.availableWizards = Wizard.getWizards();
        sendAll(new InfoMessage(">The match is started..."));
        setupGame();
    }

    //Gestisce i messaggi ricevuti dalla client connection
    public void manageMessage(VirtualView client, Message message) {
        if (currentClientConnection != client.getClientId()) {
            client.sendMessage(new InfoMessage(">It is not your turn! Please wait."));
        } else {
            if (phase == GameHandlerPhase.SETUP_NICKNAME) {
                setupNickname((SetupMessage) message);
            } else if (phase == GameHandlerPhase.SETUP_COLOR) {
                gameModel.getPlayerById(client.getClientId()).setColor(((SetupMessage) message).getString());
                sendAllExcept(currentClientConnection, new InfoMessage(">"
                        + gameModel.getPlayerById(currentClientConnection).getNickname()
                        + " has chosen " + ((SetupMessage) message).getString()));
                availableColors.remove(PlayerColor.valueOf(((SetupMessage) message).getString().toUpperCase()));
                phase = GameHandlerPhase.SETUP_WIZARD;
                setupGame();
            } else if (phase == GameHandlerPhase.SETUP_WIZARD) {
                gameModel.getPlayerById(client.getClientId()).getDeck()
                        .setWizard(((SetupMessage) message).getString().toUpperCase());
                availableWizards.remove(Wizard.valueOf(((SetupMessage) message).getString().toUpperCase()));
                currentClientConnection = (currentClientConnection + 1) % playersNumber;
                if (currentClientConnection == 0) {
                    turnNumber = 1;
                    phase = GameHandlerPhase.PIANIFICATION;
                    gameModel.createBoard();
                    pianificationTurn();
                    //TODO forse si deve fare il display della board
                } else {
                    phase = GameHandlerPhase.SETUP_NICKNAME;
                    setupGame();
                }
            } else {
                if (phase == GameHandlerPhase.PIANIFICATION) {
                    if (!controller.setAssistantCard((ActionMessage) message)) {
                        clients.get(currentClientConnection)
                                .sendMessage(new InfoMessage(">You can not choose this assistant card. " +
                                        "Please choose another one."));
                        sendAllExcept(currentClientConnection, new InfoMessage(">" + gameModel
                                .getCurrentPlayer().getNickname() + " chosen an invalid Assistant Card..."));
                    } //else sendAll(new UpdateBoard(gameModel.getBoard()));
                    if (controller.getPhase() == Action.CHOOSE_ASSISTANT_CARD)
                        pianificationTurn();
                    else if (controller.getPhase() == Action.DEFAULT_MOVEMENTS
                            || controller.getPhase() == Action.CHOOSE_CHARACTER_CARD) {
                        phase = GameHandlerPhase.ACTION;
                        actionTurn();
                    }
                } else if (phase == GameHandlerPhase.ACTION) {
                    String error = controller.nextAction((ActionMessage) message);
                    if (error != null) {
                        clients.get(currentClientConnection).sendMessage(new InfoMessage(error));
                    } //else sendAll(new UpdateBoard(gameModel.getBoard()));
                    if (controller.getPhase() == Action.SETUP_CLOUD && turnNumber < 10) {
                        turnNumber++;
                        phase = GameHandlerPhase.PIANIFICATION;
                        pianificationTurn();
                    } else if (controller.getPhase() == Action.SETUP_CLOUD && turnNumber == 10) { //finiscono i 10 turni
                        gameModel.getBoard().findWinner();
                        endGame();
                    } else actionTurn();
                }
            }
        }
    }

    public void setupGame() {
        if (phase == GameHandlerPhase.SETUP_NICKNAME) {
            clients.get(currentClientConnection).sendMessage(new NicknameMessage(false));
            sendAllExcept(currentClientConnection, new InfoMessage(">The player #"
                    + (currentClientConnection + 1) + " is choosing his nickname..."));
        } else if (phase == GameHandlerPhase.SETUP_COLOR) {
            if (availableColors.size() > 1) {
                clients.get(currentClientConnection).sendMessage(new InfoMessage("Please choose your color :"));
                clients.get(currentClientConnection).sendMessage(new MultipleChoiceMessage(availableColors));
                sendAllExcept(currentClientConnection, new InfoMessage(">"
                        + gameModel.getPlayerById(currentClientConnection).getNickname() + " is choosing his color..."));
            } else {
                clients.get(currentClientConnection)
                        .sendMessage(new InfoMessage(">The Game has chosen the color for you.\n" +
                                ">Your color is " + availableColors.get(0)));
                sendAllExcept(currentClientConnection, new InfoMessage(">The game assigned to "
                        + gameModel.getPlayerById(currentClientConnection).getNickname() + " the last color:  "
                        + availableColors.get(0).toString()));
                gameModel.getPlayerById(currentClientConnection).setColor(availableColors.get(0).toString());
                phase = GameHandlerPhase.SETUP_WIZARD;
                setupGame();
            }
        } else if (phase == GameHandlerPhase.SETUP_WIZARD) {
            clients.get(currentClientConnection).sendMessage(new InfoMessage("Please choose your wizard:"));
            clients.get(currentClientConnection).sendMessage(new MultipleChoiceMessage(availableWizards));
            sendAllExcept(currentClientConnection, new InfoMessage(">" + gameModel
                    .getPlayerById(currentClientConnection).getNickname() + " is choosing his wizard..."));
        }
    }

    private void pianificationTurn() {
        if (controller.getPhase() == Action.SETUP_CLOUD) {
            controller.setClouds();
        }
        currentClientConnection = gameModel.getCurrentPlayer().getClientID();
        sendAll(new UpdateBoard(gameModel.getBoard()));
        clients.get(currentClientConnection)
                .sendMessage(new TurnMessage(true));
        clients.get(currentClientConnection)
                .sendMessage(new AskActionMessage(controller.getPhase(), gameModel
                        .getCurrentPlayer().getDeck().getAssistantCards()));
        sendAllExcept(currentClientConnection, new TurnMessage(false));
        sendAllExcept(currentClientConnection, new InfoMessage(">" + gameModel
                .getCurrentPlayer().getNickname() + " is choosing the AssistantCard..."));
    }
    private void actionTurn() {
        sendAll(new UpdateBoard(gameModel.getBoard()));
        currentClientConnection = gameModel.getCurrentPlayer().getClientID();
        AskActionMessage askActionMessage = null;
        switch (controller.getPhase()) {
            case CHOOSE_CHARACTER_CARD:
                askActionMessage = new AskActionMessage(controller.getPhase(),
                        ((BoardExpert) gameModel.getBoard()).getCharacterCards());
                break;
            case USE_CHARACTER_CARD:
                askActionMessage = new AskActionMessage(controller.getPhase(),
                        ((BoardExpert) gameModel.getBoard()).getCharacterCardbyName(controller.getCharacterCardName()),
                        gameModel.getBoard().getIslands(), gameModel.getBoard()
                        .getSchoolByOwnerId(gameModel.getCurrentPlayer().getClientID()));
                break;
            case DEFAULT_MOVEMENTS:
                askActionMessage = new AskActionMessage(controller.getPhase(), gameModel.getBoard()
                        .getSchoolByOwnerId(gameModel.getCurrentPlayer().getClientID()),
                        gameModel.getBoard().getIslands());
                sendAllExcept(currentClientConnection, new InfoMessage(">"
                        + gameModel.getCurrentPlayer().getNickname() + " is moving a student from the Entrance..."));
                break;
            case MOVE_MOTHER_NATURE:
                if (controller.getCharacterCardName()==null || !controller.getCharacterCardName().equalsIgnoreCase("POSTMAN"))
                    askActionMessage = new AskActionMessage(controller.getPhase(), gameModel
                            .getPlayerById(currentClientConnection)
                            .getChosenAssistantCard().getMotherNatureSteps());
                else if(controller.getCharacterCardName().equalsIgnoreCase("POSTMAN"))
                    askActionMessage = new AskActionMessage(controller.getPhase(), gameModel
                            .getPlayerById(currentClientConnection)
                            .getChosenAssistantCard().getMotherNatureSteps() + 2 );
                sendAllExcept(currentClientConnection, new InfoMessage(">"
                        + gameModel.getCurrentPlayer().getNickname() + " is choosing where to move mother nature..."));
                break;
            case CHOOSE_CLOUD:
                askActionMessage = new AskActionMessage(controller.getPhase(), gameModel
                        .getBoard().getClouds());
                sendAllExcept(currentClientConnection, new InfoMessage(">"
                        + gameModel.getCurrentPlayer().getNickname() + " is choosing the cloud..."));
                break;
        }
        clients.get(currentClientConnection).sendMessage(askActionMessage);
    }

    private void setupNickname(SetupMessage message) {
        for (Player p : gameModel.getPlayers()) {
            if (p.getNickname().equals(message.getString())) {
                clients.get(currentClientConnection)
                        .sendMessage(new NicknameMessage(true));
                return;
            }
        }
        sendAllExcept(currentClientConnection, new InfoMessage(">The player #"
                + (currentClientConnection + 1) + " has chosen his nickname: " + message.getString()));
        gameModel.createPlayer(message.getString(), currentClientConnection);
        phase = GameHandlerPhase.SETUP_COLOR;
        setupGame();
    }

    public void endGame(int disconnected) {

        sendAllExcept(disconnected,new InfoMessage(">Player: " + gameModel.getPlayerById(disconnected).getNickname()
                + " has disconnected, the match will now end" + "\nThanks for playing!"));
        clients.remove(clients.get(disconnected));
        for (VirtualView client : clients) {
            client.closeConnection(false,false);
        }
        server.removeGameHandler(this);
        //TODO implementare il reset del game se vogliono rigiocare
    }

    public void endGame() {
        sendAll(new InfoMessage(">The winner is " + gameModel.getWinner().getNickname()
                + "!" + "\nThanks for playing!"));
        for (VirtualView client : clients) {
            client.closeConnection(false,false);
        }
        server.removeGameHandler(this);
    }
    /*
    public void resetGame() { //Solo se vogliono rigiocare tutti, altrimenti chi vuole rigiocare dovrebbe andare nelle code del server
        this.currentClientConnection = 0;
        this.phase = GameHandlerPhase.SETUP_NICKNAME;
        this.gameModel = new GameModel(expertMode);
        this.controller = new Controller(this.gameModel,this);
        this.turnNumber = 1;
        this.phase = GameHandlerPhase.PIANIFICATION;
        this.gameModel.createBoard();
        pianificationTurn();
    }*/

    public void sendAll(Message message) {
        for (VirtualView client : clients) {
            client.sendMessage(message);
        }
    }

    public void sendAllExcept(int clientId, Message message) { //TODO non so se serve
        for (VirtualView client : clients) {
            if (client.getClientId() != clientId)
                client.sendMessage(message);
        }
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        switch (evt.getPropertyName()) {
            case "end_game":
                if (gameModel.getWinner() == null) {
                    //TODO NON CI SONO VINCITORI.Pareggio
                } else endGame();
                break;
            case "set_assistantCard":
                sendAllExcept(gameModel.getCurrentPlayer().getClientID(), new InfoMessage(">" + gameModel.getCurrentPlayer().getNickname() + " has chosen che AssistantCard with priority #" + ((ActionMessage) evt.getNewValue()).getData()));
                break;
            case "change_turn":
                clients.get(currentClientConnection)
                        .sendMessage(new TurnMessage(true));
                sendAllExcept(currentClientConnection, new TurnMessage(false));
                break;
        }
    }
}
