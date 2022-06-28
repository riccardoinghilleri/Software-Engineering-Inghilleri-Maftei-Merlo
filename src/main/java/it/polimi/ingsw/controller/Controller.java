package it.polimi.ingsw.controller;

import it.polimi.ingsw.controller.actioncontroller.*;

import it.polimi.ingsw.enums.Action;
import it.polimi.ingsw.exceptions.*;
import it.polimi.ingsw.server.ConnectionMessage.ActionMessage;
import it.polimi.ingsw.server.GameHandler;
import it.polimi.ingsw.server.model.*;

import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.List;

/**
 * This class is the main controller class, it manages different 'action phase'.
 * It manages the players turns , sets the turn starting, the next action and the turn ending.
 *
 * @see PropertyChangeSupport
 */
public class Controller {
    private final GameModel gameModel;
    private ActionController actionController;
    private int playerTurnNumber;
    private int characterCardMovements;
    private int defaultMovements;
    private boolean alreadyUsedCharacterCard;
    private Action phase;
    private List<Action> availableActions;
    private String characterCardName;

    private int maxCharacterCardsMovements;

    private final PropertyChangeSupport listeners;

    /**
     * The constructor creates a new controller instance
     *
     * @param gameModel   of type game - GameModel reference.
     * @param gameHandler of type GameHandler - GameHandler reference.
     *                    The game start with the phase 'SETUP_CLOUD'
     */
    public Controller(GameModel gameModel, GameHandler gameHandler) {
        this.gameModel = gameModel;
        this.phase = Action.SETUP_CLOUD;
        playerTurnNumber = 0;
        characterCardMovements = -1;
        defaultMovements = 0;
        alreadyUsedCharacterCard = false;
        this.maxCharacterCardsMovements = -1;
        listeners = new PropertyChangeSupport(this);
        listeners.addPropertyChangeListener("end_game", gameHandler);
        listeners.addPropertyChangeListener("set_assistantCard", gameHandler);
        listeners.addPropertyChangeListener("change_turn", gameHandler);
        availableActions = new ArrayList<>();
    }

    public int getPlayerTurnNumber() {
        return playerTurnNumber;
    }

    /**
     * This method returns the character card name
     */
    public String getCharacterCardName() {
        return characterCardName;
    }

    /**
     * This method returns the phase name ( enum Action)
     */
    public Action getPhase() {
        return phase;
    }

    /**
     * This method calls the board to fill the clouds.
     * It moves the phase to the next one, that is CHOOSE_ASSISTANT_CARD
     * Moreover sets the current player throw the gameModel.
     */
    public void setClouds() {
        gameModel.getBoard().setStudentsonClouds();
        phase = Action.CHOOSE_ASSISTANT_CARD;
        gameModel.setCurrentPlayer(playerTurnNumber);
    }

    /**
     * Upon an action message this method sets the assistant card for a player, checking the validity of the choice.
     * When all have already chosen it calls the game Model to set the players order and start the turn of the first player.
     *
     * @param actionMessage message to communicate the assistant card
     * @return false if another player has chosen the same assistant card before, true otherwise, setting the assistant card.
     */
    public boolean setAssistantCard(ActionMessage actionMessage) {
        //TODO forse serve un'eccezione per il controllo del player giusto
        if (playerTurnNumber > 0) {
            try {
                checkSameAssistantCard(actionMessage.getData());
            } catch (SameAssistantCardException e) {
                //System.out.println(e.getMessage());
                return false;
            }
        }
        gameModel.getCurrentPlayer().setAssistantCard(actionMessage.getData());
        listeners.firePropertyChange("set_assistantCard", null, actionMessage);
        playerTurnNumber++;
        if (playerTurnNumber == gameModel.getPlayersNumber()) {
            playerTurnNumber = 0;
            gameModel.setPlayersOrder();
            startPlayerTurn();
        } else {
            Player nextPlayer = gameModel.getPlayerById((gameModel.getCurrentPlayer().getClientID() + 1) % gameModel.getPlayersNumber());
            gameModel.setCurrentPlayer(gameModel.getPlayers().indexOf(nextPlayer));
            //gameModel.setCurrentPlayer(playerTurnNumber);
        }
        return true;
    }

    /**
     * This method starts the turn for the current player.
     * Initially sets the available actions at default actions.
     * Then it checks if the game is expert mode: if so the player is presented the choice of character card.
     */
    private void startPlayerTurn() {
        gameModel.setCurrentPlayer(playerTurnNumber);
        listeners.firePropertyChange("change_turn", null, playerTurnNumber);
        characterCardMovements = -1;
        defaultMovements = 0;
        characterCardName = null;
        alreadyUsedCharacterCard = false;
        availableActions = Action.getDefaultActions();
        if (gameModel.isExpertGame()) {
            phase = Action.CHOOSE_CHARACTER_CARD;
        } else phase = availableActions.remove(0);
        actionController = new ActionController(gameModel);
    }

