package org.chou.project.fuegobase.service;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.chou.project.fuegobase.repository.database.DomainNameRepository;
import org.chou.project.fuegobase.repository.database.ProjectRepository;
import org.chou.project.fuegobase.security.ApiKeyAuthentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class AuthenticationService {
    private final ProjectRepository projectRepository;
    private final DomainNameRepository domainNameRepository;

    @Autowired
    public AuthenticationService(ProjectRepository projectRepository, DomainNameRepository domainNameRepository) {
        this.projectRepository = projectRepository;
        this.domainNameRepository = domainNameRepository;
    }

    public Authentication getAuthentication(HttpServletRequest request) {
        String APIKey = request.getHeader("X-API-KEY");
        return new ApiKeyAuthentication(APIKey, AuthorityUtils.NO_AUTHORITIES);
    }

    public Boolean validate(String projectId, String APIKey) {
        return projectRepository.existsByIdAndAPIKey(Long.parseLong(projectId), APIKey);
    }

    public Boolean domainValidate(String domain, String projectId) {

        return domainNameRepository.findByDomainNameAndProjectId(domain, Long.parseLong(projectId)).orElse(null) != null;
    }

}
