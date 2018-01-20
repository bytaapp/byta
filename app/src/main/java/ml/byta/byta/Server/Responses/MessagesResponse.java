package ml.byta.byta.Server.Responses;

import java.util.List;

import ml.byta.byta.DataBase.Message;

public class MessagesResponse {

    private boolean ok;
    private List<Message> messages;
    private String error;

    public MessagesResponse(boolean ok, List<Message> messages, String error) {
        this.ok = ok;
        this.messages = messages;
        this.error = error;
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

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }
}
