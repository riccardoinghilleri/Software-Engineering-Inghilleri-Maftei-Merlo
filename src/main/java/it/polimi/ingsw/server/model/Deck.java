package it.polimi.ingsw.server.model;

import it.polimi.ingsw.enums.Wizard;

import java.util.*;

public class Deck {
    Wizard wizard = null;
    List<AssistantCard> assistantCards;

    public Deck() {
        assistantCards = new ArrayList<>();
        int steps =1;
        for (int i = 1; i < 11; i++) {
            assistantCards.add(new AssistantCard(i, steps));
            if (i % 2 == 0)
                steps++;
        }
    }

    public Wizard getWizard() {
        return wizard;
    }

    public List<AssistantCard> getAssistantCards() {
        return assistantCards;
    }

    public void setWizard(String wizard) {
        this.wizard = Wizard.valueOf(wizard.toUpperCase());
    }

    //TODO ECCEZIONE SE LA PRIORITA' NON PUO' ESSERE SCELTA
    public AssistantCard removeAssistantCard(int priority) {
        AssistantCard result = null;
        for (int i = 0; i < assistantCards.size() && result == null; i++)
            if (assistantCards.get(i).getPriority() == priority) {
                result = assistantCards.get(i);
                assistantCards.remove(i);
            }
        return result;
    }
}
