package ml.byta.byta.Server.Responses;

import java.util.List;

import ml.byta.byta.Server.ModelsFromServer.ChatFromServer;

public class ChatsResponse {

    private boolean ok;
    private List<ChatFromServer> chats;

    public ChatsResponse(boolean ok, List<ChatFromServer> chats) {
        this.ok = ok;
        this.chats = chats;
    }

    public boolean isOk() {
        return ok;
    }

    public void setOk(boolean ok) {
        this.ok = ok;
    }

    public List<ChatFromServer> getChats() {
        return chats;
    }

    public void setChats(List<ChatFromServer> chats) {
        this.chats = chats;
    }
}
