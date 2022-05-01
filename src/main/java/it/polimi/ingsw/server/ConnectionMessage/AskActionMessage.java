package it.polimi.ingsw.server.ConnectionMessage;

import it.polimi.ingsw.client.View;
import it.polimi.ingsw.controller.Action;
import it.polimi.ingsw.server.model.*;

import java.util.ArrayList;
import java.util.List;

public class AskActionMessage implements Message, ServerMessage {
    private final Action action;
    private final int data;
    private final List<Island> islands;
    private final School school;
    private final List<AssistantCard> availableAssistantCards;
    private final Cloud[] clouds;
    private final CharacterCard[] characterCards;

    //CHOOSE_ASSISTANT_CARD
    public AskActionMessage(Action action, List<AssistantCard> availability) {
        this.action = action;
        this.data = -1;
        this.school = null;
        this.availableAssistantCards = new ArrayList<>(availability);
        this.clouds = null;
        this.characterCards = null;
        this.islands = null;
    }

    //USE_CHARACTER_CARD
    public AskActionMessage(Action action, CharacterCard[] characterCards, List<Island> islands, School school) {
        this.action = action;
        this.data = -1;
        this.school = school; //TODO va bene che mi passo il riferimento alla vera scuola??
        this.availableAssistantCards = null;
        this.characterCards = characterCards.clone();
        this.clouds = null;
        this.islands = islands;
    }

    //DEFAULT_MOVEMENTS
    public AskActionMessage(Action action, School school, List<Island> islands) {
        this.action = action;
        this.data = -1;
        this.school = school;
        this.availableAssistantCards = null;
        this.clouds = null;
        this.characterCards = null;
        this.islands = islands;
    }

    //CHOOSE_CLOUD
    public AskActionMessage(Action action, Cloud[] availability) {
        this.action = action;
        this.data = -1;
        this.school = null;
        this.availableAssistantCards = null;
        this.clouds = availability.clone();//TODO non so se serve clone. rivedere
        this.characterCards = null;
        this.islands = null;
    }

    //MOVE_MOTHER_NATURE
    public AskActionMessage(Action action, int data) {
        this.action = action;
        this.data = data;
        this.availableAssistantCards = null;
        this.school = null;
        this.clouds = null;
        this.characterCards = null;
        this.islands = null;
    }

    public Cloud[] getClouds() {
        return clouds;
    }

    public CharacterCard[] getCharacterCards() {
        return characterCards;
    }

    public List<Island> getIslands() {
        return islands;
    }

    public School getSchool() {
        return school;
    }

    public List<AssistantCard> getAvailableAssistantCards() {
        return availableAssistantCards;
    }

    public int getData() {
        return data;
    }

    public Action getAction() {
        return action;
    }


    @Override
    public void forward(View view) {
        view.askAction(this);
    }
}
