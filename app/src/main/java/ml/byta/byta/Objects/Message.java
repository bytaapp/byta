package ml.byta.byta.Objects;

public class Message {

    private int id;
    private String text;
    private User user;
    private String received_at;

    public Message(int id, String text, User user, String received_at) {
        this.id = id;
        this.text = text;
        this.user = user;
        this.received_at = received_at;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getReceived_at() {
        return received_at;
    }

    public void setReceived_at(String received_at) {
        this.received_at = received_at;
    }

}