    /**
     * This method  sets the next action, using a switch case constructor.
     * To be noticed that the character card can be used whenever after choosing the cloud.
     *
     * @param actionMessage contains the action to be made
     * @return a string of exception, if needed.
     * Otherwise, every action is redirected to the action controller.
     */
    public String nextAction(ActionMessage actionMessage) {
        switch (actionMessage.getAction()) {
            case CHOOSE_CHARACTER_CARD:
                if (actionMessage.getCharacterCardName() == null) {
                    phase = availableActions.remove(0);
                } else {
                    try {
                        checkCoins(actionMessage);
                    } catch (NotEnoughCoinsException e) {
                        //System.out.println(e.getMessage());
                        return "You have not got enough coins";
                    }
                    characterCardName = actionMessage.getCharacterCardName();
                    if (characterCardName.equalsIgnoreCase("PERFORMER") && gameModel.getBoard().getSchoolByOwnerId(gameModel.getCurrentPlayer().getClientID()).getNumDiningRoomStudents() == 0)
                        return "You can not choose this character card. You don't have enough students in your dining room!";
                    alreadyUsedCharacterCard = true;
                    // if the strategy is not set and the character card's name is not 'lumberjack' or the name is  'postman'.
                    if ((!setCharacterCardEffect(actionMessage) && !characterCardName.equalsIgnoreCase("LUMBERJACK")) || characterCardName.equalsIgnoreCase("POSTMAN")) {
                        phase = availableActions.remove(0);
                    } else phase = Action.USE_CHARACTER_CARD;
                }
                break;
            case USE_CHARACTER_CARD: // in questo modo la specialcard si può usare in qualunque momento del proprio turno tranne dopo aver scelto la nuvola
                /*if (!alreadyUsedCharacterCard && actionMessage.getCharacterCardName() == null) {
                    phase = availableActions.remove(0);
                } else if (!alreadyUsedCharacterCard) {
                    try {
                        checkCoins(actionMessage);
                    } catch (NotEnoughCoinsException e) {
                        System.out.println(e.getMessage());
                        return "You have not got enough coins";
                    }
                    alreadyUsedCharacterCard = true;
                    characterCardName = actionMessage.getCharacterCardName();
                    if (!setCharacterCardEffect(actionMessage) && !characterCardName.equalsIgnoreCase("LUMBERJACK"))
                        phase = availableActions.remove(0);
                    characterCardName = actionMessage.getCharacterCardName();
                } else {
                    if (characterCardName.equalsIgnoreCase("LUMBERJACK")) {
                        actionController = new Lumberjack(gameModel, actionMessage.getParameters().get(0));
                        phase = availableActions.remove(0);
                    } else*/
                if (characterCardName.equalsIgnoreCase("LUMBERJACK")) {
                    ((Lumberjack) actionController).setColor(actionMessage.getParameters().get(0));
                    phase = availableActions.remove(0);
                } else {
                    if ((characterCardName.equalsIgnoreCase("PERFORMER") || characterCardName.equalsIgnoreCase("CLOWN")) && characterCardMovements == -1) {
                        maxCharacterCardsMovements = actionMessage.getData();
                        characterCardMovements++;
                    } else {
                        actionController.useCharacterCardEffect(actionMessage);
                        characterCardMovements++;
                        if (characterCardName.equalsIgnoreCase("DIPLOMAT")) {
                            for (School s : gameModel.getBoard().getSchools()) {
                                if (s.getTowersNumber() == 0) {
                                    gameModel.setWinner(gameModel.getPlayerById(s.getOwnerId()));
                                    listeners.firePropertyChange("end_game", null, null);
                                }
                            }
                        }
                        try {//TODO usare direttamente if dentro metodo check
                            checkAlreadyUsedCharacterCard(characterCardName);
                        } catch (AlreadyUsedCharacterCardException e) {
                            phase = availableActions.remove(0);
                        }
                    }
                }
                break;
            case DEFAULT_MOVEMENTS:
                try {
                    checkPhase(actionMessage); //TODO sistemare
                    checkDefaultMovements(actionMessage);
                } catch (IncorrectPhaseException e) {
                    System.out.println(e.getMessage());
                    return "Invalid action!";
                } catch (DefaultMovementsNumberException e) {
                    System.out.println(e.getMessage());
                    return "You can not move another student from the entrance!";
                } catch (DefaultMovementsColorException e) {
                    System.out.println(e.getMessage());
                    return "There is not a student of this color in your entrance.";
                }
                if (actionMessage.getData() == -1) { // significa che non devo spostare studente da entrance a isola
                    actionController.moveStudent(actionMessage.getParameters().get(0).toUpperCase());
                } else {
                    actionController.moveStudent(actionMessage.getData(), actionMessage.getParameters().get(0).toUpperCase());
                }
                defaultMovements++;
                if (defaultMovements >= 4
                        || (defaultMovements >= 3 && gameModel.getPlayers().size() % 2 == 0)) {
                    if (gameModel.isExpertGame() && !alreadyUsedCharacterCard)
                        phase = Action.CHOOSE_CHARACTER_CARD;
                    else phase = availableActions.remove(0);
                }
                break;
            case MOVE_MOTHER_NATURE:
                try {
                    checkPhase(actionMessage);
                    checkChosenSteps(actionMessage);
                } catch (IncorrectPhaseException e) {
                    System.out.println(e.getMessage());
                    return "Invalid action";
                } catch (InvalidChosenStepsException e) {
                    System.out.println(e.getMessage());
                    return " You can not move mother nature so far";
                }
                int newOwner = actionController.moveMotherNature(actionMessage);
                if (newOwner != -1
                        && gameModel.getBoard().getSchoolByOwnerId(newOwner).getTowersNumber() == 0) {
                    gameModel.setWinner(gameModel.getPlayerById(newOwner));
                    listeners.firePropertyChange("end_game", null, null);
                } else if (gameModel.getBoard().getIslands().size() == 3) {
                    gameModel.getBoard().findWinner();
                    listeners.firePropertyChange("end_game", null, null);
                }
                if (gameModel.isExpertGame() && !alreadyUsedCharacterCard)
                    phase = Action.CHOOSE_CHARACTER_CARD;
                else phase = availableActions.remove(0);
                break;
            case CHOOSE_CLOUD:
                try {
                    checkPhase(actionMessage);
                    checkCloud(actionMessage);
                } catch (IncorrectPhaseException e) {
                    System.out.println(e.getMessage());
                    return "Invalid action";
                } catch (EmptyCloudException e) {
                    System.out.println(e.getMessage());
                    return "You have chose an empty cloud.";
                }
                actionController.moveStudent(actionMessage.getData());
                endPlayerTurn(); // in questo modo dopo la scelta della nuvola non si può giocare la carta personaggio
                break;
        }
        return null;
    }

