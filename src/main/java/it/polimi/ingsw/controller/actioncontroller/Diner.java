package it.polimi.ingsw.controller.actioncontroller;

import it.polimi.ingsw.server.model.GameModel;
import it.polimi.ingsw.server.model.School;
import it.polimi.ingsw.enums.CharacterColor;
/**
 * This class represents the Diner card and contains the specific method of the card.
 */
public class Diner extends ActionController {
    /**
     * Constructor: it creates a new instance of Clown
     * @param gameModel of type GameHandler - GameHandler reference.
     */
    public Diner(GameModel gameModel) {
        super(gameModel);
    }

    /**
     * This method is an override of the updateProfessor() method from the action controller.
     * Go to "model->boardExpert" to see the description
     * @param color : color of the student to be considered
     */
    @Override
    public void updateProfessor(String color)
    {
        CharacterColor c = CharacterColor.valueOf(color);
        int oldOwner = getGameModel().getBoard().getProfessorByColor(c).getOwner();
        int newOwner = -1;
        int max = 0;
        if (oldOwner != -1) {
            max = getGameModel().getBoard().getSchoolByOwnerId(oldOwner).getDiningRoom().get(c).size();
        }
        for (School s : getGameModel().getBoard().getSchools()) {
            if (max < s.getDiningRoom().get(c).size()) {
                max = s.getDiningRoom().get(c).size();
                newOwner = s.getOwnerId();
            }
            else if (max<=s.getDiningRoom().get(c).size() && s.getOwner().getClientID()==getGameModel().getCurrentPlayer().getClientID())
                newOwner=getGameModel().getCurrentPlayer().getClientID();
        }
        if (newOwner != -1 && newOwner!=oldOwner) {
            getGameModel().getBoard().getProfessorByColor(c).setOwner(newOwner);
            //Per la grafica:
            if (oldOwner != -1)
                getGameModel().getBoard().getSchoolByOwnerId(newOwner).addProfessor(getGameModel().getBoard().getSchoolByOwnerId(oldOwner).removeProfessor(c));
            else
                getGameModel().getBoard().getSchoolByOwnerId(newOwner).addProfessor(getGameModel().getBoard().getProfessorByColor(c));
        }
    }
}
