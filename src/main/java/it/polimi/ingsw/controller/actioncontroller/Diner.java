package it.polimi.ingsw.controller.actioncontroller;

import model.GameModel;
import model.School;
import model.enums.CharacterColor;

public class Diner extends ActionController {

    public Diner(GameModel gameModel) {
        super(gameModel);
    }

    @Override
    public void updateProfessor(String color) {
        CharacterColor c = CharacterColor.valueOf(color);
        String owner = getGameModel().getBoard().getProfessorByColor(color).getOwner();
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
        getGameModel().getBoard().getProfessorByColor(color).setOwner(owner);
    }
}
