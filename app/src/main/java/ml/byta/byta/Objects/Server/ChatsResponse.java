package ml.byta.byta.Objects.Server;

import java.util.List;

import ml.byta.byta.DataBase.Chat;

public class ChatsResponse {

    private List<ChatFromServer> chats;

    public ChatsResponse(List<ChatFromServer> chats) {
        this.chats = chats;
    }

    public List<ChatFromServer> getChats() {
        return chats;
    }

    public void setChats(List<ChatFromServer> chats) {
        this.chats = chats;
    }
}
