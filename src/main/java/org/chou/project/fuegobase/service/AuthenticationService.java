package org.chou.project.fuegobase.service;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.chou.project.fuegobase.repository.database.ProjectRepository;
import org.chou.project.fuegobase.security.ApiKeyAuthentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class AuthenticationService {

    private final ProjectRepository projectRepository;

    @Autowired
    public AuthenticationService(ProjectRepository projectRepository){
        this.projectRepository = projectRepository;
    }

    public Authentication getAuthentication(HttpServletRequest request) {
        String APIKey = request.getHeader("X-API-KEY");
        return new ApiKeyAuthentication(APIKey, AuthorityUtils.NO_AUTHORITIES);
    }

    public Boolean validate(String APIKey) {
        return projectRepository.existsByAPIKey(APIKey);
    }
}
