package ml.byta.byta.Objects;

public class SendNewMessageToChatRequest {

    private int author_id;
    private String text;
    private String created_at;

    public SendNewMessageToChatRequest(int author_id, String text, String created_at) {
        this.author_id = author_id;
        this.text = text;
        this.created_at = created_at;
    }

    public int getAuthor_id() {
        return author_id;
    }

    public void setAuthor_id(int author_id) {
        this.author_id = author_id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

}
