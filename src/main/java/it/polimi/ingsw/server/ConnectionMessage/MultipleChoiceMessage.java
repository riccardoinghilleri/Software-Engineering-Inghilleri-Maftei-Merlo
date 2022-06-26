package it.polimi.ingsw.server.ConnectionMessage;

import it.polimi.ingsw.client.View;

import java.util.ArrayList;
import java.util.List;
/**
 * This message contains a List of parameters declaring the availability of the latter ( e.g. colors or magician)
 */


public class MultipleChoiceMessage implements Message,ServerMessage{
    private final List<String> availableChoices;
    private final boolean color;

    public MultipleChoiceMessage(List<?> availableChoices, boolean color) {
        this.color=color;
        this.availableChoices = new ArrayList<>();
        for(Object o : availableChoices) {
            this.availableChoices.add(o.toString());
        }
    }

    public boolean isColor() {
        return color;
    }

    public List<String> getAvailableChoices() {
        return this.availableChoices;
    }

    @Override
    public void forward(View view) {
        view.setupMultipleChoice(this);
    }
}
