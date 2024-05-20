package org.chou.project.fuegobase.service.impl;

import org.chou.project.fuegobase.exception.APIKeyException;
import org.chou.project.fuegobase.model.database.Project;
import org.chou.project.fuegobase.model.security.APIKey;
import org.chou.project.fuegobase.repository.database.ProjectRepository;
import org.chou.project.fuegobase.repository.security.APIKeyRepository;
import org.chou.project.fuegobase.service.APIKeyService;
import org.chou.project.fuegobase.utils.ApiKeyGenerator;
import org.chou.project.fuegobase.utils.HashIdUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class APIKeyServiceImpl implements APIKeyService {
    private final APIKeyRepository apiKeyRepository;
    private final ApiKeyGenerator apiKeyGenerator;
    private final ProjectRepository projectRepository;
    private final HashIdUtil hashIdUtil;

    @Autowired
    public APIKeyServiceImpl(APIKeyRepository apiKeyRepository, ApiKeyGenerator apiKeyGenerator,
                             ProjectRepository projectRepository, HashIdUtil hashIdUtil) {
        this.apiKeyRepository = apiKeyRepository;
        this.apiKeyGenerator = apiKeyGenerator;
        this.projectRepository = projectRepository;
        this.hashIdUtil = hashIdUtil;
    }


    @Override
    public void generateKeyWhenCreateProject(Project project) {
        APIKey apiKey = new APIKey();

        apiKey.setProject(project);
        apiKey.setName(apiKeyGenerator.generateApiKey());

        LocalDateTime now = LocalDateTime.now().plusDays(30);
        apiKey.setExpirationTime(Timestamp.valueOf(now));
        apiKeyRepository.save(apiKey);
    }

    @Override
    public APIKey generateNewKey(String projectId) throws APIKeyException {
        APIKey apiKey = new APIKey();
        long id = hashIdUtil.decoded(projectId);

        List<APIKey> existingAPIList = apiKeyRepository.findAllByProjectId(id);
        if (existingAPIList.size() > 2) {
            throw new APIKeyException("Maximum number of API keys exceeded for this project");
        } else if (existingAPIList.isEmpty()) {
            Project project = projectRepository.findById(id).orElseThrow();
            apiKey.setProject(project);
        } else {
            apiKey.setProject(existingAPIList.get(0).getProject());
        }
        apiKey.setName(apiKeyGenerator.generateApiKey());

        LocalDateTime now = LocalDateTime.now().plusDays(90);
        apiKey.setExpirationTime(Timestamp.valueOf(now));
        apiKeyRepository.save(apiKey);
        return apiKey;
    }

    @Override
    @Transactional
    public void deleteKey(String oldKey) {
        apiKeyRepository.deleteAPIKeyByName(oldKey);
    }

    @Override
    public List<APIKey> getAllAPIKey(String projectId) {
        long id = hashIdUtil.decoded(projectId);
        Project project = projectRepository.findById(id).orElseThrow();
        return apiKeyRepository.findAllByProjectId(project.getId());
    }

}
