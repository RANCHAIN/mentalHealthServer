package data;

public class ChatMessage {

    private String sessionUrl;
    private String messageBody;


    public ChatMessage() {
    }

    public ChatMessage(String sessionUrl, String messageBody) {
        this.sessionUrl = sessionUrl;
        this.messageBody = messageBody;
    }

    public String getSessionUrl() {
        return sessionUrl;
    }

    public void setSessionUrl(String sessionUrl) {
        this.sessionUrl = sessionUrl;
    }

    public String getMessageBody() {
        return messageBody;
    }

    public void setMessageBody(String messageBody) {
        this.messageBody = messageBody;
    }
}
