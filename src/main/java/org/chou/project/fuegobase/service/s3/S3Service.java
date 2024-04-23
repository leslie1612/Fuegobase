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
import org.chou.project.fuegobase.repository.dashboard.ReadWriteLogRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.*;
import java.time.LocalDate;
import java.util.*;

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

    private ReadWriteLogRepository readWriteLogRepository;

    public S3Service(ReadWriteLogRepository readWriteLogRepository) {
        this.readWriteLogRepository = readWriteLogRepository;
    }

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
            String key = KEY_PREFIX + LocalDate.now() + "/" + action + "/" + projectId + "/" + System.currentTimeMillis() + ".log";
            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentLength(bytes.length);
            s3Client.putObject(new PutObjectRequest(bucketName, key, new ByteArrayInputStream(bytes), metadata));
        } catch (IOException e) {
            log.error("Upload log fail: " + e.getMessage());
        }
    }

    public Map<String, Integer> storeLogsIntoDB(AmazonS3 s3Client, String action) {
        ListObjectsV2Request request = new ListObjectsV2Request()
                .withBucketName(bucketName)
                .withPrefix(KEY_PREFIX + LocalDate.now().minusDays(1) + "/" + action);

        ListObjectsV2Result result = s3Client.listObjectsV2(request);
        Map<String, Integer> idCounts = new HashMap<>();

        for (S3ObjectSummary objectSummary : result.getObjectSummaries()) {
            String[] logInfo = objectSummary.getKey().split("/");

            if (logInfo.length >= 5) {
                String logProjectId = logInfo[3];
                idCounts.merge(logProjectId, 1, Integer::sum);
            }
        }
        return idCounts;
    }

    @Scheduled(cron = "0 10 0 * * *", zone = "Asia/Taipei")
    public void storeReadLogsIntoDB() {
        BasicAWSCredentials awsCredentials =
                new BasicAWSCredentials(s3AccessKey, s3SecretKey);
        AmazonS3 s3Client = AmazonS3ClientBuilder
                .standard()
                .withCredentials(new AWSStaticCredentialsProvider(awsCredentials))
                .withRegion(Regions.US_EAST_1)
                .build();
        Map<String, Integer> readCounts = storeLogsIntoDB(s3Client, "read");
        readCounts.forEach((projectId, count) -> {
            ReadWriteLog readWriteLog = new ReadWriteLog();
            readWriteLog.setProjectId(Long.parseLong(projectId));
            readWriteLog.setReadCount(count);
            readWriteLog.setDate(LocalDate.now().minusDays(1));

            readWriteLogRepository.save(readWriteLog);
        });
    }

    @Scheduled(cron = "0 10 1 * * *", zone = "Asia/Taipei")
    public void storeWriteLogsIntoDB() {
        BasicAWSCredentials awsCredentials =
                new BasicAWSCredentials(s3AccessKey, s3SecretKey);
        AmazonS3 s3Client = AmazonS3ClientBuilder
                .standard()
                .withCredentials(new AWSStaticCredentialsProvider(awsCredentials))
                .withRegion(Regions.US_EAST_1)
                .build();
        Map<String, Integer> readCounts = storeLogsIntoDB(s3Client, "write");

        readCounts.forEach((projectId, count) -> {

            ReadWriteLog existingRecord = readWriteLogRepository
                    .findReadWriteLogByProjectIdAndDate(Long.parseLong(projectId), LocalDate.now().minusDays(1));
            if (existingRecord != null) {
                existingRecord.setWriteCount(count);
                readWriteLogRepository.save(existingRecord);
            } else {
                ReadWriteLog readWriteLog = new ReadWriteLog();
                readWriteLog.setProjectId(Long.parseLong(projectId));
                readWriteLog.setWriteCount(count);
                readWriteLog.setDate(LocalDate.now().minusDays(1));

                readWriteLogRepository.save(readWriteLog);
            }
        });
    }

    public void downloadAndDeserializeLogs(AmazonS3 s3Client, String key) {
        try {
            S3Object s3Object = s3Client.getObject(new GetObjectRequest(bucketName, key));

            InputStream inputStream = s3Object.getObjectContent();
            ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);

            Map<String, String> logs = (Map<String, String>) objectInputStream.readObject();

            objectInputStream.close();

            System.out.println(logs);
        } catch (IOException | ClassNotFoundException e) {
            log.error("Download and deserialize logs fail: " + e.getMessage());
        }
    }


}
