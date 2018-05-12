package service;

import cache.CacheService;
import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.DefaultAWSCredentialsProviderChain;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.comprehend.AmazonComprehend;
import com.amazonaws.services.comprehend.AmazonComprehendClientBuilder;
import com.amazonaws.services.comprehend.model.DetectKeyPhrasesRequest;
import com.amazonaws.services.comprehend.model.DetectKeyPhrasesResult;
import com.amazonaws.services.comprehend.model.DetectSentimentRequest;
import com.amazonaws.services.comprehend.model.DetectSentimentResult;
import data.Verdict;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.stream.Collectors;

public class NlpService {

    private static NlpService single_instance = null;
    private static final Logger logger = LoggerFactory.getLogger(NlpService.class);
    private AmazonComprehend comprehendClient;


    // private constructor restricted to this class itself
    private NlpService() {
        AWSCredentialsProvider awsCreds = DefaultAWSCredentialsProviderChain.getInstance();
        this.comprehendClient =
                AmazonComprehendClientBuilder.standard()
                        .withCredentials(awsCreds)
                        .withRegion(Regions.US_EAST_1)
                        .build();

    }

    // static method to create instance of Singleton class
    public static NlpService getInstance() {
        if (single_instance == null)
            single_instance = new NlpService();

        return single_instance;
    }

    public String[] getSentimental(String text) {
        DetectSentimentResult de = this.comprehendClient.detectSentiment(new DetectSentimentRequest().withText(text)
                .withLanguageCode("en"));

        String[] re = new String[2];
        re[0] = de.getSentiment();
        re[1] = de.getSentimentScore().toString();

        return re;

    }

    public List<String> getTopics(String text) {
        DetectKeyPhrasesResult pr = this.comprehendClient.detectKeyPhrases(new DetectKeyPhrasesRequest().withText(text)
                .withLanguageCode("en"));

        return pr.getKeyPhrases().parallelStream().map(x -> x.getText()).collect(Collectors.toList());


    }


    public Verdict getVerdictForThisUsername(String username) {

        List<String> res = CacheService.getInstance().getAllMsgsFromCacheForNLPByUser(username);
        List<String[]> verdicts = res.stream().map(x -> getSentimental(x)).collect(Collectors.toList());

        return new Verdict(verdicts.get(verdicts.size() - 1)[0], verdicts.get(verdicts.size() - 1)[1]);


    }

}


