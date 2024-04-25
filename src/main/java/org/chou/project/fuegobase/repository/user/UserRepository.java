package org.chou.project.fuegobase.repository.user;

import org.chou.project.fuegobase.model.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends UserDetailsRepository, JpaRepository<User, Long> {
    User findByEmail(String email);
}
