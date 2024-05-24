package org.chou.project.fuegobase.service.impl;

import org.chou.project.fuegobase.data.dto.SignInDto;
import org.chou.project.fuegobase.data.dto.UserDto;
import org.chou.project.fuegobase.data.user.SignInForm;
import org.chou.project.fuegobase.data.user.SignupForm;
import org.chou.project.fuegobase.model.user.User;
import org.chou.project.fuegobase.repository.user.UserRepository;
import org.chou.project.fuegobase.service.UserService;
import org.chou.project.fuegobase.utils.JwtTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    private final JwtTokenUtil jwtTokenUtil;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, JwtTokenUtil jwtTokenUtil, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.jwtTokenUtil = jwtTokenUtil;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public SignInDto signup(SignupForm signupForm) throws UserExistException {
        User existingUser = userRepository.findByEmail(signupForm.getEmail());
        if (existingUser != null) {
            throw new UserExistException(signupForm.getEmail() + " is already exist");
        }

        User user = new User();
        user.setName(signupForm.getName());
        user.setEmail(signupForm.getEmail());
        user.setPassword(passwordEncoder.encode(signupForm.getPassword()));

        User savedUser = userRepository.save(user);

        return mapUserToUserDto(savedUser);

//        UserDto userDto = new UserDto();
//        userDto.setId(savedUser.getId());
//        userDto.setEmail(savedUser.getEmail());
//        userDto.setName(savedUser.getName());
//
//        SignInDto signInDto = new SignInDto();
//        String token = createAccessToken(savedUser);
//        signInDto.setAccessToken(token);
//        signInDto.setAccessExpired(jwtTokenUtil.getExpirationDateFromToken(token).getTime());
//        signInDto.setUserDto(userDto);
//
//        return signInDto;
    }

    @Override
    public SignInDto signin(SignInForm signInForm) throws UserNotExistException, UserPasswordMismatchException {
        User user = userRepository.findByEmail(signInForm.getEmail());
        if (user == null) {
            throw new UserNotExistException("User Not Found with email : " + signInForm.getEmail());
        }

        if (!passwordEncoder.matches(signInForm.getPassword(), user.getPassword())) {
            throw new UserPasswordMismatchException("Wrong Password");
        }

        return mapUserToUserDto(user);
//        UserDto userDto = new UserDto();
//        userDto.setId(user.getId());
//        userDto.setEmail(user.getEmail());
//        userDto.setName(user.getName());
//
//        SignInDto signInDto = new SignInDto();
//        String token = createAccessToken(user);
//        signInDto.setAccessToken(token);
//        signInDto.setAccessExpired(jwtTokenUtil.getExpirationDateFromToken(token).getTime());
//        signInDto.setUserDto(userDto);
//
//        return signInDto;

    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return null;
    }

    public String createAccessToken(User user) {

        Map<String, Object> claims = new HashMap<>();
        claims.put("id", user.getId());
        claims.put("name", user.getName());
        claims.put("email", user.getEmail());

        return jwtTokenUtil.generateToken(claims, user.getEmail());
    }

    public User getUserByToken(String token) {
        String email = jwtTokenUtil.getUsernameFromToken(token);
        return userRepository.findByEmail(email);
    }

    public SignInDto mapUserToUserDto(User user) {
        UserDto userDto = new UserDto();
        userDto.setId(user.getId());
        userDto.setEmail(user.getEmail());
        userDto.setName(user.getName());

        SignInDto signInDto = new SignInDto();
        String token = createAccessToken(user);
        signInDto.setAccessToken(token);
        signInDto.setAccessExpired(jwtTokenUtil.getExpirationDateFromToken(token).getTime());
        signInDto.setUserDto(userDto);

        return signInDto;
    }
}
