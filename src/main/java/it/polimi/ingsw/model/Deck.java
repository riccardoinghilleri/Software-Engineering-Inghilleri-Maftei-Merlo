package it.polimi.ingsw.model;

import it.polimi.ingsw.model.enums.Wizard;

import java.util.*;

public class Deck {
    Wizard wizard = null;
    List<AssistantCard> assistantCards;

    public Deck(){
        assistantCards=new ArrayList<>();
        for(int i=1;i<11;i++)
        {
            if(i%2==0)
            assistantCards.add(new AssistantCard(i,i-1));
            else assistantCards.add(new AssistantCard(i,i));
        }
    }

    public Wizard getWizard() {
        return wizard;
    }

    public List<AssistantCard> getAssistantCards() {
        return assistantCards;
    }

    public void setWizard(Wizard wizard) {
        this.wizard = wizard;
    }

    //TODO ECCEZIONE SE LA PRIORITA' NON PUO' ESSERE SCELTA
    public AssistantCard removeAssistantCard(int priority){
        AssistantCard result=null;
        for(int i=0; i< assistantCards.size() && result==null; i++)
            if (assistantCards.get(i).getPriority()==priority) {
                result = assistantCards.get(i);
                assistantCards.remove(i);
            }
        return result;
    }
}
