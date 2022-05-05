package it.polimi.ingsw.server.model;
import java.io.Serializable;

public class AssistantCard implements Serializable {
    int priority;
    int natureMotherSteps;
     public AssistantCard( int priority,int natureMotherSteps){
         this.priority=priority;
         this.natureMotherSteps= natureMotherSteps;
     }

    public int getPriority() {
        return priority;
    }

    public int getMotherNatureSteps() {
        return natureMotherSteps;
    }

    public StringBuilder draw(){
        StringBuilder card = new StringBuilder();
        String top_wall = "╒════════╕";
        String bottom_wall="\n╘════════╛";
        String vertical = "│";
        card.append(top_wall);
        String line="\n"+ vertical+"PRIORITY"+vertical;
        card.append(line);
        if(priority==10){
            line="\n"+vertical+"   "+priority+"   "+vertical;
        }else line="\n"+vertical+"   "+priority+"    "+vertical;
        card.append(line);
        line="\n"+ vertical+" STEPS: "+vertical;
        card.append(line);
        line="\n"+vertical+"   "+natureMotherSteps+"    "+vertical;
        card.append(line);
        card.append(bottom_wall);
        return card;
    }
    @Override
    public String toString() {
        return "PRIORITY: " + priority + " - MOTHER NATURE STEPS: " + natureMotherSteps;
    }
}
