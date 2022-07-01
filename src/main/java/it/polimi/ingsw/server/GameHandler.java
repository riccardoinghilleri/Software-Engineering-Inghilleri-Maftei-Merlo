package it.polimi.ingsw.server;

import it.polimi.ingsw.controller.Controller;
import it.polimi.ingsw.enums.Action;
import it.polimi.ingsw.server.ConnectionMessage.*;
import it.polimi.ingsw.enums.GameHandlerPhase;
import it.polimi.ingsw.server.model.BoardExpert;
import it.polimi.ingsw.server.model.GameModel;
import it.polimi.ingsw.server.model.Player;
import it.polimi.ingsw.enums.PlayerColor;
import it.polimi.ingsw.enums.Wizard;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.*;


/**
 * This class that represents the actual Game.
 * It receives from the server, after the latter accepts the connections and put each connection in the correct queue,
 * a list of the players and the game mode.
 * * Tha gameHandler can be found in different phases( SETUP_NICKNAME,SETUP_COLOR,SETUP_WIZARD,PIANIFICATION,ACTION)
 * Since multiple games are allowed, each different game has its gameHandler.
 */
public class GameHandler implements PropertyChangeListener {
    //private final int gameId;
    private final int playersNumber;
    private int currentClientConnection;
    private final GameModel gameModel;

    private int turnNumber;

    //Servono per le istanze multiple dei vari games
    private final List<PlayerColor> availableColors;
    private final List<Wizard> availableWizards;

    private GameHandlerPhase phase;
    private final List<VirtualView> clients;
    private final Server server;
    private final Controller controller;
    private String error;

    /**
     * The constructor of the class.
     * It needs the expertMode,list of players and server
     * @param expertMode specifies if the game is in expert Mode
     * @param clients list of the virtualViews of the clients
     * @param server who handle the active games
     **/
    public GameHandler(boolean expertMode, List<VirtualView> clients, Server server) {
        //this.gameId = gameId;
        this.error = null;
        this.playersNumber = clients.size();
        this.currentClientConnection = 0;
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
        sendAll(new InfoMessage(">The match is started...", false));
        setupGame();
    }

    /**
     * @return the gameModel
     */
    public GameModel getGameModel() {
        return gameModel;
    }

