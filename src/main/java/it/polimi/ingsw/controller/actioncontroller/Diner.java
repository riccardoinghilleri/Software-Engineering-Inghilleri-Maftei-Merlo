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
    public void updateProfessor(String color) {
        CharacterColor c = CharacterColor.valueOf(color);
        int owner = getGameModel().getBoard().getProfessorByColor(c).getOwner();
        int max=0;
        if(owner!=-1) {
            max = getGameModel().getBoard().getSchoolByOwnerId(owner).getDiningRoom().get(CharacterColor.valueOf(color)).size();
        }
        for(School s: getGameModel().getBoard().getSchools()){
            if(max<s.getDiningRoom().get(c).size()){
                max=s.getDiningRoom().get(c).size();
                owner=s.getOwnerId();
            }
            else if (max<=s.getDiningRoom().get(c).size() && s.getOwner().equals(getGameModel().getCurrentPlayer().getNickname()))
                owner=getGameModel().getCurrentPlayer().getClientID();
        }
        getGameModel().getBoard().getProfessorByColor(c).setOwner(owner);
    }
}
