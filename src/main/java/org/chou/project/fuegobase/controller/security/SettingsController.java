package org.chou.project.fuegobase.controller.security;

import lombok.extern.slf4j.Slf4j;
import org.chou.project.fuegobase.data.GenericResponse;
import org.chou.project.fuegobase.data.database.DomainNameData;
import org.chou.project.fuegobase.error.ErrorResponse;
import org.chou.project.fuegobase.service.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.NoSuchElementException;

@Slf4j
@RestController
public class SettingsController {

    private final ProjectService projectService;

    @Autowired
    public SettingsController(ProjectService projectService) {
        this.projectService = projectService;
    }

    @PostMapping("/api/v1/databases/whitelist/{projectId}")
    public ResponseEntity<?> addDomainNameWhitelist(@PathVariable("projectId") String projectId,
                                                    @RequestBody DomainNameData domainNameData) {
        try {
            projectService.addDomainNameWhiteList(projectId, domainNameData);
            return ResponseEntity.status(HttpStatus.CREATED).build();
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse("Project Not Found."));
        }

    }

    @GetMapping("/api/v1/databases/whitelist/{projectId}")
    public ResponseEntity<?> getDomainNameWhitelist(@PathVariable("projectId") String projectId) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(new GenericResponse<>(projectService.getDomainWhiteList(projectId)));
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse("Project Not Found."));
        }

    }

    @DeleteMapping("/api/v1/databases/whitelist/{projectId}/domain/{domainNameId}")
    public ResponseEntity<?> deleteDomainNameWhitelist(@PathVariable("projectId") String projectId,
                                                       @PathVariable("domainNameId") long domainNameId) {
        try {
            projectService.deleteDomainName(projectId, domainNameId);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse("Project Not Found."));
        }
    }

    @GetMapping("/api/v1/healthcheck")
    public String healthCheck() {
        try {
            String ip = InetAddress.getLocalHost().getHostAddress();
            return ip + " ok";
        } catch (UnknownHostException e) {
            log.error("unknownHost error : " + e.getMessage());
            return "ok";
        }
    }

}
