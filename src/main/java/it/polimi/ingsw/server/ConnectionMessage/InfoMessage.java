package it.polimi.ingsw.server.ConnectionMessage;

import it.polimi.ingsw.client.cli.Cli;
import it.polimi.ingsw.client.View;
import it.polimi.ingsw.client.gui.Gui;

public class InfoMessage implements Message,ServerMessage{
    private final String string;
    private String winner;
    private boolean disconnection;

    public InfoMessage(String string) {
        this.string = string;
        this.winner=null;
        this.disconnection=false;
    }
    public InfoMessage(String string, String winner){
        this.string = string;
        this.winner=winner;
        this.disconnection=false;
    }

    public InfoMessage(String string, boolean disconnection){
        this.string = string;
        this.winner=null;
        this.disconnection=disconnection;
    }
    public String getString() {
        return this.string;
    }

    public String getWinner(){
        return winner;
    }

    @Override
    public void forward(View view) {
        if(view instanceof Cli || (this.winner == null && !this.disconnection ))
            view.displayInfo(this);
        else if ( this.winner != null)
            ((Gui)view).displayWinner(this);
        else ((Gui)view).displayDisconnectionAlert(this);
    }

}
