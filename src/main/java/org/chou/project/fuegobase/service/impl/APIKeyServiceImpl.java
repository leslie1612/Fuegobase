package org.chou.project.fuegobase.service.impl;

import org.chou.project.fuegobase.exception.APIKeyException;
import org.chou.project.fuegobase.model.database.Project;
import org.chou.project.fuegobase.model.security.APIKey;
import org.chou.project.fuegobase.repository.database.ProjectRepository;
import org.chou.project.fuegobase.repository.security.APIKeyRepository;
import org.chou.project.fuegobase.service.APIKeyService;
import org.chou.project.fuegobase.utils.ApiKeyGenerator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class APIKeyServiceImpl implements APIKeyService {
    private APIKeyRepository apiKeyRepository;
    private ApiKeyGenerator apiKeyGenerator;
    private ProjectRepository projectRepository;

    public APIKeyServiceImpl(APIKeyRepository apiKeyRepository, ApiKeyGenerator apiKeyGenerator,
                             ProjectRepository projectRepository) {
        this.apiKeyRepository = apiKeyRepository;
        this.apiKeyGenerator = apiKeyGenerator;
        this.projectRepository = projectRepository;
    }

    @Override
    public APIKey generateNewKey(long projectId) throws APIKeyException {
        APIKey apiKey = new APIKey();

        List<APIKey> existingAPIList = apiKeyRepository.findAllByProjectId(projectId);
        if (existingAPIList.size() > 2) {
            throw new APIKeyException("Maximum number of API keys exceeded for this project");
        } else if (existingAPIList.isEmpty()) {
            Project project = projectRepository.findById(projectId).orElse(null);
            apiKey.setProject(project);
        } else {
            apiKey.setProject(existingAPIList.get(0).getProject());
        }
        apiKey.setName(apiKeyGenerator.generateApiKey());

        LocalDateTime now = LocalDateTime.now().plusDays(30);
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
    public List<APIKey> findAPIKey(long projectId) {
        return apiKeyRepository.findAllByProjectId(projectId);
    }
}
