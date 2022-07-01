package it.polimi.ingsw.server.ConnectionMessage;

import it.polimi.ingsw.client.cli.Cli;
import it.polimi.ingsw.client.View;
import it.polimi.ingsw.client.gui.Gui;

/**
 * This class represents the message to be displayed by the view during the
 * different phases of the game.
 */
public class InfoMessage implements Message,ServerMessage{
    private final String string;
    private final String winner;
    private final boolean disconnection;
    private final boolean waitBoard;

    /**
     * Constructor of the class
     */
    public InfoMessage(String string, boolean waitBoard) {
        this.string = string;
        this.winner=null;
        this.disconnection=false;
        this.waitBoard=waitBoard;
    }

    /**
     * Message sent at the end of a game with the name of winner
     */
    public InfoMessage(String string,boolean waitBoard, String winner){
        this.string = string;
        this.winner=winner;
        this.disconnection=false;
        this.waitBoard=waitBoard;
    }

    /**
     * Message sent at the end of the game, when a client disconnect
     */
    public InfoMessage(String string,boolean waitBoard, boolean disconnection){
        this.string = string;
        this.winner=null;
        this.waitBoard=waitBoard;
        this.disconnection=disconnection;
    }

    /**
     * @return the String with the message
     */
    public String getString() {
        return this.string;
    }

    /**
     * @return the name of the winner
     */
    public String getWinner(){
        return winner;
    }

    /**
     * @return boolean whose value is true if the view has to wait the board
     * to be displayed before display the message
     */
    public boolean waitBoard(){
        return waitBoard;
    }

    /**
     * This method calls the correct view method to handle the message.
     * @param view view that has to handle the message.
     */
    @Override
    public void forward(View view) {
        if(view instanceof Cli || (this.winner == null && !this.disconnection ))
            view.displayInfo(this);
        else if ( this.winner != null)
            ((Gui)view).displayWinner(this);
        else if (this.disconnection) ((Gui)view).displayDisconnectionAlert(this);
    }

}
