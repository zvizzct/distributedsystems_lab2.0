package edu.upf.uploader;

import java.io.File;
import java.util.List;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.services.s3.model.AmazonS3Exception;

public class S3Uploader implements Uploader {
    private String bucketName;
    private String prefix;
    private AmazonS3 s3Client;

    /**
     * Constructor for S3Uploader
     * 
     * @param bucketName  the name of the S3 bucket to upload to
     * @param prefix      the prefix to add to the file name when creating the key
     *                    in the S3 bucket
     * @param profileName the name of the AWS profile to use (default is "default")
     */
    public S3Uploader(String bucketName, String prefix, String profileName) {
        this.bucketName = bucketName;
        this.prefix = prefix;

        // Retrieve the AWS credentials using the given profile name
        this.s3Client = AmazonS3ClientBuilder.standard()
                .withCredentials(new ProfileCredentialsProvider(profileName))
                .build();
    }

    @Override
    public void upload(List<String> files) {
        for (String file : files) {
            // Get the filename by extracting it from the full file path
            String filename = file.substring(file.lastIndexOf("/") + 1);

            // Create the key for the file in S3 by combining the prefix and the filename
            String key = prefix + "/" + filename;

            // Create a File object from the file path
            File f = new File(file);

            // Create a PutObjectRequest with the file's bucket name, key, and File object
            PutObjectRequest request = new PutObjectRequest(bucketName, key, f);

            try {
                // Upload the file to the S3 bucket
                s3Client.putObject(request);
            } catch (AmazonS3Exception e) {
                System.err.println("Error uploading file to S3: " + e.getMessage());
            } catch (Exception e) {
                System.err.println("Error uploading file: " + e.getMessage());
            }
        }
    }

}
