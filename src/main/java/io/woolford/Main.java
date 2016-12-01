package io.woolford;

import com.lucidworks.spark.util.SolrSupport;
import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.neural.rnn.RNNCoreAnnotations;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.sentiment.SentimentCoreAnnotations;
import edu.stanford.nlp.trees.Tree;
import edu.stanford.nlp.util.CoreMap;
import org.apache.solr.common.SolrInputDocument;
import org.apache.spark.SparkConf;
import org.apache.spark.streaming.Duration;
import org.apache.spark.streaming.api.java.JavaDStream;
import org.apache.spark.streaming.api.java.JavaStreamingContext;
import org.apache.spark.streaming.twitter.TwitterUtils;
import twitter4j.Status;

import java.util.Properties;

public class Main {

    public static void main(String[] args) {

        SparkConf conf = new SparkConf();
        conf.setAppName("Streaming Tweets");
        conf.setMaster("local[*]");
        JavaStreamingContext ssc = new JavaStreamingContext(conf, new Duration(1000));

        TwitterUtils.createStream(ssc);

        // create a stream of tweets
        JavaDStream<Status> tweets = TwitterUtils.createStream(ssc);

        // filter for English language tweets
        JavaDStream<Status> enTweets = tweets.filter((status) -> "en".equalsIgnoreCase(status.getLang()));

        // filter for tweets with a known geolocation
        JavaDStream<Status> enGeoTweets = enTweets.filter((status) -> status.getGeoLocation() != null);

        // persist the tweets to Solr
        JavaDStream<SolrInputDocument> docs = enGeoTweets.map(tweet -> {
            SolrInputDocument doc = new SolrInputDocument();

            String text = tweet.getText();
            int sentiment = getSentiment(text);

            doc.setField("id", tweet.getId());
            doc.setField("createdAt", tweet.getCreatedAt());
            doc.setField("userId", tweet.getUser().getId());
            doc.setField("userScreenName", tweet.getUser().getScreenName());
            doc.setField("latitude", tweet.getGeoLocation().getLatitude());
            doc.setField("longitude", tweet.getGeoLocation().getLongitude());
            doc.setField("text", tweet.getText());
            doc.setField("lang", tweet.getLang());
            doc.setField("sentiment", sentiment);

            return doc;
        });

        SolrSupport.indexDStreamOfDocs("hadoop01.woolford.io:2181/solr", "tweets", 100, docs.dstream());

        ssc.start();
        ssc.awaitTermination();

    }

    private static int getSentiment(String text){
        Properties props = new Properties();
        props.setProperty("annotators", "tokenize, ssplit, pos, parse, sentiment");
        StanfordCoreNLP pipeline = new StanfordCoreNLP(props);
        Annotation annotation = pipeline.process(text);
        int longest = 0;
        int mainSentiment = 0;
        for (CoreMap sentence : annotation.get(CoreAnnotations.SentencesAnnotation.class)) {
            Tree tree = sentence.get(SentimentCoreAnnotations.SentimentAnnotatedTree.class);
            int sentiment = RNNCoreAnnotations.getPredictedClass(tree);
            String partText = sentence.toString();
            if (partText.length() > longest) {
                mainSentiment = sentiment;
                longest = partText.length();
            }
        }
        return mainSentiment;
    }

}
