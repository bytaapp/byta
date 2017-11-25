package ml.byta.byta.Objects;


import java.util.List;

public class MessagesFromChatResponse {

    private int count;
    private List<Message> messages;

    public MessagesFromChatResponse(int count, List<Message> messages) {
        this.count = count;
        this.messages = messages;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public List<Message> getMessages() {
        return messages;
    }

    public void setMessages(List<Message> messages) {
        this.messages = messages;
    }
    
}
