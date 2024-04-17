package org.chou.project.fuegobase.controller.dashboard;

import com.amazonaws.services.s3.AmazonS3;
import lombok.extern.slf4j.Slf4j;
import org.chou.project.fuegobase.model.dashboard.ReadWriteLog;
import org.chou.project.fuegobase.service.DashboardService;
import org.chou.project.fuegobase.service.s3.S3Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

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

    @GetMapping("/count/readwrite/{id}")
    public List<ReadWriteLog> getReadCount(@PathVariable("id") long projectId){
        return dashboardService.getLastWeekReadWriteCount(projectId);
    }


    @GetMapping("/count/test1")
    public void testGetReadLogFromS3() {
        AmazonS3 s3Client = s3Service.createS3Client();
        s3Service.storeReadLogsIntoDB(s3Client);

    }

    @GetMapping("/count/test2")
    public void testGetWriteLogFromS3(){
        AmazonS3 s3Client = s3Service.createS3Client();
        s3Service.storeWriteLogsIntoDB(s3Client);
    }

}
