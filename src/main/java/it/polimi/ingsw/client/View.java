package it.polimi.ingsw.client;

import it.polimi.ingsw.server.ConnectionMessage.*;

/**
 * Interface implemented by the Cli.
 */
public interface View {
    void askAction(AskActionMessage message);
    void displayInfo(InfoMessage message);
    void setupMultipleChoice(MultipleChoiceMessage message);
    void setupNickname(NicknameMessage message);
    void displayBoard(UpdateBoard message);
    void setupConnection(); //TODO dato ceh viene usato solo nella cli, si potrebbe togliere da questa interfaccia
    String getAddress();
    int getPort();

}

