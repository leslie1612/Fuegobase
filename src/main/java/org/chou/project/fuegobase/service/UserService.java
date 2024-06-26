package org.chou.project.fuegobase.service;

import org.chou.project.fuegobase.data.dto.SignInDto;
import org.chou.project.fuegobase.data.user.SignInForm;
import org.chou.project.fuegobase.data.user.SignupForm;
import org.chou.project.fuegobase.model.user.User;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService {
    SignInDto signup(SignupForm signupForm) throws UserExistException;

    SignInDto signin(SignInForm signInForm) throws UserNotExistException, UserPasswordMismatchException;

    User getUserByToken(String token);

    sealed class UserException extends
            Exception permits UserExistException, UserNotExistException, UserPasswordMismatchException {
        public UserException(String message) {
            super(message);
        }

    }

    final class UserExistException extends UserException {
        public UserExistException(String message) {
            super(message);
        }

    }

    final class UserNotExistException extends UserException {

        public UserNotExistException(String message) {
            super(message);
        }

    }

    final class UserPasswordMismatchException extends UserException {

        public UserPasswordMismatchException(String message) {
            super(message);
        }

    }
}
