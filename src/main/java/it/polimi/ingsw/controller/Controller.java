package it.polimi.ingsw.controller;

import it.polimi.ingsw.controller.actioncontroller.*;

import it.polimi.ingsw.exceptions.*;
import it.polimi.ingsw.server.ConnectionMessage.ActionMessage;
import it.polimi.ingsw.server.model.AssistantCard;
import it.polimi.ingsw.server.model.Cloud;
import it.polimi.ingsw.server.model.GameModel;
import it.polimi.ingsw.server.model.BoardExpert;

import java.util.ArrayList;
import java.util.List;

public class Controller {
    private final GameModel gameModel;
    private ActionController actionController;
    private int playerTurnNumber;
    private int characterCardMovements;
    private int defaultMovements;
    private boolean alreadyUsedCharacterCard;
    private Action phase;
    private String characterCardName;

    public Controller(GameModel gameModel) {
        this.gameModel = gameModel;
        this.phase = Action.SETUP_CLOUD;
        playerTurnNumber = 0;
        characterCardMovements = 0;
        defaultMovements = 0;
        alreadyUsedCharacterCard = false;
    }

    public void setClouds() {
        gameModel.getBoard().setStudentsonClouds();
        phase = Action.CHOOSE_ASSISTANT_CARD;
    }

    public void setAssistantCard(ActionMessage actionMessage) {
        //TODO forse serve un'eccezione per il controllo del player giusto
        if(playerTurnNumber>0)
        {
            try {
                checkSameAssistantCard(actionMessage.getData());
            } catch (SameAssistantCardException e) {
                System.out.println(e.getMessage());
            }
        }
        gameModel.getCurrentPlayer().setAssistantCard(actionMessage.getData());
        playerTurnNumber++;
        if(playerTurnNumber==gameModel.getPlayersNumber()) {
            playerTurnNumber = 0;
            gameModel.setPlayersOrder();
            startPlayerTurn();
        }
    }

    private void startPlayerTurn() {
        actionController = new ActionController(gameModel);
        characterCardMovements = 0;
        defaultMovements = 0;
        alreadyUsedCharacterCard = false;
        phase = Action.DEFAULT_MOVEMENTS;
        gameModel.setCurrentPlayer(playerTurnNumber);
    }

    public void nextAction(ActionMessage actionMessage) {
        switch (actionMessage.getAction()) {
            case USE_SPECIAL_CARD: // in questo modo la specialcard si può usare in qualunque momento del proprio turno tranne dopo aver scelto la nuvola
                try {
                    if(alreadyUsedCharacterCard) checkAlreadyUsedCharacterCard(actionMessage);
                    else checkCoins(actionMessage);
                } catch (AlreadyUsedCharacterCardException e) {
                    System.out.println(e.getMessage());
                } catch (NotEnoughCoinsException e) {
                    System.out.println(e.getMessage());
                }
                if(!alreadyUsedCharacterCard) { //se non è stata usata una carta la uso
                    alreadyUsedCharacterCard = true;
                    characterCardName= actionMessage.getCharacterCardName();
                    setCharacterCardEffect(actionMessage);
                } else // se è stata usata ed è clown o performer, utilizzo l'effetto
                {
                    actionController.getCharacterCardStrategy().useEffect(actionMessage); //serve per non settare la strategia più volte se è gia stata settata
                }
                characterCardMovements++; // incremento il numero di volte che è stata usata la carta personaggio scelta
            case DEFAULT_MOVEMENTS:
                try {
                    checkPhase(actionMessage); //TODO sistemare
                    checkDefaultMovements(actionMessage);
                } catch (IncorrectPhaseException e) {
                    System.out.println(e.getMessage());
                } catch (DefaultMovementsNumberException e) {
                    System.out.println(e.getMessage());
                } catch (DefaultMovementsColorException e) {
                    System.out.println(e.getMessage());
                }
                if(actionMessage.getData()==-1) { // significa che non devo spostare studente da hall a isola
                    actionController.moveStudent(actionMessage.getFirstParameter());
                }
                else {
                    actionController.moveStudent(actionMessage.getData(), actionMessage.getFirstParameter());
                }
                defaultMovements++;
                if(defaultMovements>=4
                        || (defaultMovements>=3 && gameModel.getPlayers().size()%2==0 )) {
                    phase = Action.MOVE_NATURE_MOTHER;
                }
            case GET_INFLUENCE: //TODO non so se è necessario
                actionController.getInfluence(actionMessage);
            case MOVE_NATURE_MOTHER:
                try {
                    checkPhase(actionMessage);
                    checkChosenSteps(actionMessage);
                } catch (IncorrectPhaseException e) {
                    System.out.println(e.getMessage());
                }catch (InvalidChosenStepsException e) {
                    System.out.println(e.getMessage());
                }
                actionController.moveMotherNature(actionMessage);
                phase = Action.CHOOSE_CLOUD;
                /*if(gameModel.isHardcore()) {
                    int newIslandPosition = (gameModel.getBoard().getNatureMotherPosition()
                                            + ((NatureMotherMessage) message).getChoosenSteps())
                                            % gameModel.getBoard().getIslands().size();
                    BoardHard boardHard = (BoardHard) gameModel.getBoard();
                    if (gameModel.getBoard().getIslands().get(newIslandPosition).isLocked()) {
                        boardHard.removeLock(newIslandPosition);
                        ((SpecialCardwithProhibitions) (boardHard.getSpecialCardbyName("Herbolaria"))).restockProhibitionsNumber();
                    }
                    else actionController.getInfluence(message);
                }
                else actionController.getInfluence(message);*/
            case CHOOSE_CLOUD:
                try {
                    checkPhase(actionMessage);
                    checkCloud(actionMessage);
                } catch (IncorrectPhaseException e) {
                    System.out.println(e.getMessage());
                }catch (EmptyCloudException e) {
                    System.out.println(e.getMessage());
                }
                actionController.moveStudent(actionMessage.getData());
                endPlayerTurn(); // in questo modo dopo la scelta della nuvola non si può giocare la carta personaggio
        }
    }

