package it.polimi.ingsw.controller.actioncontroller;

import it.polimi.ingsw.controller.Action;
import it.polimi.ingsw.server.ConnectionMessage.ActionMessage;
import it.polimi.ingsw.server.model.GameModel;
import it.polimi.ingsw.server.model.CharacterCardwithProhibitions;
import it.polimi.ingsw.server.model.BoardExpert;
import it.polimi.ingsw.server.model.enums.CharacterColor;

import static it.polimi.ingsw.controller.StrategyFactory.strategyFactory;

public class ActionController {
    private final GameModel gameModel;
    private String player;
    private CharacterCardStrategy strategy;

    public ActionController(GameModel gameModel) {
        this.gameModel = gameModel;
        //this.specialCardName = null;
        this.player=gameModel.getCurrentPlayer().getNickname();
    }

    public GameModel getGameModel() {
        return gameModel;
    }

    public CharacterCardStrategy getCharacterCardStrategy(){
        return strategy;
    }


    //se strategy è true, setto la strategy e la uso una volta
    //aggiorno le monete dei player, della carta e della board
    //se la carta è Diner, aggiorno tutti i professori
    public void useCharacterCard(ActionMessage actionMessage, boolean strategy) {
        if(strategy){
            this.strategy=strategyFactory(actionMessage.getCharacterCardName(),gameModel);
            this.strategy.useEffect(actionMessage);
        }
        else if(actionMessage.getCharacterCardName().equalsIgnoreCase("DINER")){
            updateAllProfessors();
        }
        BoardExpert boardExpert=(BoardExpert)gameModel.getBoard();
        boardExpert.moveCoin(player,boardExpert.getCharacterCardbyName(actionMessage.getCharacterCardName()));
    }

    //metodo che ritorna il player con più influenza sull'isola specificata
    public String getInfluence(ActionMessage actionMessage) {
        return gameModel.getBoard().getTotalInfluence(actionMessage.getData());
    }

    //metodo che muove gli studenti dalla sala all'ingresso
    //dopo il movimento viene aggiornato il professore di quel colore
    public void moveStudent(String studentColor) {
        gameModel.getBoard().getSchoolByOwner(player).fromEntrancetoDiningRoom(CharacterColor.valueOf(studentColor));
        if(gameModel.isExpertGame() && gameModel.getBoard().getSchoolByOwner(player).getDiningRoom().get(CharacterColor.valueOf(studentColor)).size()%3==0){
            BoardExpert boardExpert=(BoardExpert) gameModel.getBoard();
            boardExpert.addCointoPlayer(player);
        }
        updateProfessor(studentColor);
    }

    //metodo che muove gli studenti dalla sala di una scuola ad una isola
    public void moveStudent(int islandPosition, String studentColor) {

        gameModel.getBoard().moveStudent(player,islandPosition,studentColor);

    }

    //TODO aggiungere ai metodi
    public void moveStudent(int cloudPosition){
        gameModel.getBoard().moveStudent(cloudPosition,player);
    }

    //metodo che muove madre natura
    //sposta le tower automaticamente
    //TODO il movimento delle tower è atomico con lo spotamento di madre natura o deve essere il client a farlo cosi possiamo usare una specialCard dopo il movimento di madre natura  e prima d i muovere le torri
    //TODO tutte le getInfluence() devono ritornare NONE in caso di pareggio
    public String moveMotherNature(ActionMessage actionMessage) {
        String newOwner="NONE";
        gameModel.getBoard().moveMotherNature((actionMessage.getData()));
        int index = gameModel.getBoard().getMotherNaturePosition();
        if(gameModel.getBoard().getIslands().get(index).hasNoEntryTile()) //Se L'isola è bloccata, tolgo il divieto e lo rimetto nella carta senza calcolare l'influenza
        {
            BoardExpert boardExpert= (BoardExpert) gameModel.getBoard();
            boardExpert.removeNoEntryTiles(index);
            ((CharacterCardwithProhibitions) boardExpert.getCharacterCardbyName("HERBOLARIA")).restockProhibitionsNumber();
        }
        else{
            ActionMessage m = new ActionMessage();
            m.setAction(Action.GET_INFLUENCE);
            m.setData(index);
            newOwner= getInfluence(m);
            String oldOwner;
            if(!gameModel.getBoard().getIslands().get(index).getTowers().isEmpty())
                oldOwner = gameModel.getBoard().getIslands().get(index).getTowers().get(0).getOwner();
            else
                oldOwner = "NONE";
            //se nessuno controlla isola non faccio cambiamenti
            if (!newOwner.equalsIgnoreCase("NONE")) {
                //se l'isola non contiene torri sposto le torri dalla scuola all'isola
                if (gameModel.getBoard().getIslands().get(index).getTowers().isEmpty()) {
                    gameModel.getBoard().moveTower(gameModel.getPlayerByNickname(newOwner).getNickname(), index);
                }
                //altrimenti tolgo le tower presenti e metto quelle del nuovo owner
                else if(!newOwner.equalsIgnoreCase(oldOwner)) {
                    int towers_number = gameModel.getBoard().getIslands().get(index).getTowers().size();
                    gameModel.getBoard().moveTower(index, oldOwner);
                    for (int i = 0; i < towers_number; i++)
                        gameModel.getBoard().moveTower(newOwner, index);
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
        for(CharacterColor c: CharacterColor.values())
        updateProfessor(c.toString());
    }

}
