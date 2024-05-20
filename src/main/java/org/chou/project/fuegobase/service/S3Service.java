package org.chou.project.fuegobase.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

@Service
@Slf4j
public class S3Service {
    private static final String KEY_PREFIX = "logs/";
    private final AmazonS3 s3Client;

    @Value("${s3.bucket.name}")
    private String bucketName;

    public S3Service(AmazonS3 s3Client) {
        this.s3Client = s3Client;
    }

    public void uploadLogs(StringBuilder logContent) throws UnknownHostException {

        Instant now = Instant.now();
        ZonedDateTime utcTime = now.atZone(ZoneId.of("UTC"));
        DateTimeFormatter formatter = DateTimeFormatter.ISO_ZONED_DATE_TIME;
        String formattedTime = utcTime.format(formatter);

        String ip = InetAddress.getLocalHost().getHostAddress();
        String key = KEY_PREFIX + "_" + formattedTime + ip + "_operationLogs.log";

        byte[] logBytes = logContent.toString().getBytes(StandardCharsets.UTF_8);
        InputStream inputStream = new ByteArrayInputStream(logBytes);
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(logBytes.length);
        metadata.setContentType("text/plain");

        s3Client.putObject(new PutObjectRequest(bucketName, key, inputStream, metadata));

    }

}
