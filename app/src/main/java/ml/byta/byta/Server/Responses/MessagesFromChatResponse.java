package ml.byta.byta.Server.Responses;

import java.util.List;

import ml.byta.byta.DataBase.Message;

public class MessagesFromChatResponse {

    private boolean ok;
    private List<Message> messages;

    public MessagesFromChatResponse(boolean ok, List<Message> messages) {
        this.ok = ok;
        this.messages = messages;
    }

    public boolean isOk() {
        return ok;
    }

    public void setOk(boolean ok) {
        this.ok = ok;
    }

    public List<Message> getMessages() {
        return messages;
    }

    public void setMessages(List<Message> messages) {
        this.messages = messages;
    }
}
