package org.chou.project.fuegobase.controller.dashboard;

import lombok.extern.slf4j.Slf4j;
import org.chou.project.fuegobase.data.GenericResponse;
import org.chou.project.fuegobase.error.ErrorResponse;
import org.chou.project.fuegobase.service.DashboardService;
import org.chou.project.fuegobase.service.s3.S3Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<?> getStorageData(@PathVariable("id") String projectId) {
        return ResponseEntity.status(HttpStatus.OK).body(new GenericResponse<>(dashboardService.getStorage(projectId)));
    }

    @GetMapping("/collections/{id}")
    public ResponseEntity<?> getCollectionCount(@PathVariable("id") String projectId) {
        return ResponseEntity.status(HttpStatus.OK).body(new GenericResponse<>(dashboardService.getCollectionCount(projectId)));
    }

    @GetMapping("/documents/{id}")
    public ResponseEntity<?> getDocumentCount(@PathVariable("id") String projectId) {
        return ResponseEntity.status(HttpStatus.OK).body(new GenericResponse<>(dashboardService.getDocumentCount(projectId)));
    }

    @GetMapping("/count/readwrite/{id}")
    public ResponseEntity<?> getReadWriteCounts(@PathVariable("id") String projectId,
                                                @RequestParam("startDate") String startDate,
                                                @RequestParam("endDate") String endDate) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(new GenericResponse<>(dashboardService.getLastWeekReadWriteCount(projectId, startDate, endDate)));
        } catch (Exception e) {
            log.info(e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse(e.getMessage()));
        }

    }


//    @GetMapping("/count/test1")
//    public void testGetReadLogFromS3() {
//        System.out.println("start" + System.currentTimeMillis());
//        s3Service.uploadLogs();
//        System.out.println("done" + System.currentTimeMillis());
//        System.out.println(LocalDate.now().minusDays(1).toString());
//        System.out.println(LocalDate.now().toString());
//    }


}
