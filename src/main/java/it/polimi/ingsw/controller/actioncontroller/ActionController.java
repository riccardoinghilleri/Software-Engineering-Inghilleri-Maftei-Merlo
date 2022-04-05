package it.polimi.ingsw.controller.actioncontroller;

import it.polimi.ingsw.controller.Message;
import it.polimi.ingsw.model.GameModel;

public class ActionController {
    private final GameModel gameModel;
    private String specialCardName;
    private int cardMovements;
    private String player;

    public ActionController(GameModel gameModel) {
        this.gameModel = gameModel;
        this.specialCardName = null;
        this.cardMovements = 0;
        this.player = player;
    }

    public String getSpecialCardName() {
        return specialCardName;
    }

    public int getCardMovements() {
        return cardMovements;
    }

    public void useSpecialCard(Message message, boolean strategy) {

    }

    public String getInfluence(Message message) {
        return "a"; //TODO da fare
    }

    public void moveStudent(String studentColor) {

    }

    public void moveStudent(int islandPosition, String studentColor) {

    }

    public void moveStudent(int cloudPosition) {

    }

    public void moveNatureMother(Message message) {

    }

    public void updateProfessor() {

    }

    public void toggle(int islandPosition) {

    }
}
