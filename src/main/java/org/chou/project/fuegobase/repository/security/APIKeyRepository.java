package org.chou.project.fuegobase.repository.security;

import org.chou.project.fuegobase.model.security.APIKey;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface APIKeyRepository extends JpaRepository<APIKey, Long> {
    List<APIKey> findAllByProjectId(long projectId);

    void deleteAPIKeyByName(String name);

}
