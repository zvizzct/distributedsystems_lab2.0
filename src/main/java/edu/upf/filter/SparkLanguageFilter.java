package edu.upf.filter;

import java.util.Optional;

import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;

import edu.upf.model.SimplifiedTweet;

/**
 * A class that implements the LanguageFilter interface.
 * This class filters the tweets based on the specified language using Apache
 * Spark.
 */
public class SparkLanguageFilter implements LanguageFilter {

    // The input file path
    private final String inputPaths;
    // The output file path
    private final String outputFile;
    // The JavaSparkContext instance
    private final JavaSparkContext sc;
    // The index of the output file

    /**
     * Constructor that takes the input file path, output directory path, and
     * output index as parameters.
     * 
     * @param inputPaths The path of the input file.
     * @param outputFile The path of the output directory.
     * @param sc         The JavaSparkContext instance.
     */
    public SparkLanguageFilter(String inputPaths, String outputFile, JavaSparkContext sc) {
        this.inputPaths = inputPaths;
        this.outputFile = outputFile;
        this.sc = sc;
    }

    /**
     * The implementation of the filterLanguage method from the LanguageFilter
     * interface.
     * This method filters the tweets based on the specified language using Apache
     * Spark.
     * 
     * @param language The language to filter the tweets by.
     */
    @Override
    public void filterLanguage(String language) {
        try {
            JavaRDD<String> tweets = sc.textFile(inputPaths);

            JavaRDD<String> filteredRDD = tweets.filter(tweetJson -> {
                Optional<SimplifiedTweet> tweet = SimplifiedTweet.fromJson(tweetJson);
                return tweet.isPresent() && tweet.get().getLanguage().equals(language);
            });
            filteredRDD.saveAsTextFile(outputFile + "/" + language + "/");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
