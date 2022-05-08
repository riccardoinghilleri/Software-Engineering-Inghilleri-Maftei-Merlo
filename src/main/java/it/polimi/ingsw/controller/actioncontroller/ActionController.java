package it.polimi.ingsw.controller.actioncontroller;

import it.polimi.ingsw.enums.Action;
import it.polimi.ingsw.server.ConnectionMessage.ActionMessage;
import it.polimi.ingsw.server.model.GameModel;
import it.polimi.ingsw.server.model.CharacterCardwithProhibitions;
import it.polimi.ingsw.server.model.BoardExpert;
import it.polimi.ingsw.enums.CharacterColor;

import static it.polimi.ingsw.controller.StrategyFactory.strategyFactory;

public class ActionController {
    private final GameModel gameModel;
    private final int playerId;
    private CharacterCardStrategy strategy;

    public ActionController(GameModel gameModel) {
        this.gameModel = gameModel;
        //this.specialCardName = null;
        this.playerId = gameModel.getCurrentPlayer().getClientID();
    }

    public GameModel getGameModel() {
        return gameModel;
    }

    public CharacterCardStrategy getCharacterCardStrategy() {
        return strategy;
    }


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

    //metodo che ritorna il player con più influenza sull'isola specificata
    public int getInfluence(ActionMessage actionMessage) {
        return gameModel.getBoard().getTotalInfluence(actionMessage.getData());
    }

    //metodo che muove gli studenti dalla sala all'ingresso
    //dopo il movimento viene aggiornato il professore di quel colore
    public void moveStudent(String studentColor) {
        gameModel.getBoard().getSchoolByOwnerId(playerId).fromEntrancetoDiningRoom(CharacterColor.valueOf(studentColor));
        if (gameModel.isExpertGame() && gameModel.getBoard().getSchoolByOwnerId(playerId).getDiningRoom().get(CharacterColor.valueOf(studentColor)).size() % 3 == 0) {
            BoardExpert boardExpert = (BoardExpert) gameModel.getBoard();
            boardExpert.addCointoPlayer(playerId);
        }
        updateProfessor(studentColor);
    }

    //metodo che muove gli studenti dalla sala di una scuola ad una isola
    public void moveStudent(int islandPosition, String studentColor) {

        gameModel.getBoard().moveStudent(playerId, islandPosition, studentColor);

    }

    //TODO aggiungere ai metodi
    public void moveStudent(int cloudPosition) {
        gameModel.getBoard().moveStudent(cloudPosition, playerId);
    }

    //metodo che muove madre natura
    //sposta le tower automaticamente
    //TODO il movimento delle tower è atomico con lo spostamento di madre natura o deve essere il client a farlo cosi possiamo usare una specialCard dopo il movimento di madre natura  e prima d i muovere le torri
    //TODO tutte le getInfluence() devono ritornare NONE in caso di pareggio
    public int moveMotherNature(ActionMessage actionMessage) {
        int newOwner = -1;
        gameModel.getBoard().moveMotherNature((actionMessage.getData()));
        int index = gameModel.getBoard().getMotherNaturePosition();
        if (gameModel.getBoard().getIslands().get(index).hasNoEntryTile()) //Se L'isola è bloccata, tolgo il divieto e lo rimetto nella carta senza calcolare l'influenza
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
            //se nessuno controlla isola non faccio cambiamenti
            if (newOwner!=-1) {
                //se l'isola non contiene torri sposto le torri dalla scuola all'isola
                if (gameModel.getBoard().getIslands().get(index).getTowers().isEmpty()) {
                    gameModel.getBoard().moveTower(newOwner,index,"island");
                }
                //altrimenti tolgo le tower presenti e metto quelle del nuovo owner
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

    // metodo standard che aggiorna l'owner di un professore utilizzando come comparatore >
    public void updateProfessor(String color) {
        gameModel.getBoard().updateProfessor(CharacterColor.valueOf(color));
    }

    //metodo che aggiorna tutti i professori quando viene utilizzata la carta Diner
    private void updateAllProfessors() {
        for (CharacterColor c : CharacterColor.values())
            updateProfessor(c.toString());
    }

}
