package it.polimi.ingsw.client;

import it.polimi.ingsw.server.ConnectionMessage.*;

/**
 * Interface implemented by the Cli and GUI.
 */
public interface View {
    void askAction(AskActionMessage message);
    void displayInfo(InfoMessage message);
    void setupMultipleChoice(MultipleChoiceMessage message);
    void setupNickname(NicknameMessage message);
    void displayBoard(UpdateBoard message);
    String getAddress();
    int getPort();

}

