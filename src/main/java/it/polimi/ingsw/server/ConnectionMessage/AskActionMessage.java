package it.polimi.ingsw.server.ConnectionMessage;

import it.polimi.ingsw.client.View;
import it.polimi.ingsw.enums.Action;
import it.polimi.ingsw.server.model.*;

import java.util.ArrayList;
import java.util.List;

/**
 * This class represents the message sent by the gameHandler to the player to ask an action.
 * It has a different method for each movement allowed, according to the parameters.
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
     * Constructor used to ask the action 'CHOOSE_ASSISTANT_CARD'
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
     * Constructor used to ask the action 'CHOOSE_CHARACTER_CARD'
     * @param characterCards : list of character Cards left in the board
     */
    //CHOOSE_CHARACTER_CARD
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
     * Constructor used to ask how the player wants to use the character card.
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


    /**
     * Constructor used to ask the 'DEFAULT_MOVEMENTS' action (when a player moves
     * a student from the entrance to the dining room or to an island)
     * @param action type of action
     * @param islands islands involved in the choice
     * @param school school involved in the choice
     */
    //DEFAULT_MOVEMENTS
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

    /**
     * Constructor used to ask the action 'CHOOSE_CLOUD'
     * @param action the type of action
     * @param availability array of clouds still available to chose from.
     */
    //CHOOSE_CLOUD
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


    /**
     * Constructor used to ask the action 'MOVE_MOTHER_NATURE'
     * @param data : how many steps motherNature needs to be moved
     */
    //MOVE_MOTHER_NATURE
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
     * @return the ChosenCharacterCard
     */
    public CharacterCard getChosenCharacterCard() {
        return chosenCharacterCard;
    }

    /**
     * @return boolean whose value is true if an error was made in the previous action
     */
    public boolean isError(){
        return this.error;
    }

    /**
     * @return clouds on the board
     */
    public Cloud[] getClouds() {
        return clouds;
    }

    /**
     * @return CharacterCards on the board
     */
    public CharacterCard[] getCharacterCards() {
        return characterCards;
    }

    /**
     * @return Islands on the board
     */
    public List<Island> getIslands() {
        return islands;
    }

    /**
     * @return School of the current player
     */
    public School getSchool() {
        return school;
    }

    /**
     * @return Assistant Cards of the current player
     */
    public List<AssistantCard> getAvailableAssistantCards() {
        return availableAssistantCards;
    }

    /**
     * @return the int param of the message,
     * (e.g. how many steps can do motherNature)
     */
    public int getData() {
        return data;
    }

    /**
     * @return the Action
     */
    public Action getAction() {
        return action;
    }

    /**
     * This method calls the correct view method to handle the message.
     * @param view view that has to handle the message.
     */
    @Override
    public void forward(View view) {
        view.askAction(this);
    }
}
