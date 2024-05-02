package org.chou.project.fuegobase.service;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.chou.project.fuegobase.repository.database.DomainNameRepository;
import org.chou.project.fuegobase.repository.database.ProjectRepository;
import org.chou.project.fuegobase.security.ApiKeyAuthentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.URISyntaxException;

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

    public Boolean validate(HttpServletRequest request, String projectId, String APIKey) throws URISyntaxException {
        String origin = request.getHeader(HttpHeaders.ORIGIN);
        if (origin == null) {
            return false;
        }
        URI uri = new URI(origin);
        String userDomain = uri.getHost();
        String[] uris = request.getRequestURI().split("/");

        return projectRepository.existsByIdAndAPIKey(Long.parseLong(projectId), APIKey) && domainValidate(userDomain, uris[5]);
    }

    public Boolean domainValidate(String domain, String projectId) {
        return domainNameRepository.findByDomainNameAndProjectId(domain, Long.parseLong(projectId)).orElse(null) != null;
    }

}
