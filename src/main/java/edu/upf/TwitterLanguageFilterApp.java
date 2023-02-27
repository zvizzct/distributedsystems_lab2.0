package edu.upf;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.apache.spark.api.java.JavaSparkContext;

import edu.upf.filter.SparkLanguageFilter;

public class TwitterLanguageFilterApp {
    public static void main(String[] args) {
        List<String> argsList = Arrays.asList(args);
        String language = argsList.get(0);
        String outputFile = argsList.get(1);
        JavaSparkContext sc = new JavaSparkContext();
        int index = 0;

        System.out.println("Language: " + language + ". Output file: " + outputFile);

        long start = System.currentTimeMillis();
        for (String inputFile : argsList.subList(3, argsList.size())) {

            System.out.println("Processing: " + inputFile);
            try {
                final SparkLanguageFilter filter = new SparkLanguageFilter(inputFile, outputFile, index, sc);
                filter.filterLanguage(language);
                index++;
            } catch (Exception e) {
                System.err.println("ERROR filtering the file: " + e.getMessage());
            }

        }
        // stop the JavaSparkContext at the end of the program
        sc.stop();

        long end = System.currentTimeMillis();
        System.out.println("\nTotal time taken for processing: " + (end - start) + "ms\n");

    }

}
