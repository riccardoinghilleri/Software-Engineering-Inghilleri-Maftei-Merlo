package it.polimi.ingsw.server.model;

public class AssistantCard {
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

    @Override
    public String toString() {
        return "PRIORITY: " + priority + " - MOTHER NATURE STEPS: " + natureMotherSteps;
    }
}
