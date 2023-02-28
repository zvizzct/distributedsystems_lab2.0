package spark;

import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import scala.Tuple2;

import java.util.ArrayList;
import java.util.List;

public class MostRetweetedApp {

    public static void main(String[] args) {
        // Initialize Spark context
        JavaSparkContext sc = new JavaSparkContext(new SparkConf().setAppName("MostRetweetedApp"));

        // Read the input file and convert to RDD of ExtendedSimplifiedTweet objects
        JavaRDD<String> lines = sc.textFile(args[1]);
        JavaRDD<ExtendedSimplifiedTweet> tweets = lines.map(line -> {
            Optional<ExtendedSimplifiedTweet> tweet = ExtendedSimplifiedTweet.fromJson(line);
            if (tweet.isPresent()) {
                return tweet.get();
            } else {
                return null;
            }
        }).filter(tweet -> tweet != null);

        // Create a pair RDD where the key is the retweetedUserId and the value is the
        // tweetId
        JavaPairRDD<Long, Long> userTweetPairs = tweets
                .mapToPair(tweet -> new Tuple2<>(tweet.getRetweetedUserId(), tweet.getTweetId()));

        // Group all the tweets for a particular user and find the tweet with the
        // maximum tweetId
        JavaPairRDD<Long, Long> mostRetweetedTweets = userTweetPairs.groupByKey().mapValues(tweetIds -> {
            long maxTweetId = 0;
            for (long tweetId : tweetIds) {
                maxTweetId = Math.max(maxTweetId, tweetId);
            }
            return maxTweetId;
        });

        // Join the RDD of tweets with the RDD of mostRetweetedTweets to find the tweet
        // text
        JavaPairRDD<Long, Tuple2<ExtendedSimplifiedTweet, Long>> joinedRDD = tweets
                .mapToPair(tweet -> new Tuple2<>(tweet.getRetweetedUserId(), tweet))
                .join(mostRetweetedTweets);

        // Sort the joined RDD based on the number of followers and take the first 10
        // elements
        // Sort the joined RDD based on the number of followers and take the first 10
        // elements
        List<Tuple2<Long, Tuple2<ExtendedSimplifiedTweet, Long>>> top10Users = joinedRDD.mapToPair(tuple -> {
            ExtendedSimplifiedTweet tweet = tuple._2._1;
            return new Tuple2<>(tweet.getFollowersCount(), tuple);
        }).sortByKey(false).take(10);

        // Find the tweet with the maximum tweetId for each of the top 10 users
        List<ExtendedSimplifiedTweet> result = new ArrayList<>();
        for (Tuple2<Long, Tuple2<ExtendedSimplifiedTweet, Long>> user : top10Users) {
            Tuple2<ExtendedSimplifiedTweet, Long> tweet = user._2;
            if (tweet._1.getTweetId() == tweet._2) {
                result.add(tweet._1);
            }
        }

        // Save the result to the output file
        sc.parallelize(result).saveAsTextFile(args[0]);

        // Stop the Spark context
        sc.stop();
    }
}
