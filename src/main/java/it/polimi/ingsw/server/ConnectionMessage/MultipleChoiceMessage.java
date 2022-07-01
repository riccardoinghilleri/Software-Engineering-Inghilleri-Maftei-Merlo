package it.polimi.ingsw.server.ConnectionMessage;

import it.polimi.ingsw.client.View;

import java.util.ArrayList;
import java.util.List;
/**
 * This message is used during the Setup to make the players choose the Color and the Wizard.
 */
public class MultipleChoiceMessage implements Message,ServerMessage{
    private final List<String> availableChoices;
    private final boolean color;

    /**
     * Constructor of the class
     */
    public MultipleChoiceMessage(List<?> availableChoices, boolean color) {
        this.color=color;
        this.availableChoices = new ArrayList<>();
        for(Object o : availableChoices) {
            this.availableChoices.add(o.toString());
        }
    }


    /**
     @return boolean whose value is true if the gameHandler is asking for the color.
     */
    public boolean isColor() {
        return color;
    }

    /**
     @return list of available choices
     */
    public List<String> getAvailableChoices() {
        return this.availableChoices;
    }

    /**
     * This method calls the correct view method to handle the message.
     * @param view view that has to handle the message.
     */
    @Override
    public void forward(View view) {
        view.setupMultipleChoice(this);
    }
}
