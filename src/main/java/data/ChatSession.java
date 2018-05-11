package data;

public class ChatSession {


    private String fromId;
    private String toId;
    private String timeStamp;
    private SEVERITY severity;
    private TOPICS topics;


    public ChatSession() {
    }

    public String getFromId() {
        return fromId;
    }

    public void setFromId(String fromId) {
        this.fromId = fromId;
    }

    public String getToId() {
        return toId;
    }

    public void setToId(String toId) {
        this.toId = toId;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }

    public SEVERITY getSeverity() {
        return severity;
    }

    public void setSeverity(SEVERITY severity) {
        this.severity = severity;
    }

    public TOPICS getTopics() {
        return topics;
    }

    public void setTopics(TOPICS topics) {
        this.topics = topics;
    }
}
