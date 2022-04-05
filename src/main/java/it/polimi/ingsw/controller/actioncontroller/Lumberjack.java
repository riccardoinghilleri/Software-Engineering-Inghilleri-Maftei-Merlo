package it.polimi.ingsw.controller.actioncontroller;

import it.polimi.ingsw.controller.Message;
import it.polimi.ingsw.model.GameModel;
import it.polimi.ingsw.model.Student;
import it.polimi.ingsw.model.enums.CharacterColor;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Lumberjack extends ActionController {

    String color;
    public Lumberjack(GameModel gameModel, String color) {
        super(gameModel);
        this.color=color;
    }

    @Override
    public String getInfluence(Message message) {
        CharacterColor[] values = CharacterColor.values();
        Arrays.asList(values).remove(CharacterColor.valueOf(color));
        Map<CharacterColor, List<Student>> students = super.getGameModel().getBoard().getIslands().get(message.getData()).getStudents();
        Map<String, Integer> owners = new HashMap<>();
        for (CharacterColor c : values) {
            String owner = super.getGameModel().getBoard().getProfessorByColor(c.toString()).getOwner();
            if (owners.containsKey(owner))
                owners.replace(owner, owners.get(owner) + students.get(c).size());
            else owners.put(owner, students.get(c).size());
            if (owner.equals(super.getGameModel().getBoard().getIslands().get(message.getData()).getTowers().get(0).getOwner()))
            {
                owners.replace(owner, owners.get(owner) + students.get(c).size() + super.getGameModel().getBoard().getIslands().get(message.getData()).getTowers().size());
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
