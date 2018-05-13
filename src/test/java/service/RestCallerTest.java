package service;

import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;
import data.BlockChainRequestEntity;
import org.junit.Test;

import java.io.IOException;
import java.util.List;

public class RestCallerTest {
    @Test
    public void getFromBc() throws Exception {
    }

    @Test
    public void postToBc() throws Exception {
        RestCaller restCaller = RestCaller.getInstance();
        BlockChainRequestEntity blockChainRequestEntity = new BlockChainRequestEntity();
        blockChainRequestEntity.setContent("sdfdsfsdsfd");
        blockChainRequestEntity.setMessageId("sdfddfd12");
        blockChainRequestEntity.setReceiver("rec1");
        blockChainRequestEntity.setSender("send1");
        System.out.println(restCaller.postToBc(blockChainRequestEntity));
    }

    @Test
    public void getFromBcTest() throws Exception {
        RestCaller restCaller = RestCaller.getInstance();

        System.out.println(restCaller.getFromBc());
    }


    @Test
    public void getFromBcTest1() throws Exception {
        RestCaller restCaller = RestCaller.getInstance();

        String str = restCaller.getFromBc();
        Gson g = new Gson();

        List<LinkedTreeMap> list = g.fromJson(str, List.class);
        list.forEach(x -> {

            System.out.println(x.get("messageId"));
            try {
                restCaller.delete(x.get("messageId").toString());
            } catch (IOException e) {
                e.printStackTrace();
            }

        });

    }

}