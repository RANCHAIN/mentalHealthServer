package service;

import data.BlockChainRequestEntity;
import okhttp3.*;
import okio.BufferedSink;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import utils.Utils;

import javax.annotation.Nullable;
import java.io.IOException;
import java.util.concurrent.TimeUnit;


public class RestCaller {

    private static final Logger logger = LoggerFactory.getLogger(RestCaller.class);

    private static RestCaller single_instance = null;
    private OkHttpClient client;


    // private constructor restricted to this class itself
    private RestCaller() {
        this.client = new OkHttpClient.Builder()
                .connectTimeout(1000, TimeUnit.SECONDS)
                .writeTimeout(1000, TimeUnit.SECONDS)
                .readTimeout(3000, TimeUnit.SECONDS)
                .build();


    }

    // static method to create instance of Singleton class
    public static RestCaller getInstance() {
        if (single_instance == null)
            single_instance = new RestCaller();

        return single_instance;
    }


    @Async
    public String getFromBc() throws IOException {

        String url = "http://34.227.73.61/api/org.example.biznet.Message";
        logger.info("GET to: " + url);


        Request request = new Request.Builder()
                .url(url).get()
                .addHeader("Content-Type", "application/json")
                .addHeader("Accept", "application/json")
                .build();

        Response response = this.client.newCall(request).execute();
        return response.body().string();

    }


    @Async
    public boolean postToBc(BlockChainRequestEntity blockChainRequestEntity) throws IOException {


        String url = "http://34.227.73.61/api/org.example.biznet.Message";
        logger.info("POST to: " + url);


        Request request = new Request.Builder()
                .url(url)
                .post(new RequestBody() {
                    @Nullable
                    @Override
                    public MediaType contentType() {
                        return null;
                    }

                    @Override
                    public void writeTo(BufferedSink sink) throws IOException {
                        sink.writeUtf8(Utils.getBeautifiedJson.apply(blockChainRequestEntity));

                    }
                })
                .addHeader("Content-Type", "application/json")
                .addHeader("Accept", "application/json")
                .build();

        Response response = this.client.newCall(request).execute();

        return response.isSuccessful();

    }


    @Async
    public boolean delete(String msgId) throws IOException {

        String url = "http://34.227.73.61/api/org.example.biznet.Message/" + msgId;
        logger.info("POST to: " + url);


        Request request = new Request.Builder()
                .url(url)
                .delete()
                .addHeader("Accept", "application/json")
                .build();

        Response response = client.newCall(request).execute();

        return response.isSuccessful();

    }

}
