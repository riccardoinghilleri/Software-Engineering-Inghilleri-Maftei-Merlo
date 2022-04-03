package it.polimi.ingsw.controller.actioncontroller;

import it.polimi.ingsw.controller.messages.InfluenceMessage;
import it.polimi.ingsw.controller.messages.Message;
import it.polimi.ingsw.controller.messages.NatureMotherMessage;
import it.polimi.ingsw.model.GameModel;
import it.polimi.ingsw.model.Student;
import it.polimi.ingsw.model.enums.CharacterColor;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Lumberjack extends ActionController {

    public Lumberjack(GameModel gameModel, String player) {
        super(gameModel, player);

    }

    @Override
    public String getInfluence(Message influenceMessage) {
        InfluenceMessage m =(InfluenceMessage) influenceMessage;
        CharacterColor[] values= CharacterColor.values();
        Arrays.stream(values).toList().remove(CharacterColor.valueOf(m.getData()));
        Map<CharacterColor, List<Student>> students = gameModel.getBoard().getIslands().get(m.getIslandPosition()).getStudents();
        Map<String, Integer> owners = new HashMap<>();
        for (CharacterColor c : values) {
            String owner = gameModel.getBoard().getProfessorByColor(c.toString()).getOwner();
            if (owners.containsKey(owner))
                owners.replace(owner, owners.get(owner) + students.get(owner).size());
            else owners.put(owner, students.get(owner).size());
            if (owner.equals(gameModel.getBoard().getIslands().get((m.getIslandPosition())).getTowers().get(0).getOwner()))
            {
                owners.replace(owner, owners.get(owner) + students.get(owner).size() + gameModel.getBoard().getIslands().get((m.getIslandPosition())).getTowers().size());
            }


        }


        String result = owners.entrySet().stream().max((entry1, entry2) -> entry1.getValue() > entry2.getValue() ? 1 : -1).get().getKey();

        return result;




    }
}
