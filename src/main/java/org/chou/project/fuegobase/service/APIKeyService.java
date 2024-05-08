package org.chou.project.fuegobase.service;

import org.chou.project.fuegobase.exception.APIKeyException;
import org.chou.project.fuegobase.model.database.Project;
import org.chou.project.fuegobase.model.security.APIKey;

import java.util.List;

public interface APIKeyService {
    void generateKeyWhenCreateProject(Project project);

    APIKey generateNewKey(String projectId) throws APIKeyException;

    void deleteKey(String oldKey);

    List<APIKey> getAllAPIKey(String projectId);


}
