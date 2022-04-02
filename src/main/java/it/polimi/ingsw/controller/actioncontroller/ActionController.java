package it.polimi.ingsw.controller.actioncontroller;

import it.polimi.ingsw.controller.messages.Message;
import it.polimi.ingsw.model.GameModel;

public class ActionController {
    private final GameModel gameModel;
    private String specialCardName;
    private int cardMovements;

    public ActionController(GameModel gameModel) {
        this.gameModel = gameModel;
        this.specialCardName = null;
        this.cardMovements = 0;
    }

    public String getSpecialCardName() {
        return specialCardName;
    }

    public int getCardMovements() {
        return cardMovements;
    }

    public void useSpecialCard(String specialCardName) {

    }

    public String getInfluence(Message influenceMessage) {
        return "a"; //TODO da fare
    }

    public void moveStudent(String schoolOwner, String studentColor) {

    }

    public void moveStudent(String schoolOwner,int islandPosition, String studentColor) {

    }

    public void specialMovement(Message studentsMessage) {

    }

    public void moveNatureMother(Message message) {

    }

    public void updateProfessor() {

    }

    public void toggle(int islandPosition) {

    }
}
