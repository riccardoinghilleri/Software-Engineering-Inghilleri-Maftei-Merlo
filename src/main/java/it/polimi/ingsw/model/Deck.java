package it.polimi.ingsw.model;

import it.polimi.ingsw.model.enums.Magician;

import java.util.*;

public class Deck {
    Magician magician;
    List<AssistantCard> assistantCards;

    public Deck(){
        assistantCards=new ArrayList<>();
        for(int i=1;i<13;i++)
        {
            if(i%2==0)
            assistantCards.add(new AssistantCard(i,i-1));
            else assistantCards.add(new AssistantCard(i,i));
        }
    }

    public Magician getMagician() {
        return magician;
    }

    public List<AssistantCard> getAssistantCards() {
        return assistantCards;
    }

    public void setMagician(Magician magician) {
        this.magician = magician;
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
