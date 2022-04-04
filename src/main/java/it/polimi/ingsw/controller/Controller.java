package it.polimi.ingsw.controller;

import it.polimi.ingsw.controller.actioncontroller.*;

import it.polimi.ingsw.exceptions.*;
import it.polimi.ingsw.model.Cloud;
import it.polimi.ingsw.model.GameModel;
import it.polimi.ingsw.model.board.BoardHard;

public class Controller {
    private final GameModel gameModel;
    private ActionController actionController;
    private int playerTurnNumber;
    private int characterCardMovements;
    private int defaultMovements;
    private boolean alreadyUsedCharacterCard;
    private Phase phase;

    public Controller(GameModel gameModel) {
        this.gameModel = gameModel;
        this.phase = Phase.SET_CLOUD;
        playerTurnNumber = 0;
        characterCardMovements = 0;
        defaultMovements = 0;
        alreadyUsedCharacterCard = false;
    }

    public void setCloud() {
        gameModel.getBoard().setClouds();
        phase = Phase.SET_ASSISTANT_CARD;
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
            phase = Phase.ACTION;
            gameModel.setPlayersOrder();
        }
    }

    public void startPlayerTurn() {
        actionController = new ActionController(gameModel,gameModel.getPlayers().get(playerTurnNumber).getNickname());
        characterCardMovements = 0;
        defaultMovements = 0;
        alreadyUsedCharacterCard = false;
    }

    public void nextAction(Message message) {
        switch (message.getAction()) {
            case USE_SPECIAL_CARD:
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
                    checkDefaultMovements(message);
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
            case GET_INFLUENCE: //TODO non so se è necessario
                actionController.getInfluence(message);
            case MOVE_NATURE_MOTHER:
                try { //TODO corrispondenza con una fase
                    checkChosenSteps(message);
                } catch (InvalidChosenStepsException e) {
                    System.out.println(e.getMessage());
                }
                actionController.moveNatureMother(message);
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
                    checkCloud(message);
                } catch (EmptyCloudException e) {
                    System.out.println(e.getMessage());
                }
                actionController.moveStudent(message.getData());
        }
    }

    public void endPlayerTurn() {
        playerTurnNumber++;
        if(playerTurnNumber==gameModel.getPlayersNumber())
        {
            phase= Phase.SET_CLOUD;
        }
    }

    private void setCharacterCardEffect(Message message)
    {
        boolean strategy = false;
        switch (message.getCharacterCardName().toUpperCase()) {
            case "CENTAUR":
                actionController = new Centaur(gameModel, gameModel.getPlayers().get(playerTurnNumber).getNickname());
                break;
            case "DINER":
                actionController = new Diner(gameModel, gameModel.getPlayers().get(playerTurnNumber).getNickname());
                break;
            case "KNIGHT":
                actionController = new Knight(gameModel, gameModel.getPlayers().get(playerTurnNumber).getNickname());
                break;
            case "LUMBERJACK":
                actionController = new Lumberjack(gameModel, gameModel.getPlayers().get(playerTurnNumber).getNickname());
                break;
            default:
                strategy = true; //setta strategia e usa effetto
                break;
        }
        actionController.useSpecialCard(message, strategy);
    }

    private void checkSameAssistantCard(int priority) throws SameAssistantCardException {
        for(int i=0; i<playerTurnNumber; i++) { //TODO sistemare il caso in cui ho più di 1 carta in mano però sono state tutte gia scelte
            if(priority==gameModel.getPlayers().get(i).getChoosenAssistantCard().getPriority()
                    && gameModel.getPlayers().get(playerTurnNumber).getDeck().getAssistantCards().size()!=1) {
                throw new SameAssistantCardException();
            }
        }
    }

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
                || (defaultMovements>=3 && (gameModel.getPlayers().size()==2 || gameModel.getPlayers().size()==4)))
            throw new DefaultMovementsNumberException();
        else if(!(gameModel.getBoard().getSchoolByOwner(gameModel.getPlayers().get(playerTurnNumber)
                .getNickname()).hasHallStudentColor(message.getFirstParameter()))) {
            throw new DefaultMovementsColorException();
        }
    }
}
