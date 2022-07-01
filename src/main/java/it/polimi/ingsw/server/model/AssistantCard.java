package it.polimi.ingsw.server.model;

import java.io.Serializable;

/**
 * This class represents the Assistant Card.
 * It has a priority and a maximum number of steps which can be done by MotherNature while playing this card.
 * 10 assistant Cards are chosen by each player at the beginning of the game.
 */

public class AssistantCard implements Serializable {
    int priority;
    int motherNatureSteps;

    /**
     * Constructor AssistantCard creates a new AssistantCard instance.
     * @param priority  The number according to which players receive their turn.
     *                  The one who chooses the lowest number is the first who starts the game.
     * @param motherNatureSteps The maximum number of steps which can be done by MotherNature.
     */
    public AssistantCard(int priority, int motherNatureSteps) {
        this.priority = priority;
        this.motherNatureSteps = motherNatureSteps;
    }

    /**
     * @return the Assistant Card's priority
     */
    public int getPriority() {
        return priority;
    }

    /**
     * @return the Assistant Card's MotherNature steps
     */
    public int getMotherNatureSteps() {
        return motherNatureSteps;
    }
}
