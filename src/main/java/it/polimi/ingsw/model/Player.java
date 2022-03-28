package it.polimi.ingsw.model;

public class Player {
    String nickname;
    int clintID;

    public Player(String nickname, int clientID){
        this.nickname=nickname;
        this.clintID=clientID;
    }

    public String getNickname() {
        return nickname;
    }

    public int getClintID() {
        return clintID;
    }
}
