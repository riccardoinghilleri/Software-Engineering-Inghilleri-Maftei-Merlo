package it.polimi.ingsw.server.model;
import it.polimi.ingsw.constants.Constants;

import java.io.Serializable;

/**
 * This class represents an Assistant Card.
 * It has a priority and a maximum number of steps which can be done by MotherNature while playing this card
 * 10 assistant Cards are chosen by each player at the beginning of the game
 */

public class AssistantCard implements Serializable {
    int priority;
    int natureMotherSteps;
    /**
     * Constructor AssistantCard creates a new AssistantCard instance.
     * @param priority  it is the number according to which players receive their turn.
     *                 The one who chooses the lowest number is the first who starts the game.
     * @param natureMotherSteps The maximum number of steps which can be done by MotherNature
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

    /**
     * This method is used to display the assistant card on the screen
     */
/*
    public StringBuilder draw(int x, int y) {
        StringBuilder card = new StringBuilder();
        card.append(Constants.cursorUp(y));
        String top_wall = "╒════════╕\n";
        String bottom_wall = "╘════════╛\n";
        String vertical = "│";
        Constants.moveObject(card, x, top_wall);
        String line = vertical + "PRIORITY" + vertical + "\n";
        Constants.moveObject(card, x, line);
        if (priority == 10) {
            line = vertical + "   " + priority + "   " + vertical + "\n";
        } else line = vertical + "   " + priority + "    " + vertical + "\n";
        Constants.moveObject(card, x, line);
        line = vertical + " STEPS: " + vertical + "\n";
        Constants.moveObject(card, x, line);
        line = vertical + "   " + natureMotherSteps + "    " + vertical + "\n";
        Constants.moveObject(card, x, line);
        Constants.moveObject(card, x, bottom_wall);
        return card;
    }*/

    @Override
    public String toString() {
        return "PRIORITY: " + priority + " - MOTHER NATURE STEPS: " + natureMotherSteps;
    }
}
