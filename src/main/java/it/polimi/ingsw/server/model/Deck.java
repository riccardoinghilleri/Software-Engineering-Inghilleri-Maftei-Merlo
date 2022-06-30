package it.polimi.ingsw.server.model;

import it.polimi.ingsw.enums.Wizard;

import java.io.Serializable;
import java.util.*;

/**
 * This class represents the deck associated to a player.
 * Each deck has a magician, which is chosen by the player.
 * After a student has played a specified card, the card will be removed from the deck,
 * so the number of cards available will decrease.
 */

public class Deck implements Serializable {
    Wizard wizard = null;
    List<AssistantCard> assistantCards;

    /**
     * Constructor Deck creates a new Deck instance.
     * The AssistantCard array is initialized and creates 10 different cards for each deck
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
     * @return a Wizard Enum which corresponds to the card
     */
    public Wizard getWizard() {
        return wizard;
    }

    /**
     *  @return the list of assistant card of the deck
     */
    public List<AssistantCard> getAssistantCards() {
        return assistantCards;
    }

    /**
     * After the player chooses the wizard that wants to play , the wizard Name is set to te deck,
     * if only the given name is one of the list of enum Wizard
     * @param wizard string of the wizard
     */
    public void setWizard(String wizard) {
        this.wizard = Wizard.valueOf(wizard.toUpperCase());
    }

    /**
     * Method removeAssistantCard removes the played Chard with the specified priority,
     * given throw the 'priority' parameter.
     * @param priority the priority of the card already use
     * @return an assistant card, which is not anymore on the deck
     */
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
