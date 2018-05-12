package messageQueue;

import org.junit.Assert;
import org.junit.Test;

import java.util.List;

public class SqsSerivceTest {
    @Test
    public void ifExistedQueue() throws Exception {
        SqsSerivce sqsSerivce = SqsSerivce.getInstance();
        Assert.assertNotNull(sqsSerivce);
        List<String> res = sqsSerivce.listAllQueues();
        System.out.println(sqsSerivce.ifExistedQueue("aaa"));

    }

    @Test
    public void getInstance() throws Exception {
        SqsSerivce sqsSerivce = SqsSerivce.getInstance();
        Assert.assertNotNull(sqsSerivce);
    }

    @Test
    public void createQueue() throws Exception {

        SqsSerivce sqsSerivce = SqsSerivce.getInstance();
        Assert.assertNotNull(sqsSerivce);
        String q1 = sqsSerivce.createQueue("q1");
        String q2 = sqsSerivce.createQueue("q2");
        String q3 = sqsSerivce.createQueue("q3");

        List<String> res = sqsSerivce.listAllQueues();
        Assert.assertTrue(res.contains(q1));
        Assert.assertTrue(res.contains(q2));
        Assert.assertTrue(res.contains(q3));

//        sqsSerivce.deleteOneQueue("q1");
//        sqsSerivce.deleteOneQueue("q2");
//        sqsSerivce.deleteOneQueue("q3");


    }

    @Test
    public void insertOneMessageToOneQueue() throws Exception {
        SqsSerivce sqsSerivce = SqsSerivce.getInstance();
        Assert.assertNotNull(sqsSerivce);
        String q4 = sqsSerivce.createQueue("q4");
        sqsSerivce.insertOneMessageToOneQueue("q4", "lalalaa");
        sqsSerivce.insertOneMessageToOneQueue("q4", "lalaasdfsdfsfsdfdslaa");
        // List<Message> res = sqsSerivce.receiveMessagesFromQueue(q4);
        // System.out.println(res);
    }

    @Test
    public void deleteOneQueue() throws Exception {
    }

    @Test
    public void receiveMessagesFromQueue() throws Exception {
        SqsSerivce sqsSerivce = SqsSerivce.getInstance();
        Assert.assertNotNull(sqsSerivce);
        String q1 = sqsSerivce.createQueue("qqqq");
        sqsSerivce.insertOneMessageToOneQueue("qqqq", "lalalaa");
        sqsSerivce.insertOneMessageToOneQueue("qqqq", "lalaldsdsdaa");
       // String res = sqsSerivce.receiveMessagesFromQueue("qqqq");
      //  System.out.println(res);

    }

}