    /**
     * This method ends the turn of a player, then switches to the start of another player turn.
     */
    private void endPlayerTurn() {
        playerTurnNumber++;
        characterCardName = null;
        if (playerTurnNumber == gameModel.getPlayersNumber()) {
            playerTurnNumber = 0;
            phase = Action.SETUP_CLOUD;
        } else {
            startPlayerTurn();
        }
    }

    /**
     * This method sets the strategy and use the effect or creates the correct action controller, throw the name of character card,
     * gotten from the action message.
     *
     * @param actionMessage message containing the action to be done
     * @return the value of the strategy.
     */
    private boolean setCharacterCardEffect(ActionMessage actionMessage) //setta la strategy oppure crea l'action controller giusto
    {
        boolean strategy = false;
        switch (actionMessage.getCharacterCardName().toUpperCase()) { //TODO ormai il current player è nel gamemodel quindi nel costruttore dell'actioncontroller e dello strategyfactory il player non serve più
            case "CENTAUR":
                actionController = new Centaur(gameModel);
                break;
            case "DINER":
                actionController = new Diner(gameModel);
                break;
            case "KNIGHT":
                actionController = new Knight(gameModel);
                break;
            case "LUMBERJACK":
                actionController = new Lumberjack(gameModel);
                break;
            default:
                strategy = true; //setta strategia e usa effetto
                break;
        }
        actionController.useCharacterCard(actionMessage, strategy);
        return strategy;
    }

    /**
     * This method checks if a player chooses an assistant card previously chosen by another player.
     * If so, an exception is thrown
     *
     * @param priority value used to check. ( 2 players can't choose 2 assistant card with same priority)
     * @throws SameAssistantCardException type of exception.
     */
    private void checkSameAssistantCard(int priority) throws SameAssistantCardException {
        //TODO forse sarebbe utile avere una lista con le assistantcards scelte nel turno corrente nella board e non nel player
        List<Integer> chosenAssistantCards = new ArrayList<>(); //lista contenente le assistantcards già scelte nel turno corrente
        for (int i = 0; i < gameModel.getPlayersNumber(); i++) {
            if (gameModel.getPlayers().get(i).getChosenAssistantCard() != null)
                chosenAssistantCards.add(gameModel.getPlayers().get(i).getChosenAssistantCard().getPriority());
        }
        /*if(chosenAssistantCards.contains(priority)
                && !chosenAssistantCards.containsAll(gameModel.getCurrentPlayer().getDeck().getAssistantCards())) {
            throw new SameAssistantCardException();
        }*/
        if (chosenAssistantCards.contains(priority)) { //casting automatico da int a integer
            for (AssistantCard assistantCard : gameModel.getCurrentPlayer().getDeck().getAssistantCards()) {
                if (!chosenAssistantCards.contains(assistantCard.getPriority()))
                    throw new SameAssistantCardException();
            }
        }
    }

