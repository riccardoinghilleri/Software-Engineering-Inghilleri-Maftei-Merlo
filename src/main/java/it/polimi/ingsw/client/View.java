package it.polimi.ingsw.client;

import it.polimi.ingsw.server.ConnectionMessage.*;

public interface View {
    void askAction(AskActionMessage message);
    void displayInfo(InfoMessage message);
    void setupMultipleChoice(MultipleChoiceMessage message);
    void setupNickname(NicknameMessage message);
    void displayBoard(UpdateBoard message);
    void setupConnection();
    String getAddress();
    int getPort();

}

