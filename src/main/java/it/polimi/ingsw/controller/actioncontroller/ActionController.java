package it.polimi.ingsw.controller.actioncontroller;

import it.polimi.ingsw.enums.Action;
import it.polimi.ingsw.server.ConnectionMessage.ActionMessage;
import it.polimi.ingsw.server.model.GameModel;
import it.polimi.ingsw.server.model.CharacterCardwithProhibitions;
import it.polimi.ingsw.server.model.BoardExpert;
import it.polimi.ingsw.enums.CharacterColor;

import static it.polimi.ingsw.controller.StrategyFactory.strategyFactory;

/**
 * This class manages the actual turn of each player.
 * A new instance of action controller is created by the controller
 * when a player turn starts.
 */
public class ActionController {
    private final GameModel gameModel;
    private final int playerId;
    private CharacterCardStrategy strategy;

    /**
     * The constructor of the class.
     * @param gameModel of type GameHandler - GameHandler reference
     */
    public ActionController(GameModel gameModel) {
        this.gameModel = gameModel;
        //this.specialCardName = null;
        this.playerId = gameModel.getCurrentPlayer().getClientID();
    }

    /**
     * This method returns the gameModel
     */
    public GameModel getGameModel() {
        return gameModel;
    }

    /**
     * This method returns the CharacterCard strategy
     */
    public CharacterCardStrategy getCharacterCardStrategy() {
        return strategy;
    }

    /**
     * If strategy is true , the strategy is used once, and it updates players , board's and chard's coins.
     * If the character card is Diner, it calls the updatesProfessors() method.
     * @param actionMessage parameter which gives all the information about the player's move
     * @param strategy boolean which tells if the chard is one of those that implement character card strategy
     */
    //se strategy è true, setto la strategy e la uso una volta
    //aggiorno le monete dei player, della carta e della board
    //se la carta è Diner, aggiorno tutti i professori
    public void useCharacterCard(ActionMessage actionMessage, boolean strategy) {
        if (strategy) {
            this.strategy = strategyFactory(actionMessage.getCharacterCardName(), gameModel);
        } else if (actionMessage.getCharacterCardName().equalsIgnoreCase("DINER")) {
            updateAllProfessors();
        }
        BoardExpert boardExpert = (BoardExpert) gameModel.getBoard();
        boardExpert.moveCoin(playerId, boardExpert.getCharacterCardbyName(actionMessage.getCharacterCardName()));
    }
    public void useCharacterCardEffect(ActionMessage actionMessage){
        this.strategy.useEffect(actionMessage);
    }

    /**
     * This method upon an action message request returns the player with the highest influence
     * on the specified island by the method getData()
     */
    //metodo che ritorna il player con più influenza sull'isola specificata
    public int getInfluence(ActionMessage actionMessage) {
        return gameModel.getBoard().getTotalInfluence(actionMessage.getData());
    }

    /**
     * This method moves the students from the dining room to the entrance.
     * After each movement the professor's owner of that color is updated
     * @param studentColor the color of the student to move
     */
    public void moveStudent(String studentColor) {
        gameModel.getBoard().getSchoolByOwnerId(playerId).fromEntrancetoDiningRoom(CharacterColor.valueOf(studentColor));
        if (gameModel.isExpertGame() && gameModel.getBoard().getSchoolByOwnerId(playerId).getDiningRoom().get(CharacterColor.valueOf(studentColor)).size() % 3 == 0) {
            BoardExpert boardExpert = (BoardExpert) gameModel.getBoard();
            boardExpert.addCointoPlayer(playerId);
        }
        updateProfessor(studentColor);
    }
    /**
     * This method moves the students from the dining room to the specified island.
     * @param islandPosition  the id of the island in which to move the student
     * @param studentColor the color of the student to move
     */

    public void moveStudent(int islandPosition, String studentColor) {

        gameModel.getBoard().moveStudent(playerId, islandPosition, studentColor);

    }

    /**
     * This method adds the students of the chosen cloud to the player's entrance
     * @param cloudPosition the chosen cloud
     */
    //TODO aggiungere ai metodi
    public void moveStudent(int cloudPosition) {
        gameModel.getBoard().moveStudent(cloudPosition, playerId);
    }

    /**
     * This method moves mother nature.
     * It moves automatically the towers
     * @param actionMessage
     * @return the new owner
     */
    //TODO il movimento delle tower è atomico con lo spostamento di madre natura o deve essere il client a farlo cosi possiamo usare una specialCard dopo il movimento di madre natura  e prima d i muovere le torri
    //TODO tutte le getInfluence() devono ritornare NONE in caso di pareggio
    public int moveMotherNature(ActionMessage actionMessage) {
        int newOwner = -1;
        gameModel.getBoard().moveMotherNature((actionMessage.getData()));
        int index = gameModel.getBoard().getMotherNaturePosition();
        //if the island is locked, the ' no entry tile' is removed e it is put on the character card without getting the influence
        if (gameModel.getBoard().getIslands().get(index).hasNoEntryTile())
        {
            BoardExpert boardExpert = (BoardExpert) gameModel.getBoard();
            boardExpert.removeNoEntryTiles(index);
            ((CharacterCardwithProhibitions) boardExpert.getCharacterCardbyName("HERBOLARIA")).restockProhibitionsNumber();
        } else {
            ActionMessage m = new ActionMessage();
            m.setAction(Action.GET_INFLUENCE);
            m.setData(index);
            newOwner = getInfluence(m);
            int oldOwner;
            if (!gameModel.getBoard().getIslands().get(index).getTowers().isEmpty())
                oldOwner = gameModel.getBoard().getIslands().get(index).getTowers().get(0).getOwner();
            else
                oldOwner = -1;
            //If nobody controls the island no changes are made
            if (newOwner!=-1) {
                // if the island does not contains towers, the towers from the school are move to the island
                if (gameModel.getBoard().getIslands().get(index).getTowers().isEmpty()) {
                    gameModel.getBoard().moveTower(newOwner,index,"island");
                }
                //otherwise the towers are removed and replaced with those of the new owner
                else if (newOwner!=oldOwner) {
                    int towers_number = gameModel.getBoard().getIslands().get(index).getTowers().size();
                    int available_towers = gameModel.getBoard().getSchoolByOwnerId(newOwner).getTowersNumber();
                    gameModel.getBoard().moveTower(oldOwner,index, "school");
                    for (int i = 0; i < towers_number && i < available_towers; i++)
                        gameModel.getBoard().moveTower(newOwner, index,"island");
                }
                gameModel.getBoard().checkNearIsland(actionMessage.getData());
            }
        }
        return newOwner;
    }

    /**
     * This method updates the owner's professor using as a paragon '>', calling the updateProfessor method of the board
     */

    public void updateProfessor(String color) {
        gameModel.getBoard().updateProfessor(CharacterColor.valueOf(color));
    }

    /**
     * Since the Diner character Card has directly an effect on professors, this method updates all
     * the professor after Diner Card being used.
     */

    private void updateAllProfessors() {
        for (CharacterColor c : CharacterColor.values())
            updateProfessor(c.toString());
    }

}
