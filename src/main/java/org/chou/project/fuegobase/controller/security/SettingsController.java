package org.chou.project.fuegobase.controller.security;

import org.chou.project.fuegobase.data.GenericResponse;
import org.chou.project.fuegobase.data.database.DomainNameData;
import org.chou.project.fuegobase.service.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class SettingsController {

    private ProjectService projectService;

    @Autowired
    public SettingsController(ProjectService projectService) {
        this.projectService = projectService;
    }

    @PostMapping("/api/v1/databases/whitelist/{projectId}")
    public ResponseEntity<?> addDomainNameWhitelist(@PathVariable("projectId") String projectId,
                                                    @RequestBody DomainNameData domainNameData) {
        projectService.addDomainNameWhiteList(projectId, domainNameData);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/api/v1/databases/whitelist/{projectId}")
    public ResponseEntity<?> getDomainNameWhitelist(@PathVariable("projectId") String projectId) {
        return ResponseEntity.status(HttpStatus.OK).body(new GenericResponse<>(projectService.getDomainWhiteList(projectId)));
    }

    @DeleteMapping("/api/v1/databases/whitelist/{projectId}/domain/{domainNameId}")
    public ResponseEntity<?> deleteDomainNameWhitelist(@PathVariable("projectId") String projectId,
                                                       @PathVariable("domainNameId") long domainNameId) {
        projectService.deleteDomainName(projectId, domainNameId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @GetMapping("/api/v1/healthcheck")
    public String healthCheck() {
        return "ok";
    }

}
