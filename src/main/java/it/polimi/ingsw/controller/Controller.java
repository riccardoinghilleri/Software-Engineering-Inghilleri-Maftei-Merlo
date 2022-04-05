package it.polimi.ingsw.controller;

import it.polimi.ingsw.controller.actioncontroller.*;

import it.polimi.ingsw.exceptions.*;
import it.polimi.ingsw.model.AssistantCard;
import it.polimi.ingsw.model.Cloud;
import it.polimi.ingsw.model.GameModel;
import it.polimi.ingsw.model.board.BoardHard;

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

    public Controller(GameModel gameModel) {
        this.gameModel = gameModel;
        this.phase = Action.SETUP_CLOUD;
        playerTurnNumber = 0;
        characterCardMovements = 0;
        defaultMovements = 0;
        alreadyUsedCharacterCard = false;
    }

    public void setCloud() {
        gameModel.getBoard().setClouds();
        phase = Action.CHOOSE_ASSISTANT_CARD;
    }

    public void setAssistantCard(Message message) {
        //TODO forse serve un'eccezione per il controllo del player giusto
        if(playerTurnNumber>0)
        {
            try {
                checkSameAssistantCard(message.getData());
            } catch (SameAssistantCardException e) {
                System.out.println(e.getMessage());
            }
        }
        gameModel.getPlayers().get(playerTurnNumber).setAssistantCard(message.getData());
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
        //TODO aggiungere gamemodel.setcurrentplayer(playerturnnumber) dopo il merge
    }

    public void nextAction(Message message) {
        switch (message.getAction()) {
            case USE_SPECIAL_CARD: //TODO in questo modo la specialcard si può usare in qualunque momento del proprio turno tranne dopo aver scelto la nuvola
                try {
                    if(alreadyUsedCharacterCard) checkAlreadyUsedCharacterCard(message);
                    else checkCoins(message);
                } catch (AlreadyUsedCharacterCardException e) {
                    System.out.println(e.getMessage());
                } catch (NotEnoughCoinsException e) {
                    System.out.println(e.getMessage());
                }
                if(!alreadyUsedCharacterCard) { //se non è stata usata una carta la uso
                    alreadyUsedCharacterCard = true;
                    setCharacterCardEffect(message);
                } else // se è stata usata ed è clown o performer, utilizzo l'effetto
                {
                    actionController.getCharacterCardStrategy().useEffect(message); //serve per non settare la strategia più volte se è gia stata settata
                }
                characterCardMovements++; // incremento il numero di volte che è stata usata la carta personaggio scelta
            case DEFAULT_MOVEMENTS:
                try {
                    checkPhase(message); //TODO sistemare
                    checkDefaultMovements(message);
                } catch (IncorrectPhaseException e) {
                    System.out.println(e.getMessage());
                } catch (DefaultMovementsNumberException e) {
                    System.out.println(e.getMessage());
                } catch (DefaultMovementsColorException e) {
                    System.out.println(e.getMessage());
                }
                if(message.getData()==-1) { // significa che non devo spostare studente da hall a isola
                    actionController.moveStudent(message.getFirstParameter());
                }
                else {
                    actionController.moveStudent(message.getData(),message.getFirstParameter());
                }
                defaultMovements++;
                if(defaultMovements>=4
                        || (defaultMovements>=3 && gameModel.getPlayers().size()%2==0 )) {
                    phase = Action.MOVE_NATURE_MOTHER;
                }
            case GET_INFLUENCE: //TODO non so se è necessario
                actionController.getInfluence(message);
            case MOVE_NATURE_MOTHER:
                try {
                    checkPhase(message);
                    checkChosenSteps(message);
                } catch (IncorrectPhaseException e) {
                    System.out.println(e.getMessage());
                }catch (InvalidChosenStepsException e) {
                    System.out.println(e.getMessage());
                }
                actionController.moveNatureMother(message);
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
                    checkPhase(message);
                    checkCloud(message);
                } catch (IncorrectPhaseException e) {
                    System.out.println(e.getMessage());
                }catch (EmptyCloudException e) {
                    System.out.println(e.getMessage());
                }
                actionController.moveStudent(message.getData());
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

    private void setCharacterCardEffect(Message message) //setta la strategy oppure crea l'actioncontroller giusto
    {
        boolean strategy = false;
        switch (message.getCharacterCardName().toUpperCase()) { //TODO ormai il current player è nel gamemodel quindi nel costruttore dell'actioncontroller e dello strategyfactory il player non serve più
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
                actionController = new Lumberjack(gameModel, message.getFirstParameter());
                break;
            default:
                strategy = true; //setta strategia e usa effetto
                break;
        }
        actionController.useSpecialCard(message, strategy);
    }

    private void checkSameAssistantCard(int priority) throws SameAssistantCardException {
        //TODO forse sarebbe utile avere una lista con le assistantcards scelte nel turno corrente nella board e non nel player
        List<Integer> chosenAssistantCards = new ArrayList<>(); //lista contenente le assistantcards già scelte nel turno corrente
        for(int i=0; i<playerTurnNumber; i++) {
            chosenAssistantCards.add(gameModel.getPlayers().get(i).getChoosenAssistantCard().getPriority());
        }
        if(chosenAssistantCards.contains(priority)) { //casting automatico da int a integer
            for(AssistantCard assistantCard: gameModel.getCurrentPlayer().getDeck().getAssistantCards()) {
                if(!chosenAssistantCards.contains(assistantCard.getPriority())) throw new SameAssistantCardException();
            }
        }
    }

    //TODO sostituire dopo il merge getPlayers().get(playerTurnNumber) con getCurrentPlayer()
    private void checkCoins(Message message) throws NotEnoughCoinsException {
        BoardHard boardhard = (BoardHard) gameModel.getBoard();
        int cost = boardhard.getSpecialCardbyName(message.getCharacterCardName()).getCost();
        if(boardhard.getPlayerCoins(gameModel.getPlayers().get(playerTurnNumber).getNickname())<cost)
        {
            throw new NotEnoughCoinsException();
        }
    }

    private void checkCloud(Message message) throws EmptyCloudException {
        //TODO conviene usare un'array di cloud? Meglio una lista??
        Cloud[] cloud = gameModel.getBoard().getClouds() ;
        if(cloud[message.getData()].getStudents().isEmpty())
        {
            throw new EmptyCloudException();
        }
    }

    private void checkChosenSteps(Message message) throws InvalidChosenStepsException {
        int steps = gameModel.getPlayers().get(playerTurnNumber).getChoosenAssistantCard().getNatureMotherSteps();
        if(actionController.getSpecialCardName().equalsIgnoreCase("POSTMAN")) {
            steps += 2;
        }
        if(message.getData() > steps) {
            throw new InvalidChosenStepsException();
        }
    }

    private void checkAlreadyUsedCharacterCard(Message message) throws AlreadyUsedCharacterCardException {
        if((!message.getCharacterCardName().equalsIgnoreCase("CLOWN")
                && !message.getCharacterCardName().equalsIgnoreCase("PERFORMER"))
                || (message.getCharacterCardName().equalsIgnoreCase("CLOWN") && characterCardMovements>=3)
                || (message.getCharacterCardName().equalsIgnoreCase("PERFORMER") && characterCardMovements>=2)){
            throw new AlreadyUsedCharacterCardException();
        }
    }

    private void checkDefaultMovements(Message message) throws DefaultMovementsNumberException, DefaultMovementsColorException {
        if(defaultMovements>=4
                || (defaultMovements>=3 && gameModel.getPlayers().size()%2==0 ))
            throw new DefaultMovementsNumberException();
        else if(!(gameModel.getBoard().getSchoolByOwner(gameModel.getPlayers().get(playerTurnNumber)
                .getNickname()).hasHallStudentColor(message.getFirstParameter()))) {
            throw new DefaultMovementsColorException();
        }
    }

    private void checkPhase(Message message) throws IncorrectPhaseException {
        if(!message.getAction().equals(phase)) throw new IncorrectPhaseException();
    }
}
