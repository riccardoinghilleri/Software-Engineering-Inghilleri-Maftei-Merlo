package it.polimi.ingsw.server.ConnectionMessage;

import it.polimi.ingsw.client.View;
import it.polimi.ingsw.controller.Action;
import it.polimi.ingsw.server.model.AssistantCard;
import it.polimi.ingsw.server.model.CharacterCard;
import it.polimi.ingsw.server.model.Cloud;
import it.polimi.ingsw.server.model.Student;

import java.util.ArrayList;
import java.util.List;

public class AskActionMessage implements Message, ServerMessage {
    private final Action action;
    private final int data;
    private final List<Student> availableStudents;
    private final List<AssistantCard> availableAssistantCards;
    private final Cloud[] clouds;
    private final CharacterCard[] characterCards;

    //CHOOSE_ASSISTANT_CARD
    public AskActionMessage(Action action, List<AssistantCard> availability) {
        this.action = action;
        this.data = -1;
        this.availableStudents = null;
        this.availableAssistantCards = new ArrayList<>(availability);
        this.clouds = null;
        this.characterCards = null;
    }

    //USE_CHARACTER_CARD
    public AskActionMessage(Action action, CharacterCard[] availability) {
        this.action = action;
        this.data = -1;
        this.availableStudents = null;
        this.availableAssistantCards = null;
        this.characterCards = availability.clone();
        this.clouds = null;
    }

    //DEFAULT_MOVEMENTS
    public AskActionMessage(Action action, List<Student> availability, int data) {
        this.action = action;
        this.data = data;
        this.availableStudents = new ArrayList<>(availability);
        this.availableAssistantCards = null;
        this.clouds = null;
        this.characterCards = null;
    }

    //CHOOSE_CLOUD
    public AskActionMessage(Action action, Cloud[] availability) {
        this.action = action;
        this.data = -1;
        this.availableStudents = null;
        this.availableAssistantCards = null;
        this.clouds = availability.clone();//TODO non so se serve clone. rivedere
        this.characterCards = null;
    }

    //MOVE_MOTHER_NATURE
    public AskActionMessage(Action action, int data) {
        this.action = action;
        this.data = data;
        this.availableAssistantCards = null;
        this.availableStudents = null;
        this.clouds = null;
        this.characterCards = null;
    }

    public Cloud[] getClouds() {
        return clouds;
    }

    public CharacterCard[] getCharacterCards() {
        return characterCards;
    }

    public List<Student> getAvailableStudents() {
        return availableStudents;
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
