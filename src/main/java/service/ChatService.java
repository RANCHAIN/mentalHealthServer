package service;

import cache.CacheService;
import com.amazonaws.services.sqs.model.Message;
import com.amazonaws.services.sqs.model.QueueDoesNotExistException;
import data.ChatSession;
import messageQueue.SqsSerivce;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@Component
public class ChatService {

    private static final Logger logger = LoggerFactory.getLogger(ChatService.class);
    private static ChatService single_instance = null;
    private SqsSerivce sqsSerivce;
    private CacheService cacheService;
    private BlockChainService blockChainService;


    // private constructor restricted to this class itself
    private ChatService() {
        this.sqsSerivce = SqsSerivce.getInstance();
        this.cacheService = CacheService.getInstance();
        this.blockChainService = BlockChainService.getInstance();

    }

    // static method to create instance of Singleton class
    public static ChatService getInstance() {
        if (single_instance == null)
            single_instance = new ChatService();

        return single_instance;
    }

    public String openNewChatSession(String fromUserId, String toUserId) throws ExecutionException, InterruptedException {

        String cacheKey = fromUserId + "#" + toUserId;
        if (this.cacheService.getValueAsList(cacheKey).isEmpty()) {
            this.cacheService.putInCache(cacheKey, "");
        }

        //     return this.sqsSerivce.createQueue(fromUserId + "TTTOOO" + toUserId);

        boolean isExisted = ifExistedChatSession(fromUserId, toUserId);
        if (isExisted) {
            return this.sqsSerivce.getQueueUrls(fromUserId + "TTTOOO" + toUserId);
        } else {
            return this.sqsSerivce.createQueue(fromUserId + "TTTOOO" + toUserId);
        }


    }

    public Boolean ifExistedChatSession(String fromUserId, String toUserId) throws ExecutionException, InterruptedException {
        String chatSessionQueue = fromUserId + "TTTOOO" + toUserId;


        CompletableFuture<Boolean> future = CompletableFuture.supplyAsync(() -> {

            return this.sqsSerivce.ifExistedQueue(chatSessionQueue);
        });
        return future.get();
    }

    public Boolean sendMessageToQueue(String queueUrl, String messageBody) throws ExecutionException, InterruptedException {

        String part = queueUrl.substring(queueUrl.lastIndexOf("/") + 1);
        part = part.replace("TTTOOO", "#");
        this.cacheService.putInCache(part, messageBody);

        CompletableFuture<Boolean> future = CompletableFuture.supplyAsync(() -> {

            return this.sqsSerivce.insertOneMessageToOneQueue(queueUrl, messageBody);
        });
        return future.get();

    }


    public void terminateOneSession(ChatSession chatSession) throws ExecutionException, InterruptedException {
        String fromUserId = chatSession.getFromId();
        String toUserId = chatSession.getToId();
        String queueName = fromUserId + "TTTOOO" + toUserId;
        this.sqsSerivce.deleteOneQueue(queueName);
        List<String> allMessages = this.cacheService.getValueAsList(fromUserId + "#" + toUserId);
        this.cacheService.clearKey(fromUserId + "#" + toUserId);
        if (!allMessages.isEmpty()) {
            this.blockChainService.saveMessages(allMessages, chatSession);
        }
    }

    public Message receiveMsgs(String queryUrl) throws ExecutionException, InterruptedException {
        Message res = this.sqsSerivce.receiveMessagesFromQueue(queryUrl);
        return res;
    }

    @Scheduled(fixedRate = 3000)
    public void getFilesFromQueue() {

        logger.info("getting logger queues");
        SqsSerivce sqsSerivce = SqsSerivce.getInstance();
        try {
            Message res = sqsSerivce.receiveMessagesFromQueue("https://sqs.us-east-1.amazonaws.com/183523990685/mentalQueue");
            if (res != null) {
                logger.info(res.getBody());

                CacheService.getInstance().putSingleElementInCache(res.getBody());
                CacheService.getInstance().putSingleElementInCacheForNLP(res.getBody());
            }
        } catch (QueueDoesNotExistException queueDoesNotExistException) {

            logger.info("this queue is empty");
        }

    }


}
