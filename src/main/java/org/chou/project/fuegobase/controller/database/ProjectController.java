package org.chou.project.fuegobase.controller.database;

import org.chou.project.fuegobase.data.GenericResponse;
import org.chou.project.fuegobase.data.database.ProjectData;
import org.chou.project.fuegobase.service.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/databases/projects")
public class ProjectController {
    private final ProjectService projectService;
    @Autowired
    public ProjectController(ProjectService projectService) {
        this.projectService = projectService;
    }

    @PostMapping
    public ResponseEntity<?> createProject(@RequestBody ProjectData projectData) {
        projectService.createProject(projectData);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @GetMapping
    public ResponseEntity<?> getProjects(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorization){

//        String token = authorization.split(" ")[1].trim();
        long userId = 1;
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new GenericResponse<>(projectService.getProjects(userId)) );

    }
}
