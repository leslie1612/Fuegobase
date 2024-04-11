package org.chou.project.fuegobase.service.impl;

import org.chou.project.fuegobase.data.database.ProjectData;
import org.chou.project.fuegobase.model.database.Project;
import org.chou.project.fuegobase.repository.database.ProjectRepository;
import org.chou.project.fuegobase.service.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProjectServiceImpl implements ProjectService {

    private final ProjectRepository projectRepository;

    @Autowired
    public ProjectServiceImpl(ProjectRepository projectRepository) {
        this.projectRepository = projectRepository;
    }

    @Override
    public void createProject(ProjectData projectData) {
        String APIKey = "aaa12345bbb";

        Project project = new Project();

        project.setName(projectData.getName());
        project.setUserId(Long.parseLong(projectData.getUserId()));
        project.setAPIKey(APIKey);

        projectRepository.save(project);
    }
}
