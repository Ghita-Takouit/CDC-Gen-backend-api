package service.impl;

import config.JwtUtils;
import dto.LoginRequest;
import dto.SignupRequest;
import dto.UserProfileResponse;
import Models.User;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import repository.UserRepository;
import service.UserService;
import utils.ValidationUtils;

import java.time.LocalDateTime;

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
        // Validate required fields
        if (ValidationUtils.isNullOrEmpty(signupRequest.getName())) {
            throw new Exception("Name is required");
        }
        
        if (ValidationUtils.isNullOrEmpty(signupRequest.getEmail())) {
            throw new Exception("Email is required");
        }
        
        if (ValidationUtils.isNullOrEmpty(signupRequest.getPassword())) {
            throw new Exception("Password is required");
        }
        
        // Validate email format
        if (!ValidationUtils.isValidEmail(signupRequest.getEmail())) {
            throw new Exception("Invalid email format");
        }
        
        // Validate password strength
        if (!ValidationUtils.isValidPassword(signupRequest.getPassword())) {
            throw new Exception("Password must be at least 8 characters long and contain at least one digit, " +
                    "one lowercase letter, one uppercase letter, and one special character");
        }
        
        // Validate phone number if provided
        if (signupRequest.getPhoneNumber() != null && !signupRequest.getPhoneNumber().isEmpty() && 
            !ValidationUtils.isValidPhoneNumber(signupRequest.getPhoneNumber())) {
            throw new Exception("Invalid phone number format");
        }

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
        
        // Set memberSince and lastLogin to current time
        LocalDateTime now = LocalDateTime.now();
        user.setMemberSince(now);
        user.setLastLogin(now);

        // Save user
        return userRepository.save(user);
    }
    
    @Override
    public String authenticateUser(LoginRequest loginRequest) throws Exception {
        try {
            // Use AuthenticationManager to authenticate the user
            authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                    loginRequest.getEmail(),
                    loginRequest.getPassword()
                )
            );
            
            // Update the lastLogin time for the user
            User user = userRepository.findByEmail(loginRequest.getEmail())
                .orElseThrow(() -> new Exception("User not found"));
            user.setLastLogin(LocalDateTime.now());
            userRepository.save(user);
            
            // If authentication was successful, generate a JWT token
            return jwtUtils.generateToken(loginRequest.getEmail());
        } catch (AuthenticationException e) {
            throw new Exception("Invalid email or password");
        }
    }
    
    @Override
    public UserProfileResponse getUserProfile(String email) throws Exception {
        User user = userRepository.findByEmail(email)
            .orElseThrow(() -> new Exception("User not found"));
            
        UserProfileResponse profileResponse = new UserProfileResponse();
        profileResponse.setId(user.getId());
        profileResponse.setName(user.getName());
        profileResponse.setEmail(user.getEmail());
        profileResponse.setAddress(user.getAddress());
        profileResponse.setPhoneNumber(user.getPhoneNumber());
        profileResponse.setJobTitle(user.getJobTitle());
        profileResponse.setDepartment(user.getDepartment());
        profileResponse.setCompany(user.getCompany());
        profileResponse.setLocation(user.getLocation());
        profileResponse.setBio(user.getBio());
        profileResponse.setProfilePic(user.getProfilePic());
        profileResponse.setPlan(user.getPlan());
        profileResponse.setMemberSince(user.getMemberSince());
        profileResponse.setLastLogin(user.getLastLogin());
        
        return profileResponse;
    }
}
