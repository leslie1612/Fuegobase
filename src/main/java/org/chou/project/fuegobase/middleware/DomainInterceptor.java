package org.chou.project.fuegobase.middleware;

import jakarta.annotation.Nullable;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.chou.project.fuegobase.service.AuthenticationService;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import java.net.URI;

import static io.micrometer.common.util.StringUtils.isEmpty;

@Slf4j
@Component
@NoArgsConstructor
@AllArgsConstructor
public class DomainInterceptor implements HandlerInterceptor {
    private AuthenticationService authenticationService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String jwt = retrieveToken(request);

        if (jwt == null) {
            String origin = request.getHeader(HttpHeaders.ORIGIN);
            URI uri = new URI(origin);
            String userDomain = uri.getHost();

            String[] uris = request.getRequestURI().split("/");

            if (userDomain != null && authenticationService.domainValidate(userDomain, uris[5])) {
                return true;
            } else {
                response.sendError(HttpServletResponse.SC_FORBIDDEN, "Access denied");
                return false;
            }
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
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
