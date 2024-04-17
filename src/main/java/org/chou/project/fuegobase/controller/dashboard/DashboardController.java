package org.chou.project.fuegobase.controller.dashboard;

import com.amazonaws.services.s3.AmazonS3;
import lombok.extern.slf4j.Slf4j;
import org.chou.project.fuegobase.service.DashboardService;
import org.chou.project.fuegobase.service.s3.S3Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/v1/dashboard")
public class DashboardController {
    private final DashboardService dashboardService;
    private final S3Service s3Service;

    @Autowired
    public DashboardController(DashboardService dashboardService, S3Service s3Service) {
        this.dashboardService = dashboardService;
        this.s3Service = s3Service;
    }

    @GetMapping("/storage/{id}")
    public float getStorageData(@PathVariable("id") long projectId) {
        return dashboardService.getStorage(projectId);
    }

    @GetMapping("/collections/{id}")
    public long getCollectionCount(@PathVariable("id") long projectId) {
        return dashboardService.getCollectionCount(projectId);
    }

    @GetMapping("/documents/{id}")
    public long getDocumentCount(@PathVariable("id") long projectId) {
        return dashboardService.getDocumentCount(projectId);
    }

    @GetMapping("/count/read/{id}")
    public void getReadCount(@PathVariable("id") String projectId) {
        AmazonS3 s3Client = s3Service.createS3Client();
        s3Service.getLogs(s3Client, projectId, "read", "2024-04-17");
        s3Service.downloadAndDeserializeLogs(s3Client,"logs/2024-04-17/7/read/1713356633514.log");
    }


}
