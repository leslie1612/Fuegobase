package org.chou.project.fuegobase.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.chou.project.fuegobase.data.database.DomainNameData;
import org.chou.project.fuegobase.data.database.ProjectData;
import org.chou.project.fuegobase.data.dto.DomainNameListDto;
import org.chou.project.fuegobase.exception.APIKeyException;
import org.chou.project.fuegobase.model.database.DomainNameWhitelist;
import org.chou.project.fuegobase.model.database.Project;
import org.chou.project.fuegobase.model.security.APIKey;
import org.chou.project.fuegobase.model.user.User;
import org.chou.project.fuegobase.repository.database.DomainNameRepository;
import org.chou.project.fuegobase.repository.database.ProjectRepository;
import org.chou.project.fuegobase.service.APIKeyService;
import org.chou.project.fuegobase.service.ProjectService;
import org.chou.project.fuegobase.service.UserService;
import org.chou.project.fuegobase.utils.HashIdUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Slf4j
public class ProjectServiceImpl implements ProjectService {

    private final ProjectRepository projectRepository;
    private final DomainNameRepository domainNameRepository;
    private final UserService userService;
    private final APIKeyService apiKeyService;
    private final HashIdUtil hashIdUtil;


    @Autowired
    public ProjectServiceImpl(ProjectRepository projectRepository, DomainNameRepository domainNameRepository,
                              UserService userService, APIKeyService apiKeyService, HashIdUtil hashIdUtil) {
        this.projectRepository = projectRepository;
        this.domainNameRepository = domainNameRepository;
        this.userService = userService;
        this.apiKeyService = apiKeyService;
        this.hashIdUtil = hashIdUtil;
    }


    @Transactional
    @Override
    public void createProject(ProjectData projectData, String token) throws APIKeyException {

        User user = userService.getUserByToken(token);

        if (projectRepository.existsByNameAndUserId(projectData.getName(), user.getId())) {
            throw new IllegalArgumentException("Project name can not be repeated.");
        } else {
            Project project = new Project();
            project.setName(projectData.getName());
            project.setUserId(user.getId());
            Project savedProject = projectRepository.save(project);

            apiKeyService.generateNewKey(savedProject.getId());

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
        long id = hashIdUtil.decoded(projectId);
        projectRepository.findById(id).orElseThrow();
        projectRepository.deleteById(id);
        log.info("Delete project by : " + projectId + " successfully!");
    }

    @Override
    public void addDomainNameWhiteList(String projectId, DomainNameData domainNameData) {
        long id = hashIdUtil.decoded(projectId);
        DomainNameWhitelist domainNameWhitelist = new DomainNameWhitelist();
        domainNameWhitelist.setProjectId(id);
        domainNameWhitelist.setDomainName(domainNameData.getDomainName());
        domainNameWhitelist.setType("Custom");

        domainNameRepository.save(domainNameWhitelist);
    }

    @Override
    public DomainNameListDto getDomainWhiteList(String projectId) {
        long id = hashIdUtil.decoded(projectId);
        DomainNameListDto domainNameListDto = new DomainNameListDto();

        Project project = projectRepository.findById(id).orElseThrow();
        List<DomainNameWhitelist> domainNameWhitelists = domainNameRepository.findAllByProjectId(id);

        domainNameListDto.setProjectName(project.getName());
        List<APIKey> apiKeyList = apiKeyService.findAPIKey(id);
        List<String> apiKeyName = apiKeyList.stream().map(APIKey::getName).toList();
        domainNameListDto.setApiKeyList(apiKeyName);
        domainNameListDto.setDomainNameWhitelist(domainNameWhitelists);

        return domainNameListDto;
    }

    @Override
    public void deleteDomainName(String projectId, long domainNameId) {
        domainNameRepository.deleteById(domainNameId);
    }

}
