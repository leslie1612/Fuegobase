package org.chou.project.fuegobase.controller.dashboard;

import lombok.extern.slf4j.Slf4j;
import org.chou.project.fuegobase.data.GenericResponse;
import org.chou.project.fuegobase.data.dashboard.LogData;
import org.chou.project.fuegobase.service.DashboardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/v1/dashboard")
public class DashboardController {
    private final DashboardService dashboardService;

    @Autowired
    public DashboardController(DashboardService dashboardService) {
        this.dashboardService = dashboardService;
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

    @PostMapping("/count/readwrite/{id}")
    public ResponseEntity<?> getReadWriteCounts(@PathVariable("id") String projectId,
                                                @RequestBody LogData logData) {
        return ResponseEntity.status(HttpStatus.OK).body(new GenericResponse<>(dashboardService.getReadWriteCount(projectId, logData)));
    }

}
