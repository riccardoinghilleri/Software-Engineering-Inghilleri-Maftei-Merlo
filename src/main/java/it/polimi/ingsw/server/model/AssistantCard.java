package it.polimi.ingsw.server.model;

import it.polimi.ingsw.constants.Constants;

import java.io.Serializable;

public class AssistantCard implements Serializable {
    int priority;
    int natureMotherSteps;

    public AssistantCard(int priority, int natureMotherSteps) {
        this.priority = priority;
        this.natureMotherSteps = natureMotherSteps;
    }

    public int getPriority() {
        return priority;
    }

    public int getMotherNatureSteps() {
        return natureMotherSteps;
    }
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
