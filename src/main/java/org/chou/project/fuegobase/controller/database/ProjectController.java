package org.chou.project.fuegobase.controller.database;

import org.chou.project.fuegobase.data.GenericResponse;
import org.chou.project.fuegobase.data.database.ProjectData;
import org.chou.project.fuegobase.error.ErrorResponse;
import org.chou.project.fuegobase.service.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.*;

import java.util.NoSuchElementException;

@RestController
@RequestMapping("/api/v1/databases/projects")
public class ProjectController {
    private final ProjectService projectService;
    private String API_KEY = "aaa12345bbb";

    @Autowired
    public ProjectController(ProjectService projectService) {
        this.projectService = projectService;
    }

    @PostMapping
    public ResponseEntity<?> createProject(@RequestBody ProjectData projectData) {
        try {
            projectService.createProject(projectData);
            return ResponseEntity.status(HttpStatus.CREATED).build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse(e.getMessage()));
        }
    }

    @GetMapping
    public ResponseEntity<?> getProjects(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorization) {

//        String token = authorization.split(" ")[1].trim();
        long userId = 1;
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new GenericResponse<>(projectService.getProjects(userId)));

    }

    @DeleteMapping("{projectId}")
    public ResponseEntity<?> deleteProject(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorization,
                                           @PathVariable("projectId") String projectId) {
        try{
            projectService.deleteProject(API_KEY, projectId);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }catch (NoSuchElementException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse("Project not found."));
        }

    }
}
