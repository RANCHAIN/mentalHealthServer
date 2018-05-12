package messageQueue;

import com.amazonaws.AmazonClientException;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSClientBuilder;
import com.amazonaws.services.sqs.model.*;
import com.google.common.collect.Lists;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class SqsSerivce {

    private static final Logger logger = LoggerFactory.getLogger(SqsSerivce.class);
    private static SqsSerivce single_instance = null;
    private AmazonSQS amazonSQS;


    // private constructor restricted to this class itself
    private SqsSerivce() {
        ProfileCredentialsProvider credentialsProvider = new ProfileCredentialsProvider();
        try {
            credentialsProvider.getCredentials();
        } catch (Exception e) {
            throw new AmazonClientException(
                    "Cannot load the credentials from the credential profiles file. " +
                            "Please make sure that your credentials file is at the correct " +
                            "location (~/.aws/credentials), and is in valid format.",
                    e);
        }

        this.amazonSQS = AmazonSQSClientBuilder.standard()
                .withCredentials(credentialsProvider)
                .withRegion(Regions.US_EAST_1)
                .build();


    }

    // static method to create instance of Singleton class
    public static SqsSerivce getInstance() {
        if (single_instance == null)
            single_instance = new SqsSerivce();

        return single_instance;
    }


    public String createQueue(String queueName) {
        //   final Map<String, String> attributes = new HashMap<String, String>();
//        // A Ffinal Map<String, String> attributes = new HashMap<String, String>();IFO queue must have the FifoQueue attribute set to True
//        attributes.put("FifoQueue", "true");
//
//// If the user doesn't provide a MessageDeduplicationId, generate a MessageDeduplicationId based on the content.
        // attributes.put("ContentBasedDeduplication", "true");
//
//// The FIFO queue name must end with the .fifo suffix
//        final CreateQueueRequest createQueueRequest = new CreateQueueRequest("MyFifoQueue.fifo")
//


        logger.info("Creating a new SQS queue called MyQueue.\n");
        final CreateQueueRequest createQueueRequest = new CreateQueueRequest()
                .withQueueName(queueName)
                .addAttributesEntry(QueueAttributeName.ReceiveMessageWaitTimeSeconds
                        .toString(), "20");
//                .addAttributesEntry(QueueAttributeName.FifoQueue.toString(), "true")
//                .addAttributesEntry(QueueAttributeName.ContentBasedDeduplication.toString(), "true");
        //.withAttributes(attributes);
        // createQueueRequest.
        String myQueueUrl = this.amazonSQS.createQueue(createQueueRequest).getQueueUrl();
        return myQueueUrl;


    }


    public boolean insertOneMessageToOneQueue(String queueUrl, String messageBody) {
        try {
            logger.info("Sending a message to MyQueue.\n");
            this.amazonSQS.sendMessage(new SendMessageRequest(queueUrl, messageBody));
            return true;
        } catch (Exception e) {
            return false;
        }

    }

    public void deleteOneQueue(String queueUrl) {
        logger.info("Deleting the test queue.\n");
        this.amazonSQS.deleteQueue(new DeleteQueueRequest(queueUrl));
    }


    public Message receiveMessagesFromQueue(String queueUrl) {
        logger.info("Receiving messages from MyQueue.\n");
        ReceiveMessageRequest receiveMessageRequest = new ReceiveMessageRequest(queueUrl).withWaitTimeSeconds(20);

        List<Message> messages = this.amazonSQS.receiveMessage(receiveMessageRequest).getMessages();
//        for (Message message : messages) {
//            System.out.println("  Message");
//            System.out.println("    MessageId:     " + message.getMessageId());
//            System.out.println("    ReceiptHandle: " + message.getReceiptHandle());
//            System.out.println("    MD5OfBody:     " + message.getMD5OfBody());
//            System.out.println("    Body:          " + message.getBody());
//            for (Map.Entry<String, String> entry : message.getAttributes().entrySet()) {
//                System.out.println("  Attribute");
//                System.out.println("    Name:  " + entry.getKey());
//                System.out.println("    Value: " + entry.getValue());
//            }
//        }
//        System.out.println();


        if (!messages.isEmpty()) {
            deleteReceivedMessages(queueUrl, messages.get(0));
            return messages.get(0);
        }


        return null;


    }


    public void deleteReceivedMessages(String queueUrl, Message message) {
        // Delete a message
        logger.info("Deleting a message.\n");

        String messageReceiptHandle = message.getReceiptHandle();
        this.amazonSQS.deleteMessage(new DeleteMessageRequest(queueUrl, messageReceiptHandle));


    }


    public List<String> listAllQueues() {
        logger.info("List all queue.\n");
        List<String> res = Lists.newArrayList();
        this.amazonSQS.listQueues().getQueueUrls().forEach(x -> res.add(x));
        return res;

    }


    public boolean ifExistedQueue(String queueName) {


        List<String> resQueueUrl = Lists.newArrayList();
        this.amazonSQS.listQueues().getQueueUrls().forEach(x -> {

            resQueueUrl.add(x.substring(x.lastIndexOf("/") + 1));

        });
        return resQueueUrl.contains(queueName + ".fifo");


    }


    public String getQueueUrls(String queueName) {


        return this.amazonSQS.getQueueUrl(queueName).getQueueUrl();


    }


}
