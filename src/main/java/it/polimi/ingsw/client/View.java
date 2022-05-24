package it.polimi.ingsw.client;

import it.polimi.ingsw.server.ConnectionMessage.*;

/**
 * Interface implemented by the Cli.
 */
public interface View {
    public void askAction(AskActionMessage message);
    public void displayInfo(InfoMessage message);
    public void setupMultipleChoice(MultipleChoiceMessage message);
    public void setupNickname(NicknameMessage message);
    public void displayBoard(UpdateBoard message);

    public void setupConnection();

}

