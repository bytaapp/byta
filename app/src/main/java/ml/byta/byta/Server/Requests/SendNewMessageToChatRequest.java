package ml.byta.byta.Server.Requests;

public class SendNewMessageToChatRequest {

    private int chatId;
    private int authorId;
    private String text;
    private long timestamp;

    public SendNewMessageToChatRequest(int chatId, int authorId, String text, long timestamp) {
        this.chatId = chatId;
        this.authorId = authorId;
        this.text = text;
        this.timestamp = timestamp;
    }

    public int getChatId() {
        return chatId;
    }

    public void setChatId(int chatId) {
        this.chatId = chatId;
    }

    public int getAuthorId() {
        return authorId;
    }

    public void setAuthorId(int authorId) {
        this.authorId = authorId;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}
