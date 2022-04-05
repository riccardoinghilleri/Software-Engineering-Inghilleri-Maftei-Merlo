package it.polimi.ingsw.controller.actioncontroller;

import it.polimi.ingsw.controller.Message;
import it.polimi.ingsw.model.GameModel;
import it.polimi.ingsw.model.Student;
import it.polimi.ingsw.model.enums.CharacterColor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Centaur extends ActionController {

    public Centaur(GameModel gameModel) {
        super(gameModel);
        //TODO da finire
    }

    @Override
    public String getInfluence(Message message) { //da aggiungere un getModel in actionController

        Map<CharacterColor, List<Student>> students= super.getGameModel().getBoard().getIslands().get(message.getData()).getStudents();
        Map<String,Integer> owners = new HashMap<>();
        for (CharacterColor c: CharacterColor.values()){

            String owner=super.getGameModel().getBoard().getProfessorByColor(c.toString()).getOwner();
            if(owners.containsKey(owner))
                owners.replace(owner,owners.get(owner)+students.get(c).size());
            else owners.put(owner,students.get(c).size());
        }

        return owners.entrySet().stream().max((entry1, entry2) -> entry1.getValue()>entry2.getValue() ? 1 : -1).get().getKey();
    }
}