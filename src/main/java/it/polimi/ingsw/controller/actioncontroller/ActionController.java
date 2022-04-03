package it.polimi.ingsw.controller.actioncontroller;

import it.polimi.ingsw.controller.messages.InfluenceMessage;
import it.polimi.ingsw.controller.messages.Message;
import it.polimi.ingsw.controller.messages.NatureMotherMessage;
import it.polimi.ingsw.model.GameModel;
import it.polimi.ingsw.model.board.Board;
import it.polimi.ingsw.model.board.BoardHard;
import it.polimi.ingsw.model.enums.CharacterColor;

public class ActionController {
    private final GameModel gameModel;
    private String specialCardName;
    private int cardMovements;
    private String player;

    public ActionController(GameModel gameModel, String player) {
        this.gameModel = gameModel;
        this.specialCardName = null;
        this.cardMovements = 0;
        this.player=player;
    }

    public String getSpecialCardName() {
        return specialCardName;
    }

    public int getCardMovements() {
        return cardMovements;
    }

    //assegno all'attributo dell'actionController il valore passato
    //aggiorno le monete dei player , della carta e della board
    //se la carta è Diner, aggiorno tutti i professori
    public void useSpecialCard(String specialCardName) {
        this.specialCardName=specialCardName;
        BoardHard boardHard=(BoardHard)gameModel.getBoard();
        boardHard.moveCoin(player),boardHard.getSpecialCardbyName(specialCardName);;
        if(specialCardName.equals("Diner"))
            updateAllProfessors();
    }

    //metodo che ritorna il player con più influenza sull'isola specificata
    public String getInfluence(Message influenceMessage) {
        int index=((InfluenceMessage)influenceMessage).getIslandPosition();
        return gameModel.getBoard().getInfluence(index);


    }

    //metodo che muove gli studenti dalla sala all'ingresso
    //dopo il movimento viene aggiornato il professore di quel colore
    public void moveStudent(String studentColor) {
        gameModel.getBoard().getSchoolByOwner(player).fromHalltoClassroom(CharacterColor.valueOf(studentColor));
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

    public void specialMovement(Message studentsMessage) {
        cardMovements++;

    }

    //metodo che muove madre natura
    //sposta le tower automaticamente
    //TODO il movimento delle tower è atomico con lo spotamento di madre natura o deve essere il client a farlo cosi possiamo usare una specialCard dopo il movimento di madre natura  e prima d i muovere le torri
    public void moveNatureMother(Message message) {
        gameModel.getBoard().moveNatureMother((((NatureMotherMessage) message).getChoosenSteps()));
        InfluenceMessage m = new InfluenceMessage();
        int index = gameModel.getBoard().getNatureMotherPosition();
        m.setIslandPosition(index);
        String newOwner = getInfluence(m);
        //se nessuno controlla isola non faccio cambiamenti
        if (newOwner != "NONE") {
            //se l'isola non contiene torri sposto le torri dalla scuola all'isola
            if (gameModel.getBoard().getIslands().get(index).getTowers().isEmpty())
                gameModel.getBoard().moveTower(gameModel.getPlayerByNickname(player).getNickname(), index);
            //altrimenti tolgo le tower presenti e metto quelle del nuovo owner
            else {
                String oldOwner = gameModel.getBoard().getIslands().get(index).getTowers().get(0).getOwner();
                gameModel.getBoard().moveTower(index, oldOwner);
                gameModel.getBoard().moveTower(newOwner, index);
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

    public void toggle(int islandPosition) {
        BoardHard boardHard=(BoardHard)gameModel.getBoard();
        boardHard.removeLock(islandPosition);
    }

}
