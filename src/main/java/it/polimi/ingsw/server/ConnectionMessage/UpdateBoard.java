package it.polimi.ingsw.server.ConnectionMessage;

import it.polimi.ingsw.client.View;
import it.polimi.ingsw.server.model.Board;
/**
 * This message is used after each action made by a player, to update the board.
 */

public class UpdateBoard implements Message,ServerMessage{
    private final Board board;

    /**
     * Constructor of the class
     */
    public UpdateBoard(Board board) {
        this.board = board;
    }

    /**
     * @return the board
     */
    public Board getBoard() {
        return this.board;
    }


    /**
     * This method calls the correct view method to handle the message.
     * @param view view that has to handle the message.
     */
    @Override
    public void forward(View view) {

        view.displayBoard(this);
    }
}
