package org.chou.project.fuegobase.controller.database;

import org.chou.project.fuegobase.data.DatabaseRequestForm;
import org.chou.project.fuegobase.data.database.ProjectData;
import org.chou.project.fuegobase.service.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/databases/projects")
public class ProjectController {

    private final ProjectService projectService;

    @Autowired
    public ProjectController(ProjectService projectService) {
        this.projectService = projectService;
    }

//    @GetMapping("/database/form.html")
//    public String showForm(){
//        return "form";
//    }

//    @PostMapping(value = "/database/test")
//    @ResponseBody
//    public void test(@RequestBody MultiValueMap<String, String> file){
//        System.out.println(file);
//    }

    @PostMapping
    public void createProject(@RequestBody ProjectData projectData) {

        projectService.createProject(projectData);
    }
}
