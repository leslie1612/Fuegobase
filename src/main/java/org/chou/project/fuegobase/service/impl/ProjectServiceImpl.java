package org.chou.project.fuegobase.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.chou.project.fuegobase.data.database.DomainNameData;
import org.chou.project.fuegobase.data.database.ProjectData;
import org.chou.project.fuegobase.data.dto.DomainNameListDto;
import org.chou.project.fuegobase.model.database.DomainNameWhitelist;
import org.chou.project.fuegobase.model.database.Project;
import org.chou.project.fuegobase.model.user.User;
import org.chou.project.fuegobase.repository.database.DomainNameRepository;
import org.chou.project.fuegobase.repository.database.ProjectRepository;
import org.chou.project.fuegobase.service.ProjectService;
import org.chou.project.fuegobase.service.UserService;
import org.chou.project.fuegobase.utils.ApiKeyGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class ProjectServiceImpl implements ProjectService {

    private final ProjectRepository projectRepository;
    private final DomainNameRepository domainNameRepository;
    private final ApiKeyGenerator apiKeyGenerator;
    private final UserService userService;

    @Autowired
    public ProjectServiceImpl(ProjectRepository projectRepository, DomainNameRepository domainNameRepository,
                              ApiKeyGenerator apiKeyGenerator, UserService userService) {
        this.projectRepository = projectRepository;
        this.domainNameRepository = domainNameRepository;
        this.apiKeyGenerator = apiKeyGenerator;
        this.userService = userService;

    }


    @Override
    public void createProject(ProjectData projectData, String token) {

        User user = userService.getUserByToken(token);
        String APIKey = apiKeyGenerator.generateApiKey();

        if (projectRepository.existsByName(projectData.getName())) {
            throw new IllegalArgumentException("Project name can not be repeated.");
        } else {
            Project project = new Project();
            project.setName(projectData.getName());
            project.setUserId(user.getId());
            project.setAPIKey(APIKey);

            Project savedProject = projectRepository.save(project);

            DomainNameWhitelist domainNameWhitelist = new DomainNameWhitelist();
            domainNameWhitelist.setProjectId(savedProject.getId());
            domainNameWhitelist.setDomainName("localhost");
            domainNameWhitelist.setType("Default");

            domainNameRepository.save(domainNameWhitelist);
        }
    }

    @Override
    public List<Project> getProjects(String token) {
        User user = userService.getUserByToken(token);
        return projectRepository.getProjectsByUserId(user.getId());
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
        domainNameWhitelist.setType("Custom");

        domainNameRepository.save(domainNameWhitelist);
    }

    @Override
    public DomainNameListDto getDomainWhiteList(long projectId) {
        DomainNameListDto domainNameListDto = new DomainNameListDto();

        Project project = projectRepository.findById(projectId).orElseThrow();
        List<DomainNameWhitelist> domainNameWhitelists = domainNameRepository.findAllByProjectId(projectId);

        domainNameListDto.setProjectName(project.getName());
        domainNameListDto.setApiKey(project.getAPIKey());
        domainNameListDto.setDomainNameWhitelist(domainNameWhitelists);

        return domainNameListDto;
    }

    @Override
    public void deleteDomainName(long projectId, long domainNameId) {
        domainNameRepository.deleteById(domainNameId);
    }


}