    /**
     * This method manages the messages from the client connection according to
     * his phase.
     * If a player tries to make a move while it is not his turn, he will receive
     * a message and be told to wait.
     */
    public void manageMessage(VirtualView client, Message message) {
        if (currentClientConnection != client.getClientId()) {
            client.sendMessage(new InfoMessage(">It is not your turn! Please wait.", false));
        } else {
            if (phase == GameHandlerPhase.SETUP_NICKNAME) {
                setupNickname((SetupMessage) message);
            } else if (phase == GameHandlerPhase.SETUP_COLOR) {
                gameModel.getPlayerById(client.getClientId()).setColor(((SetupMessage) message).getString());
                sendAllExcept(currentClientConnection, new InfoMessage(">"
                        + gameModel.getPlayerById(currentClientConnection).getNickname()
                        + " has chosen " + ((SetupMessage) message).getString(), false));
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
                    phase = GameHandlerPhase.PLANNING;
                    gameModel.createBoard();
                    for (VirtualView view : clients) {
                        view.sendMessage(new ConnectionIdMessage(clients.indexOf(view)));
                    }
                    planningTurn(null);
                } else {
                    phase = GameHandlerPhase.SETUP_NICKNAME;
                    setupGame();
                }
            } else {
                if (phase == GameHandlerPhase.PLANNING) {
                    error = controller.setAssistantCard((ActionMessage) message);
                    if (controller.getPhase() == Action.CHOOSE_ASSISTANT_CARD) {
                        planningTurn(error);
                    } else if (controller.getPhase() == Action.DEFAULT_MOVEMENTS
                            || controller.getPhase() == Action.CHOOSE_CHARACTER_CARD) {
                        phase = GameHandlerPhase.ACTION;
                        sendAll(new UpdateBoard(gameModel.getBoard()));
                        actionTurn();
                    }
                } else if (phase == GameHandlerPhase.ACTION) {
                    error = controller.nextAction((ActionMessage) message);
                    if (error != null) {
                        clients.get(currentClientConnection).sendMessage(new InfoMessage(error, false));
                    } else if (controller.getPhase() != Action.SETUP_CLOUD) {
                        sendAll(new UpdateBoard(gameModel.getBoard()));
                    }
                    if (controller.getPhase() == Action.SETUP_CLOUD && turnNumber < 10) {
                        turnNumber++;
                        phase = GameHandlerPhase.PLANNING;
                        planningTurn(null);
                    } else if (controller.getPhase() == Action.SETUP_CLOUD && turnNumber == 10) { //finiscono i 10 turni
                        gameModel.getBoard().findWinner();
                        endGame();
                    } else actionTurn();
                }
            }
        }
    }

    /**
     * This method manages the setting of the game parameters (nickname, color
     * and magician) for each player, according to their order connection to the server.
     */
    public void setupGame() {
        if (phase == GameHandlerPhase.SETUP_NICKNAME) {
            clients.get(currentClientConnection).sendMessage(new NicknameMessage(false));
            sendAllExcept(currentClientConnection, new InfoMessage(">The player #"
                    + (currentClientConnection + 1) + " is choosing his nickname...", false));
        } else if (phase == GameHandlerPhase.SETUP_COLOR) {
            clients.get(currentClientConnection).sendMessage(new MultipleChoiceMessage(availableColors, true));
            if (availableColors.stream().distinct().count() > 1) {
                sendAllExcept(currentClientConnection, new InfoMessage(">"
                        + gameModel.getPlayerById(currentClientConnection).getNickname() + " is choosing his color...", false));
            } else {
                sendAllExcept(currentClientConnection, new InfoMessage(">The game assigned to "
                        + gameModel.getPlayerById(currentClientConnection).getNickname() + " the last color: "
                        + availableColors.get(0).toString(), false));
                gameModel.getPlayerById(currentClientConnection).setColor(availableColors.get(0).toString());
                availableColors.remove(0);
                phase = GameHandlerPhase.SETUP_WIZARD;
                setupGame();
            }
        } else if (phase == GameHandlerPhase.SETUP_WIZARD) {
            clients.get(currentClientConnection).sendMessage(new MultipleChoiceMessage(availableWizards, false));
            if (availableWizards.size() > 1) {
                sendAllExcept(currentClientConnection, new InfoMessage(">" + gameModel
                        .getPlayerById(currentClientConnection).getNickname() + " is choosing his wizard...", false));
            } else {
                sendAllExcept(currentClientConnection, new InfoMessage(">The game assigned to "
                        + gameModel.getPlayerById(currentClientConnection).getNickname() + " the last wizard: "
                        + availableWizards.get(0).toString(), false));
                gameModel.getPlayerById(currentClientConnection).getDeck().setWizard(availableWizards.get(0).toString());
                setClientIdOrder();
                clients.sort(new IdComparator());
                turnNumber = 1;
                phase = GameHandlerPhase.PLANNING;
                gameModel.createBoard();
                for (VirtualView view : clients) {
                    view.sendMessage(new ConnectionIdMessage(clients.indexOf(view)));
                }
                planningTurn(null);
            }
        }
    }

    /**
     * Method planningTurn manages cloud filling and the selection of Assistant Card
     * for each player
     * @param  error specifies if the gameHandler has to ask again for an AssistantCard
     *               or if it is the first time.
     */
    private void planningTurn(String error) {
        if (controller.getPhase() == Action.SETUP_CLOUD) {
            controller.setClouds();
        }
        if (error == null) {
            sendAll(new UpdateBoard(gameModel.getBoard()));
            currentClientConnection = gameModel.getCurrentPlayer().getClientID();
            sendAllExcept(currentClientConnection, new InfoMessage(">" + gameModel
                    .getCurrentPlayer().getNickname() + " is choosing the AssistantCard...", true));
        } else {
            clients.get(currentClientConnection)
                    .sendMessage(new InfoMessage(error, false));
            sendAllExcept(currentClientConnection, new InfoMessage(">" + gameModel
                    .getCurrentPlayer().getNickname() + " chosen an invalid Assistant Card...", false));
        }
        clients.get(currentClientConnection)
                .sendMessage(new AskActionMessage(controller.getPhase(), gameModel
                        .getCurrentPlayer().getDeck().getAssistantCards(), error != null));
    }

    /**
     * Method actionTurn manages the actual game round for a player.
     */
    private void actionTurn() {
        currentClientConnection = gameModel.getCurrentPlayer().getClientID();
        AskActionMessage askActionMessage = null;
        switch (controller.getPhase()) {
            case CHOOSE_CHARACTER_CARD:
                askActionMessage = new AskActionMessage(controller.getPhase(),
                        ((BoardExpert) gameModel.getBoard()).getCharacterCards(), error != null);
                if (error == null)
                    sendAllExcept(currentClientConnection, new InfoMessage(">"
                            + gameModel.getCurrentPlayer().getNickname() + " is deciding whether to use a card...", error == null));
                break;
            case USE_CHARACTER_CARD:
                askActionMessage = new AskActionMessage(controller.getPhase(),
                        ((BoardExpert) gameModel.getBoard()).getCharacterCardbyName(controller.getCharacterCardName()),
                        gameModel.getBoard().getIslands(), gameModel.getBoard()
                        .getSchoolByOwnerId(gameModel.getCurrentPlayer().getClientID()), error != null);
                sendAllExcept(currentClientConnection, new InfoMessage(">"
                        + gameModel.getCurrentPlayer().getNickname() + " is using the card: " + controller.getCharacterCardName(), error == null));
                break;
            case DEFAULT_MOVEMENTS:
                askActionMessage = new AskActionMessage(controller.getPhase(), gameModel.getBoard()
                        .getSchoolByOwnerId(gameModel.getCurrentPlayer().getClientID()),
                        gameModel.getBoard().getIslands(), error != null);
                sendAllExcept(currentClientConnection, new InfoMessage(">"
                        + gameModel.getCurrentPlayer().getNickname() + " is moving a student from the Entrance...", error == null));
                break;
            case MOVE_MOTHER_NATURE:
                int increment = 0;
                if (controller.getCharacterCardName() != null && controller.getCharacterCardName().equalsIgnoreCase("POSTMAN"))
                    increment = 2;
                askActionMessage = new AskActionMessage(controller.getPhase(), gameModel
                        .getPlayerById(currentClientConnection)
                        .getChosenAssistantCard().getMotherNatureSteps() + increment, error != null);
                sendAllExcept(currentClientConnection, new InfoMessage(">"
                        + gameModel.getCurrentPlayer().getNickname() + " is choosing where to move mother nature...", error == null));
                break;
            case CHOOSE_CLOUD:
                askActionMessage = new AskActionMessage(controller.getPhase(), gameModel
                        .getBoard().getClouds(), error != null);
                sendAllExcept(currentClientConnection, new InfoMessage(">"
                        + gameModel.getCurrentPlayer().getNickname() + " is choosing the cloud...", error == null));
                break;
        }
        clients.get(currentClientConnection).sendMessage(askActionMessage);
    }

    /**
     * This method is called when a player chooses a nickname.
     * It checks the availability of the nickname and notify the other players the
     * chosen nickname.
     * @param message message with the chosen nickname
     */
    private void setupNickname(SetupMessage message) {
        if (message.getString().isEmpty() || message.getString().equalsIgnoreCase("")) {
            clients.get(currentClientConnection)
                    .sendMessage(new NicknameMessage(true));
            return;
        }
        for (Player p : gameModel.getPlayers()) {
            if (p.getNickname().equals(message.getString())) {
                clients.get(currentClientConnection)
                        .sendMessage(new NicknameMessage(true));
                return;
            }
        }
        sendAllExcept(currentClientConnection, new InfoMessage(">The player #"
                + (currentClientConnection + 1) + " has chosen his nickname: " + message.getString(), false));
        gameModel.createPlayer(message.getString(), currentClientConnection);
        phase = GameHandlerPhase.SETUP_COLOR;
        setupGame();
    }

    /**
     * This method is called when a player wants to disconnect.
     * All the connections are closed and the server removes the gameHandler
     * from the list of active games.
     * @param disconnected id of the client who wants to disconnect
     */
    public void endGame(int disconnected) {
        String player = gameModel.getPlayerById(disconnected) != null ? gameModel.getPlayerById(disconnected).getNickname() : "The client #" + (disconnected + 1);
        sendAllExcept(disconnected, new InfoMessage(">" + player
                + " has disconnected, the match will now end" + "\nThanks for playing!", false, true));
        clients.remove(clients.get(disconnected));
        for (VirtualView client : clients) {
            client.closeConnection(false);
        }
        server.removeGameHandler(this);
    }

    /**
     * This method notify to all players the winner.
     * It notifies each player of the disconnection.
     * It notifies the server which removes it from the list of active Games.
     */
    public void endGame() {
        sendAll(new UpdateBoard(gameModel.getBoard()));
        String winner = gameModel.getWinner() != null ? gameModel.getWinner().getNickname() : "draw";
        String result = !winner.equalsIgnoreCase("draw") ? (">The winner is " + winner
                + "!" + "\nThanks for playing!") : "There are no winners. The match ended in a draw!";
        InfoMessage message = new InfoMessage(result, false, winner);
        sendAll(message);
        for (VirtualView client : clients) {
            //client.closeConnection(false);
            Thread t = new Thread(() -> client.closeConnection(false));
            t.start();
        }
        server.removeGameHandler(this);
    }

    /**
     * This method sends the same message to all the clients.
     */
    public void sendAll(Message message) {
        for (VirtualView client : clients) {
            client.sendMessage(message);
        }
    }

    /**
     * This method sends the same message to all the clients,
     * except the one specified in the parameter client id
     * @param clientId id of the player who should not receive the message.
     * @param message message to be sent.
     */
    public void sendAllExcept(int clientId, Message message) { //TODO non so se serve
        for (VirtualView client : clients) {
            if (client.getClientId() != clientId)
                client.sendMessage(message);
        }
    }

    /**
     * Method for the game mode '4 players'.
     * It set the clientId of players :
     * FIRST_CLIENT_WHITE:0   SECOND_CLIENT_WHITE:2
     * FIRST_CLIENT_BLACK.1   FIRST_CLIENT_BLACK:3
     */
    //Serve per quattro giocatori. In questo modo il primo è bianco con torri e il secondo nero con torri
    private void setClientIdOrder() {
        int whiteIndex = 0, blackIndex = 1;
        for (int i = 0; i < 4; i++) {
            if (gameModel.getPlayers().get(i).getColor().equals(PlayerColor.WHITE)) {
                gameModel.getPlayers().get(i).setClientID(whiteIndex);
                clients.get(i).setClientId(whiteIndex);
                whiteIndex += 2;
            } else {
                gameModel.getPlayers().get(i).setClientID(blackIndex);
                clients.get(i).setClientId(blackIndex);
                blackIndex += 2;
            }
        }
    }

    /**
     * @param evt A PropertyChangeEvent object describing the event source
     *            and the property that has changed.
     */
    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        switch (evt.getPropertyName()) {
            case "end_game":
                endGame();
                break;
            case "set_assistantCard":
                sendAllExcept(gameModel.getCurrentPlayer().getClientID(), new InfoMessage(">" + gameModel.getCurrentPlayer().getNickname() + " has chosen che AssistantCard with priority #" + ((ActionMessage) evt.getNewValue()).getData(), false));
                break;
        }
    }
}

/**
 * Class that override the comparator method to sort virtualViews according to the clientId.
 */
class IdComparator implements Comparator<VirtualView> {
    @Override
    public int compare(VirtualView v1, VirtualView v2) {
        return Integer.compare(v1.getClientId(), v2.getClientId());
    }
}
