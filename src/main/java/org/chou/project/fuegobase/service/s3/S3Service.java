package org.chou.project.fuegobase.service.s3;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import lombok.extern.slf4j.Slf4j;
import org.chou.project.fuegobase.repository.dashboard.ReadWriteLogRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.time.LocalDate;
import java.util.Map;

@Service
@Slf4j
public class S3Service {
    private static final String KEY_PREFIX = "logs/";
    private final AmazonS3 s3Client;
    @Value("${s3.access.key}")
    private String s3AccessKey;
    @Value("${s3.secret.key}")
    private String s3SecretKey;
    @Value("${s3.bucket.name}")
    private String bucketName;
    private ReadWriteLogRepository readWriteLogRepository;

    public S3Service(ReadWriteLogRepository readWriteLogRepository, AmazonS3 s3Client) {
        this.readWriteLogRepository = readWriteLogRepository;
        this.s3Client = s3Client;

    }

    @Scheduled(cron = "0 15 16 * * *", zone = "Asia/Taipei")
    public void uploadLogs() {
        try {
            BasicAWSCredentials awsCredentials =
                    new BasicAWSCredentials(s3AccessKey, s3SecretKey);
            AmazonS3 s3Client = AmazonS3ClientBuilder
                    .standard()
                    .withCredentials(new AWSStaticCredentialsProvider(awsCredentials))
                    .withRegion(Regions.US_EAST_1)
                    .build();

            File logsDir = new File("logs/");
            for (File logFile : logsDir.listFiles()) {
                String fileName = logFile.getName();
                String key = KEY_PREFIX + fileName;
                System.out.println(fileName);

                if (fileName.startsWith(LocalDate.now().minusDays(1).toString())) {
                    System.out.println("get");
                    s3Client.putObject(new PutObjectRequest(bucketName, key, logFile));
                }
            }
        } catch (Exception e) {
            log.error("Upload log fail: " + e.getMessage());
        }
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
