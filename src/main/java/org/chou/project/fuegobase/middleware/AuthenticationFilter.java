package org.chou.project.fuegobase.middleware;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.Nullable;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.chou.project.fuegobase.security.ApiKeyAuthentication;
import org.chou.project.fuegobase.service.AuthenticationService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static io.micrometer.common.util.StringUtils.isEmpty;

@Slf4j
@Component
public class AuthenticationFilter extends OncePerRequestFilter {
    private final ObjectMapper jsonObjectMapper = new ObjectMapper();

    private final AuthenticationService authenticationService;

    public AuthenticationFilter(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @Override
    public void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws IOException, ServletException {
        try {
            String APIKey = retrieveToken(request);
            String[] uri = request.getRequestURI().split("/");
            String projectId = null;
            if (uri.length > 5) {
                projectId = uri[5];
            }
            if (APIKey == null || projectId == null ||!authenticationService.validate(projectId, APIKey)) {
                filterChain.doFilter(request, response);
                return;
            }
            Authentication authentication = authenticationService.getAuthentication(request);
            SecurityContextHolder.getContext().setAuthentication(authentication);
            filterChain.doFilter(request, response);


        } catch (Exception e) {
            log.error("error " + e.getMessage());
            Map<String, String> errorMsg = new HashMap<>();
            errorMsg.put("error ", e.getMessage());
            handleException(response, HttpStatus.UNAUTHORIZED.value(), errorMsg);
        }
    }

    private void handleException(HttpServletResponse response, int status, Map<String, String> message)
            throws IOException {
        response.setStatus(status);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        jsonObjectMapper.writeValue(response.getWriter(), message);
    }

    @Nullable
    public String retrieveToken(HttpServletRequest request) {
        String APIKey = request.getHeader("X-API-KEY");
        if (isEmpty(APIKey)) {
            return null;
        }
        return APIKey;
    }
}
