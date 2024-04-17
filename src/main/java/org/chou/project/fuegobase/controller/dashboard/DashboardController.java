package org.chou.project.fuegobase.controller.dashboard;

import lombok.extern.slf4j.Slf4j;
import org.chou.project.fuegobase.service.DashboardService;
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
    @Autowired
    public DashboardController(DashboardService dashboardService){
        this.dashboardService = dashboardService;
    }
    @GetMapping("/storage/{id}")
    public float getStorageData(@PathVariable("id") long projectId){
        return dashboardService.getStorage(projectId);
    }

    @GetMapping("/collections/{id}")
    public long getCollectionCount(@PathVariable("id") long projectId){
        return dashboardService.getCollectionCount(projectId);
    }

    @GetMapping("/documents/{id}")
    public long getDocumentCount(@PathVariable("id") long projectId){
        return dashboardService.getDocumentCount(projectId);
    }

}