    private void endPlayerTurn() {
        playerTurnNumber++;
        if(playerTurnNumber==gameModel.getPlayersNumber())
        {
            phase = Action.SETUP_CLOUD;
        }
        else
        {
            startPlayerTurn();
        }
    }

    private void setCharacterCardEffect(ActionMessage actionMessage) //setta la strategy oppure crea l'actioncontroller giusto
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
                actionController = new Lumberjack(gameModel, actionMessage.getFirstParameter());
                break;
            default:
                strategy = true; //setta strategia e usa effetto
                break;
        }
        actionController.useCharacterCard(actionMessage, strategy);
    }

    private void checkSameAssistantCard(int priority) throws SameAssistantCardException {
        //TODO forse sarebbe utile avere una lista con le assistantcards scelte nel turno corrente nella board e non nel player
        List<Integer> chosenAssistantCards = new ArrayList<>(); //lista contenente le assistantcards già scelte nel turno corrente
        for(int i=0; i<playerTurnNumber; i++) {
            chosenAssistantCards.add(gameModel.getPlayers().get(i).getChoosenAssistantCard().getPriority());
        }
        /*if(chosenAssistantCards.contains(priority)
                && !chosenAssistantCards.containsAll(gameModel.getCurrentPlayer().getDeck().getAssistantCards())) {
            throw new SameAssistantCardException();
        }*/
        if(chosenAssistantCards.contains(priority)) { //casting automatico da int a integer
            for(AssistantCard assistantCard: gameModel.getCurrentPlayer().getDeck().getAssistantCards()) {
                if(!chosenAssistantCards.contains(assistantCard.getPriority())) throw new SameAssistantCardException();
            }
        }
    }

    
    private void checkCoins(ActionMessage actionMessage) throws NotEnoughCoinsException {
        BoardExpert boardexpert = (BoardExpert) gameModel.getBoard();
        int cost = boardexpert.getCharacterCardbyName(actionMessage.getCharacterCardName()).getCost();
        if(boardexpert.getPlayerCoins(gameModel.getCurrentPlayer().getNickname())<cost)
        {
            throw new NotEnoughCoinsException();
        }
    }

    private void checkCloud(ActionMessage actionMessage) throws EmptyCloudException {
        //TODO conviene usare un'array di cloud? Meglio una lista??
        Cloud[] cloud = gameModel.getBoard().getClouds() ;
        if(cloud[actionMessage.getData()].getStudents().isEmpty())
        {
            throw new EmptyCloudException();
        }
    }
    //TODO controllare se usare characterCardName e i suoi metodi
    private void checkChosenSteps(ActionMessage actionMessage) throws InvalidChosenStepsException {
        int steps = gameModel.getCurrentPlayer().getChoosenAssistantCard().getMotherNatureSteps();
        if(characterCardName.equalsIgnoreCase("POSTMAN")) {
            steps += 2;
        }
        if(actionMessage.getData() > steps) {
            throw new InvalidChosenStepsException();
        }
    }

    private void checkAlreadyUsedCharacterCard(ActionMessage actionMessage) throws AlreadyUsedCharacterCardException {
        if((!actionMessage.getCharacterCardName().equalsIgnoreCase("CLOWN")
                && !actionMessage.getCharacterCardName().equalsIgnoreCase("PERFORMER"))
                || (actionMessage.getCharacterCardName().equalsIgnoreCase("CLOWN") && characterCardMovements>=3)
                || (actionMessage.getCharacterCardName().equalsIgnoreCase("PERFORMER") && characterCardMovements>=2)){
            throw new AlreadyUsedCharacterCardException();
        }
    }

    private void checkDefaultMovements(ActionMessage actionMessage) throws DefaultMovementsNumberException, DefaultMovementsColorException {
        if(defaultMovements>=4
                || (defaultMovements>=3 && gameModel.getPlayers().size()%2==0 ))
            throw new DefaultMovementsNumberException();
        else if(!(gameModel.getBoard().getSchoolByOwner(gameModel.getCurrentPlayer()
                .getNickname()).hasEntranceStudentColor(actionMessage.getFirstParameter()))) {
            throw new DefaultMovementsColorException();
        }
    }

    private void checkPhase(ActionMessage actionMessage) throws IncorrectPhaseException {
        if(!actionMessage.getAction().equals(phase)) throw new IncorrectPhaseException();
    }
}
