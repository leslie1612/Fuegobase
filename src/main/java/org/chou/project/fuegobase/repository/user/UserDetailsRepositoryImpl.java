package org.chou.project.fuegobase.repository.user;

import org.chou.project.fuegobase.middleware.JwtTokenUtil;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

public class UserDetailsRepositoryImpl implements UserDetailsRepository {

    private final JwtTokenUtil jwtTokenUtil;

    public UserDetailsRepositoryImpl(JwtTokenUtil jwtTokenUtil) {
        this.jwtTokenUtil = jwtTokenUtil;
    }

    @Override
    public UserDetails getUserDetailsByToken(String token) {
        return User.builder()
                .username(jwtTokenUtil.getUsernameFromToken(token))
                .password("")
                .build();
    }

    @Override
    public UserDetails getUserDetails(String userName, String password) {
        return User.builder().username(userName).password(password).build();

    }
}
