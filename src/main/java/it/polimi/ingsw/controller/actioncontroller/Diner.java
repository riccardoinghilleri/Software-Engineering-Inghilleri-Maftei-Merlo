package it.polimi.ingsw.controller.actioncontroller;

import it.polimi.ingsw.server.model.GameModel;
import it.polimi.ingsw.server.model.School;
import it.polimi.ingsw.server.model.enums.CharacterColor;

public class Diner extends ActionController {

    public Diner(GameModel gameModel) {
        super(gameModel);
    }

    @Override
    public void updateProfessor(String color) {
        CharacterColor c = CharacterColor.valueOf(color);
        String owner = getGameModel().getBoard().getProfessorByColor(c).getOwner();
        int max=0;
        if(!owner.equalsIgnoreCase("NONE")) {
            max = getGameModel().getBoard().getSchoolByOwner(owner).getDiningRoom().get(CharacterColor.valueOf(color)).size();
        }
        for(School s: getGameModel().getBoard().getSchools()){
            if(max<s.getDiningRoom().get(c).size()){
                max=s.getDiningRoom().get(c).size();
                owner=s.getOwner();
            }
            else if (max<=s.getDiningRoom().get(c).size() && s.getOwner().equals(getGameModel().getCurrentPlayer().getNickname()))
                owner=getGameModel().getCurrentPlayer().getNickname();
        }
        getGameModel().getBoard().getProfessorByColor(c).setOwner(owner);
    }
}
