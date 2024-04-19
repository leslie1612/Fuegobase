package org.chou.project.fuegobase.middleware;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.chou.project.fuegobase.service.AuthenticationService;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

@Component
@NoArgsConstructor
@AllArgsConstructor
public class DomainInterceptor implements HandlerInterceptor {
    private AuthenticationService authenticationService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String userDomain = request.getServerName();
        String[] uri = request.getRequestURI().split("/");

        if (userDomain != null && authenticationService.domainValidate(userDomain, uri[5])) {
            return true;
        } else {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Access denied");
            return false;
        }
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
    }
}
