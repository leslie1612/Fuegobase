package org.chou.project.fuegobase.controller.user;

import org.chou.project.fuegobase.data.GenericResponse;
import org.chou.project.fuegobase.data.user.SignInForm;
import org.chou.project.fuegobase.data.user.SignupForm;
import org.chou.project.fuegobase.error.ErrorResponse;
import org.chou.project.fuegobase.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/user")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/signup")
    public ResponseEntity<?> signUp(@RequestBody SignupForm signupForm) {
        try {
            return ResponseEntity.status(HttpStatus.CREATED).body(new GenericResponse<>(userService.signup(signupForm)));
        } catch (UserService.UserExistException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse(e.getMessage()));
        }
    }

    @PostMapping("/signin")
    public ResponseEntity<?> signIn(@RequestBody SignInForm signInForm) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(new GenericResponse<>(userService.signin(signInForm)));
        } catch (UserService.UserNotExistException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse(e.getMessage()));
        } catch (UserService.UserPasswordMismatchException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new ErrorResponse(e.getMessage()));
        }

    }

}
