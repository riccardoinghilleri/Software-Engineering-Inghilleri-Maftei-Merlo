package it.polimi.ingsw.server.ConnectionMessage;

import it.polimi.ingsw.client.cli.Cli;
import it.polimi.ingsw.client.View;
import it.polimi.ingsw.client.gui.Gui;

/**
 * This class represents the message to be displayed by the view during the different phases of the game.
 */
public class InfoMessage implements Message,ServerMessage{
    private final String string;
    private String winner;
    private boolean disconnection;

    private boolean waitBoard;

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
     * Message sent at the end of a game with the winner
     */
    public InfoMessage(String string,boolean waitBoard, String winner){
        this.string = string;
        this.winner=winner;
        this.disconnection=false;
        this.waitBoard=waitBoard;
    }

    /**
     * Message sent at the end of the game, when the client disconnect
     */
    public InfoMessage(String string,boolean waitBoard, boolean disconnection){
        this.string = string;
        this.winner=null;
        this.waitBoard=waitBoard;
        this.disconnection=disconnection;
    }
    public String getString() {
        return this.string;
    }

    public String getWinner(){
        return winner;
    }

    public boolean waitBoard(){
        return waitBoard;
    }

    @Override
    public void forward(View view) {
        if(view instanceof Cli || (this.winner == null && !this.disconnection ))
            view.displayInfo(this);
        else if ( this.winner != null)
            ((Gui)view).displayWinner(this);
        else if (this.disconnection) ((Gui)view).displayDisconnectionAlert(this);
    }

}
