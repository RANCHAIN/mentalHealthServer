package controller;

import data.*;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.http.ResponseEntity;
import utils.Utils;

public class ChatControllerImplTest {

    private String header = "application/json; charset=utf-8";

    @Test
    public void test1() throws Exception {

        ChatControllerImpl chatController = new ChatControllerImpl();

        ResponseEntity<String> responseEntity = chatController.test();
        Assert.assertTrue(responseEntity.getBody().equalsIgnoreCase("testing the restful controller of chat"));


    }

    @Test
    public void openSession() throws Exception {
        ChatControllerImpl chatController = new ChatControllerImpl();

        ChatSession chatSession = new ChatSession();
        chatSession.setFromId("from3");
        chatSession.setToId("to3");
        ResponseEntity<String> responseEntity = chatController.openSession(this.header, chatSession);
        System.out.println(responseEntity.getStatusCode());
        System.out.println(responseEntity.getBody());
    }

    @Test
    public void sendMessage() throws Exception {
        ChatControllerImpl chatController = new ChatControllerImpl();


        ChatMessage chatMessage = new ChatMessage("https://sqs.us-east-1.amazonaws.com/183523990685/from3TTTOOOto3", "contentForTesting");

        ResponseEntity<String> responseEntity = chatController.sendMessage(this.header, chatMessage);
        System.out.println(responseEntity.getStatusCode());
        System.out.println(responseEntity.getBody());
    }

    @Test
    public void closeSession() throws Exception {
        ChatControllerImpl chatController = new ChatControllerImpl();

        ChatSession chatSession = new ChatSession();
        chatSession.setFromId("from3");
        chatSession.setToId("to3");
        ResponseEntity<String> responseEntity = chatController.closeSession(this.header, chatSession);
        System.out.println(responseEntity.getStatusCode());
        System.out.println(responseEntity.getBody());

    }

    @Test
    public void generateMockup2() {

        ChatControllerImpl chatController = new ChatControllerImpl();
        ResponseEntity<String> res = chatController.receiveConversation(this.header, new ConversationEntity("sen", "sss"));
        System.out.println(res.getBody());

    }

    @Test
    public void generateMockup() {
//        ChatSession chatSession = new ChatSession();
//        chatSession.setFromId("from3");
//        chatSession.setToId("to3");
//        chatSession.setSeverity(SEVERITY.HIGH);
//        chatSession.setTimeStamp("20170809");
//        chatSession.setTopics(TOPICS.LOVE);
//
//        System.out.println(Utils.getBeautifiedJson.apply(chatSession));
//
//
//        ChatMessage chatMessage = new ChatMessage("https://sqs.us-east-1.amazonaws.com/183523990685/from3TTTOOOto3", "contentForTesting");
//        System.out.println(Utils.getBeautifiedJson.apply(chatMessage));

        ChatControllerImpl chatController = new ChatControllerImpl();
        ResponseEntity<String> res = chatController.receiveConversation(this.header, new ConversationEntity("sen", "sss"));
        System.out.println(res.getBody());

    }


}