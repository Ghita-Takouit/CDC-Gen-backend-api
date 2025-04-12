package service.impl;

import dto.SignupRequest;
import Models.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import repository.UserRepository;
import service.UserService;
import utils.ValidationUtils;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
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

        // Save user
        return userRepository.save(user);
    }
}
