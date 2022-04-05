package it.polimi.ingsw.controller.actioncontroller;

import it.polimi.ingsw.controller.Action;
import it.polimi.ingsw.controller.Message;
import it.polimi.ingsw.model.GameModel;
import it.polimi.ingsw.model.SpecialCardwithProhibitions;
import it.polimi.ingsw.model.board.BoardHard;
import it.polimi.ingsw.model.enums.CharacterColor;

public class ActionController {
    private final GameModel gameModel;
    private String specialCardName;
    private String player;
    private CharacterCardStrategy strategy;

    public ActionController(GameModel gameModel) {
        this.gameModel = gameModel;
        this.specialCardName = null;
        this.player=gameModel.getCurrentPlayer().getNickname();
    }

    public CharacterCardStrategy getStrategy(){
        return strategy;
    }
    public String getSpecialCardName() {
        return specialCardName;
    }

    //se strategy è true, setto la strategy e la uso una volta
    //aggiorno le monete dei player , della carta e della board
    //se la carta è Diner, aggiorno tutti i professori
    public void useSpecialCard(Message message,boolean strategy) {
        if(strategy){
            this.strategy=strategyFactory(message.getCharacterCardName(),gameModel);
            this.strategy.useEffect(message);
        }
        else if(message.getCharacterCardName().equalsIgnoreCase("DINER")){
            updateAllProfessors();
        }
        BoardHard boardHard=(BoardHard)gameModel.getBoard();
        boardHard.moveCoin(player,boardHard.getSpecialCardbyName(message.getCharacterCardName()));

    }

    //metodo che ritorna il player con più influenza sull'isola specificata
    public String getInfluence(Message message) {
        int index=message.getData();
        return gameModel.getBoard().getInfluence(index);
    }

    //metodo che muove gli studenti dalla sala all'ingresso
    //dopo il movimento viene aggiornato il professore di quel colore
    public void moveStudent(String studentColor) {
        gameModel.getBoard().getSchoolByOwner(player).fromHalltoClassroom(CharacterColor.valueOf(studentColor));
        if(gameModel.isHardcore() && gameModel.getBoard().getSchoolByOwner(player).getClassroom().get(CharacterColor.valueOf(studentColor)).size()%3==0){
            BoardHard boardHard=(BoardHard) gameModel.getBoard();
            boardHard.moveCoin(player);
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
    public void moveNatureMother(Message message) {
        gameModel.getBoard().moveNatureMother((message.getData()));
        int index = gameModel.getBoard().getNatureMotherPosition();
        if(gameModel.getBoard().getIslands().get(index).isLocked()) //Se L'isola è bloccata, tolgo il divieto e lo rimetto nella carta senza calcolare l'influenza
        {
            BoardHard boardHard= (BoardHard) gameModel.getBoard();
            boardHard.removeLock(index);
            ((SpecialCardwithProhibitions) boardHard.getSpecialCardbyName("HERBOLARIA")).restockProhibitionsNumber();
        }
        else{
            Message m = new Message(Action.GET_INFLUENCE);
            m.setData(index);
            String newOwner = getInfluence(m);
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
                else if(!newOwner.equalsIgnoreCase(oldOwner)){
                    gameModel.getBoard().moveTower(index, oldOwner);
                    gameModel.getBoard().moveTower(newOwner, index);
                }
            }
        }

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
