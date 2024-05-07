package org.chou.project.fuegobase.service;

import org.chou.project.fuegobase.exception.APIKeyException;
import org.chou.project.fuegobase.model.security.APIKey;

import java.util.List;

public interface APIKeyService {
    APIKey generateNewKey(long projectId) throws APIKeyException;

    void deleteKey(String oldKey);

    List<APIKey> findAPIKey(long projectId);

}
