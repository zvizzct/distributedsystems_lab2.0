# Lab 2:  Spark Batch Applications on ElasticMapReduce - 2022/23 Edition

### Objectives
The objective of this lab is to create a Spark-based Java command line application to perform various data processing tasks on a collection of tweets in JSON format. The tasks include:

- Implementing a language filter similar to Lab 1, which filters tweets in a specified language and stores the filtered tweets in S3.
- Benchmarking the Spark-based TwitterFilter application on AWS ElasticMapReduce.
- Enhancing the SimplifiedTweet class to access additional fields.
- Finding the top-10 most popular bi-grams for a given language from the original tweets used during the 2018 Eurovision Festival.
- Finding the most retweeted tweet for the 10 most retweeted users.

### Usage
Build the app using `mvn package` and navigate to the `target` folder. Then, run the app using the jar file named `spark-test-1.0-SNAPSHOT` with the necessary parameters as described below:

##### TwitterFilter
The application takes the following parameters:

- [language]: a 2 character string representing a language in ISO 639-1 standard
- [output file]: the name of a local output file to temporarily store the filtered tweets
- [input file(s)]: one or more file paths to process the tweets from

Example: `spark-submit --master <YOUR MASTER> --class TwitterLanguageFilterApp spark-test-1.0-SNAPSHOT es /tmp/ Eurovision3.json Eurovision4.json Eurovision5.json`

##### BiGramsApp
The application takes the following parameters:

- [language]: a 2 character string representing a language in ISO 639-1 standard
- [output file]: the name of a local output file to store the top-10 most popular bi-grams
- [input file(s)]: one or more file paths to process the tweets from

Example: `spark-submit --master <YOUR MASTER> --class BiGramsApp spark-test-1.0-SNAPSHOT es /tmp/ Eurovision3.json Eurovision4.json Eurovision5.json`

### BiGramsApp output
The BiGramsApp output was generated after executing the program with Spanish language and using all the Eurovision files. The output consists of the top 9 most frequently occurring bigrams and their frequency of occurrence:
1. (#eurovision #finaleurovision,22545)
2. (en el,21448)
3. (en #eurovision,16049)
4. (que no,15545)
5. (en la,13135)
6. (el año,12610)
7. (lo que,12465)
8. (a la,11812)
9. (que el,11507)




### Dependencies
The lab uses two external libraries:

- GSON (version 2.2.4) developed by Google, one of the most used solutions for handling JSON content in Java.
- AWS-SDK for Amazon S3 (version 1.12.399) created by Amazon, to access S3 from Java code.
- JUnit (version 4.13.1) for testing.
- JSoup (version 1.15.3) for parsing HTML.
- Apache Spark (version 2.4.7 or later) for big data processing.


### Considerations
- Ensure that the necessary AWS credentials are setup before attempting to run the app. This can be done through environment variables or through the AWS CLI.
- The input files should be in JSON format.
- The output file should be in plain text format.
- The app may take some time to process large collections of files and upload the output file to S3, depending on the size and number of files processed.