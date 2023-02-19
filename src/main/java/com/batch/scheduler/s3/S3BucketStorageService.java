package com.batch.scheduler.s3;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;


import java.io.*;
import java.util.ArrayList;
import java.util.List;

@Service
public class S3BucketStorageService {

    private Logger logger = LoggerFactory.getLogger(S3BucketStorageService.class);

    @Autowired
    @Qualifier("s3Client")
    private AmazonS3 amazonS3Client;

    @Value("${application.bucket.name}")
    private String bucketName;

    /**
     * Upload file into AWS S3
     *
     * @param keyName
     * @param file
     * @return String
     */

    /**
     * Deletes file from AWS S3 bucket
     *
     * @param fileName
     * @return
     */
    public String deleteFile(final String fileName) {
        amazonS3Client.deleteObject(bucketName, fileName);
        return "Deleted File: " + fileName;
    }


    /**
     * Downloads file using amazon S3 client from S3 bucket
     *
     * @param keyName
     * @return ByteArrayOutputStream
     */
    public ByteArrayOutputStream downloadFile(String keyName) {
        try {
            S3Object s3object = amazonS3Client.getObject(new GetObjectRequest(bucketName, keyName));

            InputStream is = s3object.getObjectContent();
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            int len;
            byte[] buffer = new byte[4096];
            while ((len = is.read(buffer, 0, buffer.length)) != -1) {
                outputStream.write(buffer, 0, len);
            }

            return outputStream;
        } catch (IOException ioException) {
            logger.error("IOException: " + ioException.getMessage());
        } catch (AmazonServiceException serviceException) {
            logger.info("AmazonServiceException Message:    " + serviceException.getMessage());
            throw serviceException;
        } catch (AmazonClientException clientException) {
            logger.info("AmazonClientException Message: " + clientException.getMessage());
            throw clientException;
        }

        return null;
    }

    /**
     * Get all files from S3 bucket
     *
     * @return
     */
    public List<String> downloadFiles() throws FileNotFoundException {

        ListObjectsRequest listObjectsRequest =
                new ListObjectsRequest()
                        .withBucketName(bucketName);

        List<String> keys = new ArrayList<>();

        ObjectListing objects = amazonS3Client.listObjects(listObjectsRequest);

        while (true) {
            List<S3ObjectSummary> objectSummaries = objects.getObjectSummaries();
            if (objectSummaries.size() < 1) {
                break;
            }

            for (S3ObjectSummary item : objectSummaries) {
                if (!item.getKey().endsWith("/")) {
                    keys.add(item.getKey());
                    try {

                        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                        outputStream = downloadFile(item.getKey());
                        byte[] bytes = outputStream.toByteArray();

                        FileOutputStream fileOutputStream = new FileOutputStream(item.getKey());
                        fileOutputStream.write(bytes);
                        fileOutputStream.close();
                    } catch (IOException ex) {
                        logger.info("AmazonServiceException Message:    " + ex.getMessage());
                    }
                }
            }

            objects = amazonS3Client.listNextBatchOfObjects(objects);
        }

        return keys;
    }

}
