package it.polimi.ingsw.controller;

import it.polimi.ingsw.controller.actioncontroller.ActionController;
import it.polimi.ingsw.controller.messages.*;
import it.polimi.ingsw.exceptions.EmptyCloudException;
import it.polimi.ingsw.exceptions.NotEnoughCoinsException;
import it.polimi.ingsw.exceptions.SameAssistantCardException;
import it.polimi.ingsw.model.Cloud;
import it.polimi.ingsw.model.GameModel;
import it.polimi.ingsw.model.SpecialCard;
import it.polimi.ingsw.model.SpecialCardwithProhibitions;
import it.polimi.ingsw.model.board.BoardHard;

public class Controller {
    private final GameModel gameModel;
    private int playerTurnNumber;
    private ActionControllerFactory actionControllerFactory;
    private Phase phase;
    private ActionController actionController;

    public Controller(GameModel gameModel) {
        this.gameModel = gameModel;
        this.phase = Phase.SET_CLOUD;
        playerTurnNumber = 0;
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
                checkSameAssistantCard(((AssistantCardMessage)message).getPriority());
            } catch (SameAssistantCardException e) {
                System.out.println(e.getMessage());
            }
        }
        gameModel.getPlayers().get(playerTurnNumber).setAssistantCard(((AssistantCardMessage)message).getPriority());
        playerTurnNumber++;
        if(playerTurnNumber==gameModel.getPlayersNumber()) {
            playerTurnNumber = 0;
            phase = Phase.ACTION;
            gameModel.setPlayersOrder();
        }
    }

    public void startTurn() {
        actionController = new ActionController(gameModel,gameModel.getPlayers().get(playerTurnNumber).getNickname());
    }

    public void nextAction(Message message) {
        switch (message.getAction()) {
            case USE_SPECIAL_CARD:
                try {
                    checkCoins((SpecialCardMessage)message);
                } catch (NotEnoughCoinsException e) {
                    System.out.println(e.getMessage());
                }
                actionController.useSpecialCard(((SpecialCardMessage)message).getSpecialCardName());
            case SPECIAL_MOVEMENTS:
            case DEFAULT_MOVEMENTS:
            case GET_INFLUENCE:
                if(gameModel.isHardcore()) {
                    int islandPosition = ((InfluenceMessage) message).getIslandPosition();
                    BoardHard boardHard = (BoardHard) gameModel.getBoard();
                    if (gameModel.getBoard().getIslands().get(islandPosition).isLocked()) {
                        boardHard.removeLock(islandPosition);
                        ((SpecialCardwithProhibitions) (boardHard.getSpecialCardbyName("Herbolaria"))).restockProhibitionsNumber();
                        //TODO va bene che se l'isola è bloccata, il controller la sblocca e non ritorna nulla?
                    }
                    else actionController.getInfluence(message);
                }
                else actionController.getInfluence(message);
            case MOVE_NATURE_MOTHER:
            case CHOOSE_CLOUD:
                try {
                    checkCloud(message);
                } catch (EmptyCloudException e) {
                    System.out.println(e.getMessage());
                }
                actionController.moveStudent(((CloudMessage)message).getCloudPosition());
        }
    }

    public void endPlayerTurn() {
        playerTurnNumber++;

    }

    public void endTurn() {

    }

    private void checkSameAssistantCard(int priority) throws SameAssistantCardException {
        for(int i=0; i<playerTurnNumber; i++) {
            if(priority==gameModel.getPlayers().get(i).getChoosenAssistantCard().getPriority()
                    && gameModel.getPlayers().get(playerTurnNumber).getDeck().getAssistantCards().size()!=1) {
                throw new SameAssistantCardException();
            }
        }
    }

    private void checkCoins(SpecialCardMessage message) throws NotEnoughCoinsException {
        BoardHard boardhard = (BoardHard) gameModel.getBoard();
        int cost = boardhard.getSpecialCardbyName(message.getSpecialCardName()).getCost();
        if(boardhard.getPlayerCoins(gameModel.getPlayers().get(playerTurnNumber).getNickname())<cost)
        {
            throw new NotEnoughCoinsException();
        }
    }

    private void checkCloud(Message message) throws EmptyCloudException {
        //TODO conviene usare un'array di cloud? Meglio una lista??
        Cloud[] cloud = gameModel.getBoard().getClouds() ;
        if(cloud[((CloudMessage)message).getCloudPosition()].getStudents().isEmpty())
        {
            throw new EmptyCloudException();
        }
    }
}
