package controller;


import cache.CacheService;
import com.amazonaws.services.sqs.model.Message;
import com.google.common.collect.Lists;
import com.google.gson.Gson;
import data.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import messageQueue.SqsSerivce;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import service.ChatService;
import service.NlpService;
import utils.Utils;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.List;
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

        String receivedData = Utils.getBeautifiedJson.apply(conversationEntity);
        logger.info("receiveConversation of " + receivedData);
        SqsSerivce sqsSerivce = SqsSerivce.getInstance();
        sqsSerivce.insertOneMessageToOneQueue("https://sqs.us-east-1.amazonaws.com/183523990685/mentalQueue", receivedData);
        CacheService.getInstance().putSingleElementInCacheForNLP(receivedData);
        return new ResponseEntity<String>("ok", HttpStatus.OK);


    }

    @RequestMapping(value = "/wearable", method = RequestMethod.POST, consumes = "application/json")
    @ApiOperation(value = "wearable", response = ResponseEntity.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully"),
            @ApiResponse(code = 500, message = "Exception is encountered")
    })

    public ResponseEntity<String> wearable(

            @RequestBody WearbleRequestData wearbleRequestData) {

        String receivedData = Utils.getBeautifiedJson.apply(wearbleRequestData);
        logger.info("wearable of " + receivedData);

        return new ResponseEntity<String>("ok", HttpStatus.OK);

    }


    @RequestMapping(value = "/nlp/sentimental", method = RequestMethod.POST)
    @ApiOperation(value = "nlp sentimental", response = ResponseEntity.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully"),
            @ApiResponse(code = 500, message = "Exception is encountered")
    })

    public ResponseEntity<String[]> nlpSentimental(

            @RequestBody String text) {

        String receivedData = Utils.getBeautifiedJson.apply(text);
        logger.info("nlpSentimental of " + text);


        return new ResponseEntity<String[]>(NlpService.getInstance().getSentimental(text), HttpStatus.OK);

    }


    @RequestMapping(value = "/nlp/topics", method = RequestMethod.POST)
    @ApiOperation(value = "nlp topics", response = ResponseEntity.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully"),
            @ApiResponse(code = 500, message = "Exception is encountered")
    })

    public ResponseEntity<List<String>> nlpTopics(

            @RequestBody String text) {
        logger.info("topics of " + text);
        return new ResponseEntity<List<String>>(NlpService.getInstance().getTopics(text), HttpStatus.OK);

    }


    @RequestMapping(value = "/nlp/submit", method = RequestMethod.POST)
    @ApiOperation(value = "nlp submit", response = ResponseEntity.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully"),
            @ApiResponse(code = 500, message = "Exception is encountered")
    })
    public ResponseEntity<OrganizedScore> nlpSubmit(
            @RequestBody String username) {
        Verdict v = NlpService.getInstance().getVerdictForThisUsername(username);


        OrganizedScore organizedScore = new OrganizedScore();

        if (v.getState() != "") {

            organizedScore.setVerdict(v.getState());
            Gson g = new Gson();
            Score score = g.fromJson(v.getScore(), Score.class);
            organizedScore.setDepScore(score.getNegative().toString());
            CacheService.getInstance().putSingleElementInCacheForVerdicts(v);
        } else {
            organizedScore.setDepScore("");
            organizedScore.setVerdict("");
        }

        logger.info("organizedScore is " + Utils.getBeautifiedJson.apply(organizedScore));


        return new ResponseEntity<OrganizedScore>(organizedScore, HttpStatus.OK);
    }


    @RequestMapping(value = "/nlp/getAllVerdicts", method = RequestMethod.GET)
    @ApiOperation(value = "nlp getAllVerdicts", response = ResponseEntity.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully"),
            @ApiResponse(code = 500, message = "Exception is encountered")
    })
    public ResponseEntity<DataForPlotting> getAllVerdicts() {
        List<Verdict> cs = CacheService.getInstance().getAllVerdicts();
        List<OrganizedScore> res = Lists.newArrayList();
        for (Verdict v : cs) {

            OrganizedScore organizedScore = new OrganizedScore();

            if (v.getState() != "") {
                organizedScore.setVerdict(v.getState());
                Gson g = new Gson();
                Score score = g.fromJson(v.getScore(), Score.class);
                organizedScore.setDepScore(score.getNegative().toString());
            } else {
                organizedScore.setDepScore("");
                organizedScore.setVerdict("");
            }

            res.add(organizedScore);
        }

        DataForPlotting dataForPlotting = new DataForPlotting();

        List<Integer> seires = Lists.newArrayList();
        for (int i = 0; i < res.size(); i++) {
            seires.add(i + 1);
        }
        dataForPlotting.setX(seires);


        List<Double> doubleList = Lists.newArrayList();
        for (int i = 0; i < res.size(); i++) {
            doubleList.add(Double.valueOf(res.get(i).getDepScore()));
        }
        dataForPlotting.setY(doubleList);

        return new ResponseEntity<DataForPlotting>(dataForPlotting, HttpStatus.OK);
    }


    @RequestMapping(value = "/voice/upload", method = RequestMethod.POST)
    public @ResponseBody
    String handleFileUpload(
            @RequestParam("file") MultipartFile file) {
        String name = "test11";
        if (!file.isEmpty()) {
            try {
                byte[] bytes = file.getBytes();
                BufferedOutputStream stream =
                        new BufferedOutputStream(new FileOutputStream(new File(name + "-uploaded")));
                stream.write(bytes);
                stream.close();
                return "You successfully uploaded " + name + " into " + name + "-uploaded !";
            } catch (Exception e) {
                return "You failed to upload " + name + " => " + e.getMessage();
            }
        } else {
            return "You failed to upload " + name + " because the file was empty.";
        }
    }

}

