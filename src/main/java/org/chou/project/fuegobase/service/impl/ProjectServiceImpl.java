package org.chou.project.fuegobase.service.impl;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.chou.project.fuegobase.data.database.DomainNameData;
import org.chou.project.fuegobase.data.database.ProjectData;
import org.chou.project.fuegobase.model.database.DomainNameWhitelist;
import org.chou.project.fuegobase.model.database.Project;
import org.chou.project.fuegobase.repository.database.DomainNameRepository;
import org.chou.project.fuegobase.repository.database.ProjectRepository;
import org.chou.project.fuegobase.service.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class ProjectServiceImpl implements ProjectService {

    private final ProjectRepository projectRepository;
    private final DomainNameRepository domainNameRepository;


    @Autowired
    public ProjectServiceImpl(ProjectRepository projectRepository, DomainNameRepository domainNameRepository) {
        this.projectRepository = projectRepository;
        this.domainNameRepository = domainNameRepository;
    }


    @Override
    public void createProject(ProjectData projectData) {
        // TODO API key 如何產生
        String APIKey = "aaa12345bbb";

        if (projectRepository.existsByName(projectData.getName())) {
            throw new IllegalArgumentException("Name can not be repeated.");
        } else {
            Project project = new Project();
            project.setName(projectData.getName());
            project.setUserId(Long.parseLong(projectData.getUserId()));
            project.setAPIKey(APIKey);

            projectRepository.save(project);
        }
    }

    @Override
    public List<Project> getProjects(long userId) {
        System.out.println(userId);
        return projectRepository.getProjectsByUserId(userId);
    }

    @Override
    public void deleteProject(String projectId) {
        projectRepository.findById(Long.parseLong(projectId)).orElseThrow();
        projectRepository.deleteById(Long.parseLong(projectId));
        log.info("Delete project by : " + projectId + " successfully!");
    }

    @Override
    public void addDomainNameWhiteList(long projectId, DomainNameData domainNameData) {
        DomainNameWhitelist domainNameWhitelist = new DomainNameWhitelist();
        domainNameWhitelist.setProjectId(projectId);
        domainNameWhitelist.setDomainName(domainNameData.getDomainName());

        domainNameRepository.save(domainNameWhitelist);
    }

}
