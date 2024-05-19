package org.chou.project.fuegobase.controller.database;

import lombok.extern.slf4j.Slf4j;
import org.chou.project.fuegobase.data.GenericResponse;
import org.chou.project.fuegobase.data.database.ProjectData;
import org.chou.project.fuegobase.error.ErrorResponse;
import org.chou.project.fuegobase.exception.ResourceNotFoundException;
import org.chou.project.fuegobase.service.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/v1/databases/projects")
public class ProjectController {
    private final ProjectService projectService;

    @Autowired
    public ProjectController(ProjectService projectService) {
        this.projectService = projectService;
    }

    @PostMapping
    public ResponseEntity<?> createProject(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorization,
                                           @RequestBody ProjectData projectData) {
        String token = authorization.split(" ")[1].trim();
        try {
            projectService.createProject(projectData, token);
            return ResponseEntity.status(HttpStatus.CREATED).build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse(e.getMessage()));
        }
    }

    @GetMapping
    public ResponseEntity<?> getProjects(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorization) {
        String token = authorization.split(" ")[1].trim();
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new GenericResponse<>(projectService.getProjects(token)));
    }

    @DeleteMapping("/{projectId}")
    public ResponseEntity<?> deleteProject(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorization,
                                           @PathVariable("projectId") String projectId) {
        String token = authorization.split(" ")[1].trim();
        try {
            projectService.deleteProject(projectId, token);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse(e.getMessage()));
        }
    }

}
