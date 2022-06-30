package it.polimi.ingsw.server.model;

import java.io.Serializable;

/**
 * This class represents the Assistant Card.
 * It has a priority and a maximum number of steps which can be done by MotherNature while playing this card.
 * 10 assistant Cards are chosen by each player at the beginning of the game.
 */

public class AssistantCard implements Serializable {
    int priority;
    int natureMotherSteps;
    /**
     * Constructor AssistantCard creates a new AssistantCard instance.
     * @param priority  the number according to which players receive their turn.
     *                  The one who chooses the lowest number is the first who starts the game.
     * @param natureMotherSteps The maximum number of steps which can be done by MotherNature.
     */
    public AssistantCard(int priority, int natureMotherSteps) {
        this.priority = priority;
        this.natureMotherSteps = natureMotherSteps;
    }

    /**
     * @return the Assistant Card's priority
     */
    public int getPriority() {
        return priority;
    }
    /**
     * @return  the Assistant Card's MotherNature steps
     */
    public int getMotherNatureSteps() {
        return natureMotherSteps;
    }
}
