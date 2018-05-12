package service;

import okhttp3.*;
import okio.BufferedSink;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.scheduling.annotation.Async;

import javax.annotation.Nullable;
import java.io.IOException;
import java.util.concurrent.TimeUnit;


public class RestCaller {

    private static final Logger logger = LoggerFactory.getLogger(RestCaller.class);


    @Async
    @Cacheable("schemas")
    public String okCall(String formular) throws IOException {

        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(1000, TimeUnit.SECONDS)
                .writeTimeout(1000, TimeUnit.SECONDS)
                .readTimeout(3000, TimeUnit.SECONDS)
                .build();


        // "http://10.65.9.221/snapshot/rest/eds/v1/select?uuid=PAXTRA27775&formula=Test_GET_ESG_Basic(20140214%2C%2020170214)&identifiers=IBM.N%40RIC&productid=CPVIEWS"
        String url = "http://10.65.9.221/snapshot/rest/eds/v1/select?uuid=PAXTRA27775&formula=" + formular + "&identifiers=IBM.N%40RIC&productid=CPVIEWS";
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

                    }
                })
                .addHeader("reutersuuid", "PAXTRA27775")
                .build();

        Response response = client.newCall(request).execute();
        return response.body().string();


    }

}
