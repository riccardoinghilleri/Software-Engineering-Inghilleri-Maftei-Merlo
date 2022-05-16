package it.polimi.ingsw.controller.actioncontroller;

import it.polimi.ingsw.server.ConnectionMessage.ActionMessage;
import it.polimi.ingsw.server.model.GameModel;
import it.polimi.ingsw.server.model.CharacterCardwithStudents;
import it.polimi.ingsw.server.model.Student;
import it.polimi.ingsw.server.model.BoardExpert;
import it.polimi.ingsw.enums.CharacterColor;
/**
 * This class represents the Centaur card and contains the specific method of the card.
 */
public class Clown implements CharacterCardStrategy{

    GameModel gameModel;
    BoardExpert board;

    /**
     * Constructor: it creates a new instance of Clown.
     * @param gameModel of type GameHandler - GameHandler reference.
     */
    public Clown(GameModel gameModel) {
        this.gameModel=gameModel;
        board=(BoardExpert) gameModel.getBoard();
    }

    /**
     * This method specifies the 'super-power' of the card :
     * getting 3 students from the card and putting them in the player's entrance.
     * Go to "model->boardExpert" to see the complete description.
     * @param actionMessage  message with all the necessary parameters for this type of card.
     */
    @Override
    public void useEffect(ActionMessage actionMessage) {
        int player = gameModel.getCurrentPlayer().getClientID();
        Student s1 = ((CharacterCardwithStudents) board.getCharacterCardbyName("CLOWN")).removeStudent(CharacterColor.valueOf(actionMessage.getParameters().get(0)));
        Student s2 = board.getSchoolByOwnerId(player).removeEntranceStudent(CharacterColor.valueOf(actionMessage.getParameters().get(1)));
        ((CharacterCardwithStudents) board.getCharacterCardbyName("CLOWN")).addStudent(s2);
        board.getSchoolByOwnerId(player).getEntrance().add(s1);
    }
}

