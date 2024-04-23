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
import org.chou.project.fuegobase.utils.ApiKeyGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class ProjectServiceImpl implements ProjectService {

    private final ProjectRepository projectRepository;
    private final DomainNameRepository domainNameRepository;
    private final ApiKeyGenerator apiKeyGenerator;

    @Autowired
    public ProjectServiceImpl(ProjectRepository projectRepository,
                              DomainNameRepository domainNameRepository,
                              ApiKeyGenerator apiKeyGenerator) {
        this.projectRepository = projectRepository;
        this.domainNameRepository = domainNameRepository;
        this.apiKeyGenerator = apiKeyGenerator;
    }


    @Override
    public void createProject(ProjectData projectData) {
        String APIKey = apiKeyGenerator.generateApiKey();

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

    @Override
    public List<DomainNameWhitelist> getDomainWhiteList(long projectId) {
        return domainNameRepository.findAllByProjectId(projectId);
//        List<String> domainNames = new ArrayList<>();
//        for (DomainNameWhitelist domainNameWhitelist : domainNameWhitelists) {
//            domainNames.add(domainNameWhitelist.getDomainName());
//        }
//        return domainNames;
    }

    @Override
    public void deleteDomainName(long projectId,long domainNameId) {
        domainNameRepository.deleteById(domainNameId);
    }
}
