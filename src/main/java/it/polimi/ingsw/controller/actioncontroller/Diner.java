package it.polimi.ingsw.controller.actioncontroller;

import it.polimi.ingsw.model.GameModel;
import it.polimi.ingsw.model.School;
import it.polimi.ingsw.model.enums.CharacterColor;

public class Diner extends ActionController {

    public Diner(GameModel gameModel) {
        super(gameModel);
    }

    @Override
    public void updateProfessor(String color) {
        CharacterColor c = CharacterColor.valueOf(color);
        String owner = null;
        int max=0;
        GameModel gameModel = super.getGameModel();
        for(School s: gameModel.getBoard().getSchools()){
            if(max<s.getClassroom().get(c).size()){
                max=s.getClassroom().get(c).size();
                owner=s.getOwner();
            }
            else if (max<=s.getClassroom().get(c).size() && s.getOwner().equals(gameModel.getCurrentPlayer().getNickname()))
                owner=gameModel.getCurrentPlayer().getNickname();
        }
        gameModel.getBoard().getProfessorByColor(color).setOwner(owner);

    }
}
