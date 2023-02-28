package edu.upf;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;

import edu.upf.filter.SparkLanguageFilter;

public class TwitterLanguageFilterApp {
    public static void main(String[] args) {
        List<String> argsList = Arrays.asList(args);
        String language = argsList.get(0);
        String outputFile = argsList.get(1);
        // String inputPath = args[2];
        String inputPaths = String.join(",", Arrays.copyOfRange(args, 2, args.length));

        // Create the Spark configuration and context
        SparkConf conf = new SparkConf().setAppName("TwitterLanguageFilterApp");
        JavaSparkContext sc = new JavaSparkContext(conf);

        // Read the tweets from the input path

        System.out.println("Language: " + language + ". Output file: " + outputFile);

        long start = System.currentTimeMillis();

        try {
            final SparkLanguageFilter filter = new SparkLanguageFilter(inputPaths, outputFile, sc);
            filter.filterLanguage(language);
        } catch (Exception e) {
            System.err.println("ERROR filtering the file: " + e.getMessage());
        }

        // stop the JavaSparkContext at the end of the program
        sc.stop();

        long end = System.currentTimeMillis();
        System.out.println("\nTotal time taken for processing: " + (end - start) + "ms\n");

    }

}
