package org.chou.project.fuegobase.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.chou.project.fuegobase.data.database.ProjectData;
import org.chou.project.fuegobase.model.database.Project;
import org.chou.project.fuegobase.repository.database.ProjectRepository;
import org.chou.project.fuegobase.service.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class ProjectServiceImpl implements ProjectService {

    private final ProjectRepository projectRepository;

    @Autowired
    public ProjectServiceImpl(ProjectRepository projectRepository) {
        this.projectRepository = projectRepository;
    }

    @Override
    public void createProject(ProjectData projectData) {
        // TODO API key 如何產生
        String APIKey = "aaa12345bbb";

        if(projectRepository.existsByName(projectData.getName())){
            throw new IllegalArgumentException("Name can not be repeated.");
        }else{
            Project project = new Project();
            project.setName(projectData.getName());
            project.setUserId(Long.parseLong(projectData.getUserId()));
            project.setAPIKey(APIKey);

            projectRepository.save(project);
        }
    }

    @Override
    public List<Project> getProjects(long userId) {
         return projectRepository.getProjectsByUserId(userId);
    }

    @Override
    public void deleteProject(String APIKey, String projectId) {
        projectRepository.findById(Long.parseLong(projectId)).orElseThrow();
        projectRepository.deleteById(Long.parseLong(projectId));
        log.info("Delete project by : " + projectId + " successfully!");

    }
}
