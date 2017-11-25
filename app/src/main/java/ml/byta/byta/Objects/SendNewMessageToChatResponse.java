package ml.byta.byta.Objects;

public class SendNewMessageToChatResponse {

    private Description description;

    public SendNewMessageToChatResponse(Description description) {
        this.description = description;
    }

    public Description getDescription() {
        return description;
    }

    public void setDescription(Description description) {
        this.description = description;
    }

}
