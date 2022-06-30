package it.polimi.ingsw.server.ConnectionMessage;

import it.polimi.ingsw.client.View;
import it.polimi.ingsw.enums.Action;
import it.polimi.ingsw.server.model.*;

import java.util.ArrayList;
import java.util.List;
/**
 * This class presents the concrete action asked the player by the server.
 * It has a different method for each movement allowed, according to the parameters
 */

public class AskActionMessage implements Message, ServerMessage {
    private final Action action;
    private final int data;
    private final List<Island> islands;
    private final School school;
    private final List<AssistantCard> availableAssistantCards;
    private final Cloud[] clouds;
    private final CharacterCard[] characterCards;

    private final CharacterCard chosenCharacterCard;

    private final boolean error;

    //CHOOSE_ASSISTANT_CARD
    /**
     * This constructor represents the action 'CHOOSE_ASSISTANT_CARD'
     * @param availability list of assistant Cards left in the decks
     */

    public AskActionMessage(Action action, List<AssistantCard> availability,boolean error) {
        this.action = action;
        this.data = -1;
        this.school = null;
        this.availableAssistantCards = new ArrayList<>(availability);
        this.clouds = null;
        this.characterCards = null;
        this.islands = null;
        this.chosenCharacterCard=null;
        this.error=error;
    }
    /**
     * This constructor represents the action 'CHOOSE_CHARACTER_CARD'
     * @param characterCards : list of character Cards left in the board
     */

    public AskActionMessage(Action action, CharacterCard[] characterCards,boolean error) {
        this.action = action;
        this.data = -1;
        this.school = null;
        this.availableAssistantCards = null;
        this.clouds = null;
        this.characterCards = characterCards.clone();
        this.islands = null;
        this.chosenCharacterCard=null;
        this.error=error;
    }
    /**
     * This constructor asks how the player wants to use the character card,
     * therefore it needs as parameter the card, the player's school and also the islands since
     * some special movements involves islands
     */

    //USE_CHARACTER_CARD
    public AskActionMessage(Action action, CharacterCard characterCard, List<Island> islands, School school,boolean error) {
        this.action = action;
        this.data = -1;
        this.school = new School(school);
        this.availableAssistantCards = null;
        this.chosenCharacterCard = characterCard;
        this.clouds = null;
        this.islands = new ArrayList<>(islands);
        this.characterCards=null;
        this.error=error;
    }

    //DEFAULT_MOVEMENTS
    /**
     * This constructor is for the 'DEFAULT_ACTIONS' action(when a player moves a student from the entrance to
     * the dining room or to an island)
     * @param action  type of action
     * @param islands islands involved in the choice
     * @param school school involved in the choice
     */

    public AskActionMessage(Action action, School school, List<Island> islands,boolean error) {
        this.action = action;
        this.data = -1;
        this.school = new School(school);
        this.availableAssistantCards = null;
        this.clouds = null;
        this.characterCards = null;
        this.islands = new ArrayList<>(islands);
        this.chosenCharacterCard=null;
        this.error=error;
    }

    //CHOOSE_CLOUD

    /**
     * This constructor is for the 'CHOOSE_CLOUD' action
     * @param action the type of action
     * @param availability array of clouds still available to chose from
     */

    public AskActionMessage(Action action, Cloud[] availability,boolean error) {
        this.action = action;
        this.data = -1;
        this.school = null;
        this.availableAssistantCards = null;
        this.clouds = availability.clone();//TODO non so se serve clone. rivedere
        this.characterCards = null;
        this.islands = null;
        this.chosenCharacterCard=null;
        this.error=error;
    }

    //MOVE_MOTHER_NATURE
    /**
     * This constructor represents the action 'MOVE_MOTHER_NATURE'
     * @param data : how many steps mothernature needs to be moved
     */

    public AskActionMessage(Action action, int data,boolean error) {
        this.action = action;
        this.data = data;
        this.availableAssistantCards = null;
        this.school = null;
        this.clouds = null;
        this.characterCards = null;
        this.islands = null;
        this.chosenCharacterCard=null;
        this.error=error;
    }
    /**
     * getters and setters for the parameters of the class
     */

    public CharacterCard getChosenCharacterCard() {
        return chosenCharacterCard;
    }

    public boolean isError(){
        return this.error;
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

    /**
     * This method forwards the message to the view
     */

    @Override
    public void forward(View view) {
        view.askAction(this);
    }
}
