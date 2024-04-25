package org.chou.project.fuegobase.repository.user;

import org.springframework.security.core.userdetails.UserDetails;

public interface UserDetailsRepository {
    UserDetails getUserDetailsByToken(String token);

    UserDetails getUserDetails(String userName, String password);
}