    /**
     * This method checks if a player has enough coins to choose a specified character card, throwing an exception otherwise.
     *
     * @param actionMessage message containing the action to be done
     * @throws NotEnoughCoinsException type of exception
     */
    private void checkCoins(ActionMessage actionMessage) throws NotEnoughCoinsException {
        BoardExpert boardexpert = (BoardExpert) gameModel.getBoard();
        int cost = boardexpert.getCharacterCardbyName(actionMessage.getCharacterCardName()).getCost();
        if (boardexpert.getPlayerCoins(gameModel.getCurrentPlayer().getClientID()) < cost) {
            throw new NotEnoughCoinsException();
        }
    }

    /**
     * This method controls if a student chooses an empty cloud, if so an exception is thrown.
     *
     * @param actionMessage message containing the action to be done
     * @throws EmptyCloudException type of exception
     */
    private void checkCloud(ActionMessage actionMessage) throws EmptyCloudException {
        //TODO conviene usare un'array di cloud? Meglio una lista??
        Cloud[] cloud = gameModel.getBoard().getClouds();
        if (cloud[actionMessage.getData()].getStudents().isEmpty()) {
            throw new EmptyCloudException();
        }
    }

    /**
     * This method checks if the steps of mother nature are valid according to the assistant card.
     * It pays attention also to the character card POSTMAN which allows 2 more steps than those shown on the assistant card.
     *
     * @param actionMessage message containing the action to be done
     * @throws InvalidChosenStepsException type of exception thrown
     */
    //TODO controllare se usare characterCardName e i suoi metodi
    private void checkChosenSteps(ActionMessage actionMessage) throws InvalidChosenStepsException {
        int steps = gameModel.getCurrentPlayer().getChosenAssistantCard().getMotherNatureSteps();
        if (characterCardName != null && characterCardName.equalsIgnoreCase("POSTMAN")) {
            steps += 2;
        }
        if (actionMessage.getData() > steps) {
            throw new InvalidChosenStepsException();
        }
    }

    /**
     * This method checks if a character card has already been used by another player:
     * if so an exception is thrown.
     *
     * @param characterCardName name fo the chosen card
     * @throws AlreadyUsedCharacterCardException type of exception thrown
     */
    private void checkAlreadyUsedCharacterCard(String characterCardName) throws AlreadyUsedCharacterCardException {
        if ((!characterCardName.equalsIgnoreCase("CLOWN")
                && !characterCardName.equalsIgnoreCase("PERFORMER"))
                || (characterCardName.equalsIgnoreCase("CLOWN") && characterCardMovements >= Math.min(3, maxCharacterCardsMovements))
                || (characterCardName.equalsIgnoreCase("PERFORMER") && characterCardMovements >= Math.min(2, maxCharacterCardsMovements))) {
            throw new AlreadyUsedCharacterCardException();
        }
    }

    /**
     * This method checks if a player chooses a student of a color that doesn't exist in his entrance, or if he
     * has already  finished all the default movements available.
     *
     * @param actionMessage message containing the action to be done
     * @throws DefaultMovementsNumberException type of exception thrown
     * @throws DefaultMovementsColorException  type of exception thrown
     */
    private void checkDefaultMovements(ActionMessage actionMessage) throws DefaultMovementsNumberException, DefaultMovementsColorException {
        if (defaultMovements >= 4
                || (defaultMovements >= 3 && gameModel.getPlayers().size() % 2 == 0))
            throw new DefaultMovementsNumberException();
        else if (!(gameModel.getBoard().getSchoolByOwnerId(gameModel.getCurrentPlayer().getClientID()).
                hasEntranceStudentColor(actionMessage.getParameters().get(0)))) {
            throw new DefaultMovementsColorException();
        }
    }

    /**
     * This method checks if the player does the correct action in the correct phase, throwing an exception otherwise.
     *
     * @param actionMessage message containing the action to be done.
     * @throws IncorrectPhaseException : type of exception thrown.
     */
    private void checkPhase(ActionMessage actionMessage) throws IncorrectPhaseException {
        if (!actionMessage.getAction().equals(phase))
            throw new IncorrectPhaseException();
    }
}
