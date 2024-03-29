package it.polimi.ingsw.controller.actioncontroller;

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
     *
     * @param gameModel of type GameHandler - GameHandler reference
     */
    public ActionController(GameModel gameModel) {
        this.gameModel = gameModel;
        this.strategy = null;
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
     * If strategy is true , the strategy is used once, and it updates players , board's and chard's coins.
     * If the character card is Diner, it calls the updatesProfessors() method.
     *
     * @param actionMessage parameter which gives all the information about the player's move
     * @param strategy      boolean which tells if the chard is one of those that implement character card strategy
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

    /**
     * This method uses the effect of the character card performer, queen, thief which requires to update the professor eventually after using them.
     * @param actionMessage type of message sent by the client
     */
    public void useCharacterCardEffect(ActionMessage actionMessage) {
        this.strategy.useEffect(actionMessage);
        if (actionMessage.getCharacterCardName().equalsIgnoreCase("PERFORMER")
                || actionMessage.getCharacterCardName().equalsIgnoreCase("QUEEN")
                || actionMessage.getCharacterCardName().equalsIgnoreCase("THIEF")) {
            updateAllProfessors();
        }
    }

    /**
     * This method upon an action message request returns the player with the highest influence
     * on the specified island
     *
     * @param index id of island
     * @return the player with the highest influence
     */
    public int getInfluence(int index) {
        return gameModel.getBoard().getTotalInfluence(index);
    }

    /**
     * This method moves the students from the entrance to the diningroom.
     * After each movement the professor's owner of that color is updated
     *
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
     *
     * @param islandPosition the id of the island in which to move the student
     * @param studentColor   the color of the student to move
     */

    //metodo che muove gli studenti dalla sala di una scuola a una isola
    public void moveStudent(int islandPosition, String studentColor) {

        gameModel.getBoard().moveStudent(playerId, islandPosition, studentColor);

    }

    /**
     * This method adds the students of the chosen cloud to the player's entrance
     *
     * @param cloudPosition the chosen cloud
     */
    //TODO aggiungere ai metodi
    public void moveStudent(int cloudPosition) {
        gameModel.getBoard().moveStudent(cloudPosition, playerId);
    }

    /**
     * This method moves mother nature upon request.
     * It moves automatically the towers
     *
     * @param actionMessage type of message
     * @return the new owner
     */
    //TODO il movimento delle tower è atomico con lo spostamento di madre natura o deve essere il client a farlo cosi possiamo usare una specialCard dopo il movimento di madre natura  e prima d i muovere le torri
    public int moveMotherNature(ActionMessage actionMessage) {
        int newOwner = -1;
        //muovo madre natura
        gameModel.getBoard().moveMotherNature((actionMessage.getData()));
        int index = gameModel.getBoard().getMotherNaturePosition();
        //Se L'isola è bloccata, tolgo il divieto e lo rimetto nella carta senza calcolare l'influenza
        if (gameModel.getBoard().getIslands().get(index).hasNoEntryTile()) {
            BoardExpert boardExpert = (BoardExpert) gameModel.getBoard();
            boardExpert.removeNoEntryTiles(index);
            ((CharacterCardwithProhibitions) boardExpert.getCharacterCardbyName("HERBOLARIA")).restockProhibitionsNumber();
        } else {
            newOwner = getInfluence(index);
            int oldOwner = -1;
            //Se l'isola ha le torri, trovo il vecchio owner
            if (!gameModel.getBoard().getIslands().get(index).getTowers().isEmpty())
                oldOwner = gameModel.getBoard().getIslands().get(index).getTowers().get(0).getOwner();
            //se nessuno controlla non faccio niente
            if (newOwner == -1)
                return -1;
                //se qualcuno controlla l'isola e non è uguale al vecchio owner
            else if (newOwner != oldOwner) {
                //se l'isola non contiene torri sposto le torri dalla scuola all'isola
                if (oldOwner == -1) {
                    gameModel.getBoard().moveTower(newOwner, index, "island");
                }
                //altrimenti tolgo le tower presenti e metto quelle del nuovo owner
                else {
                    int towers_number = gameModel.getBoard().getIslands().get(index).getTowers().size();
                    int available_towers = gameModel.getBoard().getSchoolByOwnerId(newOwner).getTowersNumber();
                    gameModel.getBoard().moveTower(oldOwner, index, "school");
                    for (int i = 0; i < towers_number && i < available_towers; i++)
                        gameModel.getBoard().moveTower(newOwner, index, "island");
                }
                gameModel.getBoard().checkNearIsland(index, false);
            }
        }
        return newOwner;
    }

    /**
     * This method updates the owner's professor , calling the updateProfessor method of the board
     * @param color color of the professor
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
