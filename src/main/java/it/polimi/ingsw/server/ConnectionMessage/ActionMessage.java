package it.polimi.ingsw.server.ConnectionMessage;

import it.polimi.ingsw.enums.Action;

import java.util.ArrayList;
import java.util.List;
/**
 * This class represents the messages sent by the client when performing an action:
 *  CHOOSE_ASSISTANT_CARD,
 *  CHOOSE_CHARACTER_CARD,
 *  USE_CHARACTER_CARD,
 *  DEFAULT_MOVEMENTS,
 *  MOVE_MOTHER_NATURE,
 *  CHOOSE_CLOUD
 *  Messages from the client contain an Action attribute that corresponds to a
 *  phase of the controller.
 *  Data presents all the numerical information that the client can give to the controller:
 *  (e.g. the number of steps of MotherNature, the number of students that a player wants to move).
 *  Parameters is a list of strings that represents all the other information
 *  excepting for the characterCardName.
 *  (e.g. the color of the student to move)
 *
 */

public class ActionMessage implements Message{
    private Action action;
    private String characterCardName;
    private int data;
    private final List<String> parameters;

    /**
     * Constructor of the class.
     * All the attribute are initialized to a default value.
     */
    public ActionMessage() {
        this.data = -1; //valore di default
        this.parameters = new ArrayList<>();
    }

    /**
     * Method setAction sets the action chosen by the player in the message.
     */
    public void setAction(Action action) {
        this.action = action;
    }

    /**
     * @return the action of the message.
     */
    public Action getAction() {
        return action;
    }

    /**
     * @return the name of the CharacterCard in case the player chooses to play
     * with a CharacterCard
     */
    public String getCharacterCardName() {
        return characterCardName;
    }

    /**
     * Method setCharacterCardName sets the name chosen by th player in the message
     */
    public void setCharacterCardName(String characterCardName) {
        this.characterCardName = characterCardName;
    }

    /**
     * This method returns the list of parameters
     */
    public List<String> getParameters() {
        return parameters;
    }

    /**
     * This method set a parameter one at a time.
     */
    public void setParameter(String parameter) {
        this.parameters.add(parameter);
    }

    /**
     * This method returns the data of the message.
     */
    public int getData() {
        return data;
    }

    /**
     * This method sets the data of the message.
     */
    public void setData(int data) {
        this.data = data;
    }
}
