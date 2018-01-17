package ml.byta.byta.Objects.Server;

import java.util.List;

import ml.byta.byta.DataBase.Message;

public class MessagesFromChatResponse {

    private List<Message> messages;

    public MessagesFromChatResponse(List<Message> messages) {
        this.messages = messages;
    }

    public List<Message> getMessages() {
        return messages;
    }

    public void setMessages(List<Message> messages) {
        this.messages = messages;
    }
}
