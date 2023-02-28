package spark;

import edu.upf.model.ExtendedSimplifiedTweet;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import scala.Tuple2;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class BiGramsApp {

    public static void main(String[] args) {
        // Parse the parameters
        String language = args[0];
        String outputPath = args[1];
        // String inputPath = args[2];
        String inputPaths = String.join(",", Arrays.copyOfRange(args, 2, args.length));

        // Create the Spark configuration and context
        SparkConf conf = new SparkConf().setAppName("BiGramsApp");
        JavaSparkContext sc = new JavaSparkContext(conf);

        // Read the tweets from the input path
        JavaRDD<String> tweets = sc.textFile(inputPaths);

        // Filter the tweets based on the language
        JavaRDD<String> filteredTweets = tweets.filter(tweet -> {
            Optional<ExtendedSimplifiedTweet> simplifiedTweet = ExtendedSimplifiedTweet.fromJson(tweet);
            return simplifiedTweet.isPresent() && simplifiedTweet.get().getLanguage().equals(language);
        });

        // Normalize the text of each tweet
        JavaRDD<String> normalizedTweets = filteredTweets.map(tweet -> {
            Optional<ExtendedSimplifiedTweet> simplifiedTweet = ExtendedSimplifiedTweet.fromJson(tweet);
            String text = simplifiedTweet.get().getText();
            text = text.trim().toLowerCase();
            return text;
        });

        // Create the bigrams
        JavaPairRDD<String, Integer> bigrams = normalizedTweets.flatMapToPair(tweet -> {
            List<String> words = Arrays.asList(tweet.split("\\s+"));
            int wordCount = words.size();
            List<Tuple2<String, Integer>> bigramList = new ArrayList<>();
            for (int i = 0; i < wordCount - 1; i++) {
                String bigram = words.get(i) + " " + words.get(i + 1);
                bigramList.add(new Tuple2<>(bigram, 1));
            }
            return bigramList.iterator();
        }).reduceByKey((a, b) -> a + b);

        // Sort the bigrams based on their count and select the top 10
        List<Tuple2<String, Integer>> topBigrams = bigrams.mapToPair(Tuple2::swap)
                .sortByKey(false)
                .mapToPair(Tuple2::swap)
                .take(10);

        // Write the results to the output path
        sc.parallelize(topBigrams).saveAsTextFile(outputPath);

        // Stop the Spark context
        sc.stop();
    }
}
