package service.impl;

import config.JwtUtils;
import dto.LoginRequest;
import dto.SignupRequest;
import Models.User;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import repository.UserRepository;
import service.UserService;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;

    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder,
                          AuthenticationManager authenticationManager, JwtUtils jwtUtils) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtUtils = jwtUtils;
    }

    @Override
    public User registerUser(SignupRequest signupRequest) throws Exception {
        // Check if user already exists
        if (userRepository.existsByEmail(signupRequest.getEmail())) {
            throw new Exception("Email is already in use!");
        }

        // Create new user
        User user = new User();
        user.setName(signupRequest.getName());
        user.setEmail(signupRequest.getEmail());
        user.setAddress(signupRequest.getAddress());
        user.setPhoneNumber(signupRequest.getPhoneNumber());
        user.setPassword(passwordEncoder.encode(signupRequest.getPassword()));

        // Save user
        return userRepository.save(user);
    }
    
    @Override
    public String authenticateUser(LoginRequest loginRequest) throws Exception {
        try {
            // Use AuthenticationManager to authenticate the user
            Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                    loginRequest.getEmail(),
                    loginRequest.getPassword()
                )
            );
            
            // If authentication was successful, generate a JWT token
            return jwtUtils.generateToken(loginRequest.getEmail());
        } catch (AuthenticationException e) {
            throw new Exception("Invalid email or password");
        }
    }
}
