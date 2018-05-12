package service;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.comprehend.AmazonComprehend;
import com.amazonaws.services.comprehend.AmazonComprehendClientBuilder;
import com.amazonaws.services.comprehend.model.DetectKeyPhrasesRequest;
import com.amazonaws.services.comprehend.model.DetectKeyPhrasesResult;
import org.junit.Test;

public class NlpService {

    @Test
    public void test() {

        String text = "James: today i am sad. I want to shit because of my study.";

        // Create credentials using a provider chain. For more information, see
        // https://docs.aws.amazon.com/sdk-for-java/v1/developer-guide/credentials.html


        AmazonComprehend comprehendClient =
                AmazonComprehendClientBuilder.standard()
                        .withCredentials(new AWSStaticCredentialsProvider(new BasicAWSCredentials("AKIAJKNHOSBN2K3FSPOQ", "hVbLjYLn5pRCm8MXvUsRkkeBhr2r0CxEtEUYQVcS")))
                        .withRegion(Regions.US_EAST_1)
                        .build();

        // Call detectSentiment API
//        System.out.println("Calling DetectSentiment");
//        DetectSentimentRequest detectSentimentRequest = new DetectSentimentRequest().withText(text)
//                .withLanguageCode("en");
//        DetectSentimentResult detectSentimentResult = comprehendClient.detectSentiment(detectSentimentRequest);
//        System.out.println(detectSentimentResult);
//        System.out.println("End of DetectSentiment\n");
//        System.out.println("Done");
//
//
        DetectKeyPhrasesRequest detectKeyPhrasesRequest = new DetectKeyPhrasesRequest().withText(text)
                .withLanguageCode("en");
        DetectKeyPhrasesResult detectKeyPhrasesResult = comprehendClient.detectKeyPhrases(detectKeyPhrasesRequest);
        detectKeyPhrasesResult.getKeyPhrases().forEach(System.out::println);
        System.out.println("End of DetectKeyPhrases\n");


//        System.out.println("Calling DetectEntities");
//        DetectEntitiesRequest detectEntitiesRequest = new DetectEntitiesRequest().withText(text)
//                .withLanguageCode("en");
//        DetectEntitiesResult detectEntitiesResult = comprehendClient.detectEntities(detectEntitiesRequest);
//        detectEntitiesResult.getEntities().forEach(System.out::println);
//        System.out.println("End of DetectEntities\n");


    }
}
