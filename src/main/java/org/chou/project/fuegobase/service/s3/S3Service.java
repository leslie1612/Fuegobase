package org.chou.project.fuegobase.service.s3;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.*;
import lombok.extern.slf4j.Slf4j;
import org.chou.project.fuegobase.model.dashboard.ReadWriteLog;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.*;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class S3Service {
    @Value("${s3.access.key}")
    private String s3AccessKey;

    @Value("${s3.secret.key}")
    private String s3SecretKey;

    @Value("${s3.bucket.name}")
    private String bucketName;
    private static final String KEY_PREFIX = "logs/";

    public AmazonS3 createS3Client() {
        BasicAWSCredentials awsCredentials =
                new BasicAWSCredentials(s3AccessKey, s3SecretKey);
        AWSCredentials credentials = awsCredentials;
        AmazonS3 s3Client = AmazonS3ClientBuilder
                .standard()
                .withCredentials(new AWSStaticCredentialsProvider(credentials))
                .withRegion(Regions.US_EAST_1)
                .build();

        return s3Client;
    }

    public void uploadLogs(AmazonS3 s3Client, String projectId, String action, Map<String, String> logs) {
        try {
            // Convert logs to byte array
            ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
            ObjectOutputStream objectStream = new ObjectOutputStream(byteStream);
            objectStream.writeObject(logs);
            objectStream.close();
            byte[] bytes = byteStream.toByteArray();

            // upload to S3
            String key = KEY_PREFIX + LocalDate.now() + "/" + projectId + "/" + action + "/" + System.currentTimeMillis() + ".log";
            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentLength(bytes.length);
            s3Client.putObject(new PutObjectRequest(bucketName, key, new ByteArrayInputStream(bytes), metadata));
        } catch (IOException e) {
            log.error("Upload log fail: " + e.getMessage());
        }
    }

    public void getLogs(AmazonS3 s3Client, String projectId, String action, String date) {
        ListObjectsV2Request request = new ListObjectsV2Request()
                .withBucketName(bucketName)
                .withPrefix(KEY_PREFIX + date + "/" + projectId + "/" + action);

        ListObjectsV2Result result = s3Client.listObjectsV2(request);
        System.out.println(result.getObjectSummaries().size());
    }

    public void downloadAndDeserializeLogs(AmazonS3 s3Client, String key) {
        try {
            // 下載物件
            S3Object s3Object = s3Client.getObject(new GetObjectRequest(bucketName, key));

            // 讀取物件內容
            InputStream inputStream = s3Object.getObjectContent();
            ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);

            // 還原成 Map<String, String> 格式
            Map<String, String> logs = (Map<String, String>) objectInputStream.readObject();

            // 關閉串流
            objectInputStream.close();

            System.out.println(logs);
        } catch (IOException | ClassNotFoundException e) {
            log.error("Download and deserialize logs fail: " + e.getMessage());

        }
    }


}
