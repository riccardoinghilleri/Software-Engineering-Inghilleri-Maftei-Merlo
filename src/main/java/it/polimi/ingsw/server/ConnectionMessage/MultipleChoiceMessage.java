package it.polimi.ingsw.server.ConnectionMessage;

import it.polimi.ingsw.client.View;

import java.util.ArrayList;
import java.util.List;

public class MultipleChoiceMessage implements Message,ServerMessage{
    private final String string;
    private final List<String> availableChoices;

    public MultipleChoiceMessage(String string, List<?> availableChoices) {
        this.availableChoices = new ArrayList<>();
        this.string = string;
        for(Object o : availableChoices) {
            this.availableChoices.add(o.toString());
        }
    }

    public String getString() {
        return this.string;
    }

    public List<String> getAvailableChoices() {
        return this.availableChoices;
    }

    @Override
    public void forward(View view) {
        view.setupMultipleChoice(this);
    }
}
