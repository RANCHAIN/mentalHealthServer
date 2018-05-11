package controller;


import com.amazonaws.services.sqs.model.Message;
import data.ChatMessage;
import data.ChatSession;
import data.ConversationEntity;
import data.ReceiveMessageRequestEntity;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import service.ChatService;
import utils.Utils;

import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping(value = "/chat")
@Api(value = "chat_service", description = "Chat Restful controller service")
public class ChatControllerImpl {

    private static final Logger logger = LoggerFactory.getLogger(ChatControllerImpl.class);


    @RequestMapping(value = "/echo", method = RequestMethod.GET)
    @ApiOperation(value = "Simply testing the service", response = ResponseEntity.class)
    public ResponseEntity<String> test() {
        return new ResponseEntity<>("testing the restful controller of chat", HttpStatus.OK);

    }


    @RequestMapping(value = "/openSession", method = RequestMethod.POST, consumes = "application/json")
    @ApiOperation(value = "open messsging session", response = ResponseEntity.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully"),
            @ApiResponse(code = 500, message = "Exception is encountered")
    })
    public ResponseEntity<String> openSession(

            @RequestHeader("Content-Type") String contentType,
            // expecting:  application/json; charset=utf-8
            @RequestBody ChatSession chatSession) {


        try {
            logger.info("received the chart session of " + Utils.getBeautifiedJson.apply(chatSession));
            String res = ChatService.getInstance().openNewChatSession(chatSession.getFromId(), chatSession.getToId());
            logger.info("getting the url for this queue: " + res);
            return new ResponseEntity<String>(res, HttpStatus.OK);
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return new ResponseEntity<String>("error", HttpStatus.INTERNAL_SERVER_ERROR);


    }


    @RequestMapping(value = "/sendMessage", method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
    @ApiOperation(value = "send Message", response = ResponseEntity.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully"),
            @ApiResponse(code = 500, message = "Exception is encountered")
    })

    public ResponseEntity<String> sendMessage(

            @RequestHeader("Content-Type") String contentType,
            // expecting:  application/json; charset=utf-8
            @RequestBody ChatMessage chatMessage) {


        try {
            logger.info("received the chart message of " + Utils.getBeautifiedJson.apply(chatMessage));
            boolean res = ChatService.getInstance().sendMessageToQueue(chatMessage.getSessionUrl(), chatMessage.getMessageBody());
            logger.info("response of sending this message is " + res);
            if (res) {
                return new ResponseEntity<String>("true", HttpStatus.OK);
            }

        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return new ResponseEntity<String>("error", HttpStatus.INTERNAL_SERVER_ERROR);


    }


    @RequestMapping(value = "/closeSession", method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
    @ApiOperation(value = "close Session", response = ResponseEntity.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully"),
            @ApiResponse(code = 500, message = "Exception is encountered")
    })

    public ResponseEntity<String> closeSession(

            @RequestHeader("Content-Type") String contentType,
            // expecting:  application/json; charset=utf-8
            @RequestBody ChatSession chatSession) {


        try {
            logger.info("received the chart session of " + Utils.getBeautifiedJson.apply(chatSession));
            ChatService.getInstance().terminateOneSession(chatSession);

            return new ResponseEntity<String>("true", HttpStatus.OK);
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return new ResponseEntity<String>("error", HttpStatus.INTERNAL_SERVER_ERROR);


    }


    @RequestMapping(value = "/receiveMessages", method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
    @ApiOperation(value = "close Session", response = ResponseEntity.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully"),
            @ApiResponse(code = 500, message = "Exception is encountered")
    })

    public ResponseEntity<String> receiveMessages(

            @RequestHeader("Content-Type") String contentType,
            // expecting:  application/json; charset=utf-8
            @RequestBody ReceiveMessageRequestEntity receiveMessageRequestEntity) {


        try {
            logger.info("receiveMessages " + Utils.getBeautifiedJson.apply(receiveMessageRequestEntity));
            Message msgs = ChatService.getInstance().receiveMsgs(receiveMessageRequestEntity.getQueryUrl());
            String res = msgs.getBody();
            return new ResponseEntity<String>(Utils.getBeautifiedJson.apply(res), HttpStatus.OK);
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (Exception e) {

        }

        return new ResponseEntity<String>("", HttpStatus.OK);


    }


    @RequestMapping(value = "/receiveConversation", method = RequestMethod.POST, consumes = "application/json")
    @ApiOperation(value = "receiveConversation", response = ResponseEntity.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully"),
            @ApiResponse(code = 500, message = "Exception is encountered")
    })

    public ResponseEntity<String> receiveConversation(

            @RequestHeader("Content-Type") String contentType,
            // expecting:  application/json; charset=utf-8
            @RequestBody ConversationEntity conversationEntity) {

        logger.info("receiveConversation of " + Utils.getBeautifiedJson.apply(conversationEntity));
        return new ResponseEntity<String>("ok", HttpStatus.OK);


    }


}

