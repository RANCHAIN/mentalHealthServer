package data;

import java.util.List;

public class BlockChainPersistenceData {
    private ChatSession chatSession;
    private List<String> messages;

    public BlockChainPersistenceData(ChatSession chatSession, List<String> messages) {
        this.chatSession = chatSession;
        this.messages = messages;
    }

    public ChatSession getChatSession() {
        return chatSession;
    }

    public void setChatSession(ChatSession chatSession) {
        this.chatSession = chatSession;
    }

    public List<String> getMessages() {
        return messages;
    }

    public void setMessages(List<String> messages) {
        this.messages = messages;
    }
}
