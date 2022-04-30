package it.polimi.ingsw.server.ConnectionMessage;

import it.polimi.ingsw.client.View;

import java.util.ArrayList;
import java.util.List;

public class MultipleChoiceMessage implements Message,ServerMessage{
    private final List<String> availableChoices;

    public MultipleChoiceMessage( List<?> availableChoices) {
        this.availableChoices = new ArrayList<>();
        for(Object o : availableChoices) {
            this.availableChoices.add(o.toString());
        }
    }


    public List<String> getAvailableChoices() {
        return this.availableChoices;
    }

    @Override
    public void forward(View view) {
        view.setupMultipleChoice(this);
    }
}
