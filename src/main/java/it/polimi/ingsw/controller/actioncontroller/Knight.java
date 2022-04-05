package it.polimi.ingsw.controller.actioncontroller;

import it.polimi.ingsw.controller.Message;
import it.polimi.ingsw.model.GameModel;
import it.polimi.ingsw.model.Student;
import it.polimi.ingsw.model.enums.CharacterColor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Knight extends ActionController {

    public Knight(GameModel gameModel) {
        super(gameModel);
        //TODO da finire
    }
    //metodo che calcola l'influenza aggiungenddo due punti addizionali al currentPlayer
    @Override
    public String getInfluence(Message message) {
        Map<CharacterColor, List<Student>> students = super.getGameModel().getBoard().getIslands().get(message.getData()).getStudents();
        Map<String, Integer> owners = new HashMap<>();
        for (CharacterColor c : CharacterColor.values()) {
            String owner = super.getGameModel().getBoard().getProfessorByColor(c.toString()).getOwner();
            if (owners.containsKey(owner))
                owners.replace(owner, owners.get(owner) + students.get(c).size());
            else owners.put(owner, students.get(c).size());
            if (owner.equals(super.getGameModel().getBoard().getIslands().get(message.getData()).getTowers().get(0).getOwner()))
            {
                owners.replace(owner, owners.get(owner) + students.get(c).size() + super.getGameModel().getBoard().getIslands().get(message.getData()).getTowers().size());
            }
            if(owner.equals(super.getGameModel().getCurrentPlayer().getNickname()))
            {
                owners.replace(owner, owners.get(owner) +2);
            }

        }

        int max=0;
        String result="NONE";
        for(String s : owners.keySet())
        {
            if(owners.get(s)>max) {
                max = owners.get(s);
                result = s;
            }
            else if(owners.get(s)==max)
                result="NONE";
        }
        return result;


    }
}
