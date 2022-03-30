package it.polimi.ingsw.model;

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

    public int getNatureMotherSteps() {
        return natureMotherSteps;
    }
}
