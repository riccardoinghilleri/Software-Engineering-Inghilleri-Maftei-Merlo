package it.polimi.ingsw.controller.actioncontroller;

import it.polimi.ingsw.controller.Message;
import it.polimi.ingsw.model.GameModel;
import it.polimi.ingsw.model.Student;
import it.polimi.ingsw.model.enums.CharacterColor;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Lumberjack extends ActionController {

    public Lumberjack(GameModel gameModel, String color) {
        super(gameModel);
        //TODO da finire
    }

    @Override
    public String getInfluence(Message message) {
        CharacterColor[] values = CharacterColor.values();
        Arrays.asList(values).remove(CharacterColor.valueOf(message.getFirstParameter()));
        Map<CharacterColor, List<Student>> students = super.getGameModel().getBoard().getIslands().get(message.getData()).getStudents();
        Map<String, Integer> owners = new HashMap<>();
        for (CharacterColor c : values) {
            String owner = super.getGameModel().getBoard().getProfessorByColor(c.toString()).getOwner();
            if (owners.containsKey(owner))
                owners.replace(owner, owners.get(owner) + students.get(owner).size());
            else owners.put(owner, students.get(owner).size());
            if (owner.equals(super.getGameModel().getBoard().getIslands().get(message.getData()).getTowers().get(0).getOwner()))
            {
                owners.replace(owner, owners.get(owner) + students.get(owner).size() + super.getGameModel().getBoard().getIslands().get(message.getData()).getTowers().size());
            }


        }


        String result = owners.entrySet().stream().max((entry1, entry2) -> entry1.getValue() > entry2.getValue() ? 1 : -1).get().getKey();

        return result;




    }
}
