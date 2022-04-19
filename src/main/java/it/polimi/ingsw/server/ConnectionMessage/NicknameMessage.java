package it.polimi.ingsw.server.ConnectionMessage;

public class NicknameMessage extends ClientMessage{
    String nickname;

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }
}
