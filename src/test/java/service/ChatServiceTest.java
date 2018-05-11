package service;

import org.junit.Assert;
import org.junit.Test;

public class ChatServiceTest {
    @Test
    public void getInstance() throws Exception {
        ChatService chatService = ChatService.getInstance();
        Assert.assertNotNull(chatService);
    }

    @Test
    public void openNewChatSession() throws Exception {
        ChatService chatService = ChatService.getInstance();
        Assert.assertNotNull(chatService);
        chatService.openNewChatSession("from1", "to2");
        Assert.assertTrue(ChatService.getInstance().ifExistedChatSession("from1", "to2"));
    }

    @Test
    public void ifExistedChatSession() throws Exception {

    }

    @Test
    public void sendMessageToQueue() throws Exception {
        ChatService chatService = ChatService.getInstance();
        Assert.assertNotNull(chatService);
        String url = chatService.openNewChatSession("from2", "to3");
        //  Assert.assertTrue(ChatService.getInstance().ifExistedChatSession("from2", "to3"));
        boolean res = chatService.sendMessageToQueue(url, "testingMessage");
        Assert.assertTrue(res);
    }

    @Test
    public void terminateOneSession() throws Exception {
        ChatService chatService = ChatService.getInstance();
        Assert.assertNotNull(chatService);

     //   chatService.terminateOneSession("from1", "to2");
    }

}