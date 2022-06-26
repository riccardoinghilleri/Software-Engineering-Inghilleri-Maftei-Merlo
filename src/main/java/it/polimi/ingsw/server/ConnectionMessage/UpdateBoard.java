package it.polimi.ingsw.server.ConnectionMessage;

import it.polimi.ingsw.client.View;
import it.polimi.ingsw.server.model.Board;
/**
 * This message is used after each move made by a player, to update the board.
 */

public class UpdateBoard implements Message,ServerMessage{
    private final Board board;

    public UpdateBoard(Board board) {
        this.board = board;
    }

    public Board getBoard() {
        return this.board;
    }


    @Override
    public void forward(View view) {

        view.displayBoard(this);
    }
}
