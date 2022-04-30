package it.polimi.ingsw.client;

import it.polimi.ingsw.server.ConnectionMessage.AskActionMessage;
import it.polimi.ingsw.server.ConnectionMessage.InfoMessage;
import it.polimi.ingsw.server.ConnectionMessage.MultipleChoiceMessage;
import it.polimi.ingsw.server.ConnectionMessage.NicknameMessage;

public interface View {
    public void askAction(AskActionMessage message);
    public void displayInfo(InfoMessage message);
    public void setupMultipleChoice(MultipleChoiceMessage message);
    public void setupNickname(NicknameMessage message);

}

