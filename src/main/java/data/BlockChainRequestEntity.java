package data;

/*


{
  "messageId": "string34333",
  "sender": "a1",
  "receiver": "a2",
  "content": "string"
}


 */

public class BlockChainRequestEntity {

    private String messageId;
    private String sender;
    private String receiver;
    private String content;
    private String $class;

    public String getMessageId() {
        return messageId;
    }

    public String get$class() {
        return $class;
    }

    public void set$class(String $class) {
        this.$class = $class;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
