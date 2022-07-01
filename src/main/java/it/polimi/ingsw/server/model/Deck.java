package it.polimi.ingsw.server.model;

import it.polimi.ingsw.enums.Wizard;

import java.io.Serializable;
import java.util.*;

/**
 * This class represents the deck associated to a player.
 * Each deck has a magician, which is chosen by the player.
 * After a student has played a specified Assistant card, the card will be removed from the deck,
 * so the number of cards available will decrease.
 */

public class Deck implements Serializable {
    Wizard wizard = null;
    List<AssistantCard> assistantCards;

    /**
     * Constructor Deck creates a new Deck instance.
     * The AssistantCard array is initialized with 10 different AssistantCard with increasing priority.
     */
    public Deck() {
        assistantCards = new ArrayList<>();
        int steps =1;
        for (int i = 1; i < 11; i++) {
            assistantCards.add(new AssistantCard(i, steps));
            if (i % 2 == 0)
                steps++;
        }
    }

    /**
     * @return the Wizard associated to the deck.
     */
    public Wizard getWizard() {
        return wizard;
    }

    /**
     *  @return the list of assistant card of the deck.
     */
    public List<AssistantCard> getAssistantCards() {
        return assistantCards;
    }

    /**
     * This method set the name of the Wizard associated to the deck.
     * @param wizard name of the chosen wizard
     */
    public void setWizard(String wizard) {
        this.wizard = Wizard.valueOf(wizard.toUpperCase());
    }

    /**
     * Method removeAssistantCard removes the chosen Card with the specified priority.
     * @param priority the priority of the chosen card.
     * @return the chosen assistant card, removed from rhe deck.
     */
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
