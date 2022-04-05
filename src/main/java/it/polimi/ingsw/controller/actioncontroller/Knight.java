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

    @Override
    public String getInfluence(Message influenceMessage) {
        InfluenceMessage m = (InfluenceMessage) influenceMessage;
        Map<CharacterColor, List<Student>> students = gameModel.getBoard().getIslands().get(m.getIslandPosition()).getStudents();
        Map<String, Integer> owners = new HashMap<>();
        for (CharacterColor c : CharacterColor.values()) {
            String owner = gameModel.getBoard().getProfessorByColor(c.toString()).getOwner();
            if (owners.containsKey(owner))
                owners.replace(owner, owners.get(owner) + students.get(owner).size());
            else owners.put(owner, students.get(owner).size());
            if (owner.equals(gameModel.getBoard().getIslands().get((m.getIslandPosition())).getTowers().get(0).getOwner()))
            {
                owners.replace(owner, owners.get(owner) + students.get(owner).size() + gameModel.getBoard().getIslands().get((m.getIslandPosition())).getTowers().size());
            }
            if(owner.equals(player))
            {
                owners.replace(owner, owners.get(owner) +2);
            }

        }


        String result = owners.entrySet().stream().max((entry1, entry2) -> entry1.getValue() > entry2.getValue() ? 1 : -1).get().getKey();

        return result;


    }
}
