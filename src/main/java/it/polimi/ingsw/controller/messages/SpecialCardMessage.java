package it.polimi.ingsw.controller.messages;

import it.polimi.ingsw.controller.Action;

public class SpecialCardMessage implements Message{

    private final Action action;
    private final String specialCardName;

    public SpecialCardMessage(Action action, String specialCardName) {
        this.action = action;
        this.specialCardName = specialCardName;
    }

    @Override
    public Action getAction() {
        return action;
    }

    public String getSpecialCardName() {
        return specialCardName;
    }
}
