package org.chou.project.fuegobase.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;

@Slf4j
@Service
public class RedisService {
    private final RedisTemplate<String, String> redisTemplate;
    private final S3Service s3Service;

    public RedisService(RedisTemplate<String, String> redisTemplate, S3Service s3Service) {
        this.redisTemplate = redisTemplate;
        this.s3Service = s3Service;
    }

    public void addReadWriteNumber(String projectId, String fieldId, String action) {
        long timeMillis = System.currentTimeMillis(); // get current timeMillis
        LocalDateTime localDateTime = LocalDateTime.ofInstant(Instant.ofEpochMilli(timeMillis), ZoneId.systemDefault()); // local date time
        ZonedDateTime zonedSys = localDateTime.atZone(ZoneId.systemDefault()); // system default zone date time
        ZonedDateTime utcZone = zonedSys.withZoneSameInstant(ZoneId.of("UTC")); // convert to UTC

        String log = "/" + utcZone + "/" + projectId + "/" + action + "/" + fieldId + "/";

        redisTemplate.opsForList().leftPush("logs", log);
    }

    @Scheduled(fixedRate = 60 * 60 * 1000, initialDelay = 5 * 60 * 1000)
    public void getLogsFromRedis() {
        if (getLock()) {
            List<String> logs = redisTemplate.opsForList().range("logs", 0, -1);

            if (logs != null && !logs.isEmpty()) {
                StringBuilder logContent = new StringBuilder();
                for (String log : logs) {
                    logContent.append(log).append("\n");
                }
                try {
                    s3Service.uploadLogs(logContent);
                    redisTemplate.opsForList().trim("logs", 1, 0);
                } catch (Exception e) {
                    log.error("upload to S3 fail");
                }
            }
            unlock();
        }
    }

    public Boolean getLock() {
        try {
            long count = 0;
            Long i = redisTemplate.opsForValue().increment("lock", 1);
            if (i != null) {
                count = i;
            }
            log.info("get redis lock");
            return count == 1;
        } catch (Exception e) {
            log.error("lock exception : " + e.getMessage());
            redisTemplate.delete("lock");
            return false;
        }
    }

    public void unlock() {
        try {
            redisTemplate.delete("lock");
            log.info("redis unlock");
        } catch (Exception e) {
            log.error("unlock exception : " + e.getMessage());
            redisTemplate.delete("lock");
        }
    }
}

