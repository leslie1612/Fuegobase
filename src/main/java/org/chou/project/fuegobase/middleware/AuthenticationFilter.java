package org.chou.project.fuegobase.middleware;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.Nullable;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.chou.project.fuegobase.repository.user.UserRepository;
import org.chou.project.fuegobase.service.AuthenticationService;
import org.chou.project.fuegobase.utils.JwtTokenUtil;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
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
    private final JwtTokenUtil jwtTokenUtil;
    private final UserRepository userRepository;
    private final AuthenticationService authenticationService;

    public AuthenticationFilter(AuthenticationService authenticationService, JwtTokenUtil jwtTokenUtil,
                                UserRepository userRepository) {
        this.authenticationService = authenticationService;
        this.jwtTokenUtil = jwtTokenUtil;
        this.userRepository = userRepository;
    }


    @Override
    public void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws IOException, ServletException {

        String APIKey = retrieveAPIKey(request);
        String token = retrieveToken(request);

        String[] uri = request.getRequestURI().split("/");
        String projectId = null;

        String path = request.getRequestURI();

        try {

            if (uri.length > 5) {
                projectId = uri[5];
            }

            // check jwt
            if (token != null && jwtTokenUtil.validate(token)) {
                UserDetails userDetails = userRepository.getUserDetailsByToken(token);
                if (path.equals("/api/v1/databases/projects") ||
                        path.startsWith("/api/v1/security/project/") ||
                        authenticationService.validateByJWT(userDetails, projectId)) {
                    UsernamePasswordAuthenticationToken authAfterSuccessLogin = new UsernamePasswordAuthenticationToken(
                            userDetails, null, userDetails.getAuthorities());
                    authAfterSuccessLogin.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authAfterSuccessLogin);
                }
            }

            // check APIKey and domain
            if (APIKey != null && authenticationService.validate(request, projectId, APIKey)) {
                Authentication authentication = authenticationService.getAuthentication(request);
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
            filterChain.doFilter(request, response);

        } catch (Exception e) {
            log.error("Filter error " + e.getMessage());
            Map<String, String> errorMsg = new HashMap<>();
            errorMsg.put("Error ", "authentication fail");
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
    public String retrieveAPIKey(HttpServletRequest request) {

        String APIKey = request.getHeader("X-API-KEY");
        if (isEmpty(APIKey)) {
            return null;
        }
        return APIKey;
    }

    @Nullable
    public String retrieveToken(HttpServletRequest request) {

        final String header = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (isEmpty(header) || !header.startsWith("Bearer ")) {
            return null;
        }
        return header.split(" ")[1].trim();
    }


}
