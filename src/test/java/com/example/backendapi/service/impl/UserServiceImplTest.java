package com.example.backendapi.service.impl;

import Models.User;
import config.JwtUtils;
import dto.LoginRequest;
import dto.SignupRequest;
import dto.UserProfileResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import repository.UserRepository;
import service.impl.UserServiceImpl;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private JwtUtils jwtUtils;

    @Mock
    private Authentication authentication;

    @InjectMocks
    private UserServiceImpl userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void registerUser_Success() throws Exception {
        // Arrange
        SignupRequest signupRequest = new SignupRequest();
        signupRequest.setName("Test User");
        signupRequest.setEmail("test@example.com");
        signupRequest.setPassword("Test@1234");
        signupRequest.setAddress("123 Test Street");
        signupRequest.setPhoneNumber("1234567890");

        User savedUser = new User();
        savedUser.setId(1L);
        savedUser.setName(signupRequest.getName());
        savedUser.setEmail(signupRequest.getEmail());
        savedUser.setAddress(signupRequest.getAddress());
        savedUser.setPhoneNumber(signupRequest.getPhoneNumber());
        savedUser.setPassword("encodedPassword");

        when(userRepository.existsByEmail(signupRequest.getEmail())).thenReturn(false);
        when(passwordEncoder.encode(signupRequest.getPassword())).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenReturn(savedUser);

        // Act
        User result = userService.registerUser(signupRequest);

        // Assert
        assertNotNull(result);
        assertEquals(savedUser.getId(), result.getId());
        assertEquals(signupRequest.getName(), result.getName());
        assertEquals(signupRequest.getEmail(), result.getEmail());
        assertEquals(signupRequest.getAddress(), result.getAddress());
        assertEquals(signupRequest.getPhoneNumber(), result.getPhoneNumber());
        assertEquals("encodedPassword", result.getPassword());

        verify(userRepository).existsByEmail(signupRequest.getEmail());
        verify(passwordEncoder).encode(signupRequest.getPassword());
        verify(userRepository).save(any(User.class));
    }

    @Test
    void registerUser_EmailAlreadyExists() {
        // Arrange
        SignupRequest signupRequest = new SignupRequest();
        signupRequest.setName("Test User");
        signupRequest.setEmail("existing@example.com");
        signupRequest.setPassword("Test@1234");

        when(userRepository.existsByEmail(signupRequest.getEmail())).thenReturn(true);

        // Act & Assert
        Exception exception = assertThrows(Exception.class, () -> {
            userService.registerUser(signupRequest);
        });

        assertEquals("Email is already in use!", exception.getMessage());
        verify(userRepository).existsByEmail(signupRequest.getEmail());
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void authenticateUser_Success() throws Exception {
        // Arrange
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail("test@example.com");
        loginRequest.setPassword("Test@1234");

        User user = new User();
        user.setEmail(loginRequest.getEmail());

        String expectedToken = "jwtToken";

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(authentication);
        when(userRepository.findByEmail(loginRequest.getEmail())).thenReturn(Optional.of(user));
        when(jwtUtils.generateToken(loginRequest.getEmail())).thenReturn(expectedToken);

        // Act
        String result = userService.authenticateUser(loginRequest);

        // Assert
        assertEquals(expectedToken, result);
        verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(userRepository).findByEmail(loginRequest.getEmail());
        verify(userRepository).save(any(User.class));
        verify(jwtUtils).generateToken(loginRequest.getEmail());
    }

    @Test
    void authenticateUser_InvalidCredentials() {
        // Arrange
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail("test@example.com");
        loginRequest.setPassword("wrongPassword");

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenThrow(new AuthenticationException("Invalid credentials") {});

        // Act & Assert
        Exception exception = assertThrows(Exception.class, () -> {
            userService.authenticateUser(loginRequest);
        });

        assertEquals("Invalid email or password", exception.getMessage());
        verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(jwtUtils, never()).generateToken(anyString());
    }

    @Test
    void getUserProfile_Success() throws Exception {
        // Arrange
        String email = "test@example.com";
        User user = new User();
        user.setId(1L);
        user.setName("Test User");
        user.setEmail(email);
        user.setAddress("123 Test Street");
        user.setPhoneNumber("1234567890");
        user.setJobTitle("Developer");
        user.setDepartment("IT");
        user.setCompany("Test Company");
        user.setLocation("Test Location");
        user.setBio("Test Bio");
        user.setProfilePic("test.jpg");
        user.setPlan("Premium");
        user.setMemberSince(LocalDateTime.now().minusDays(30));
        user.setLastLogin(LocalDateTime.now());

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));

        // Act
        UserProfileResponse result = userService.getUserProfile(email);

        // Assert
        assertNotNull(result);
        assertEquals(user.getId(), result.getId());
        assertEquals(user.getName(), result.getName());
        assertEquals(user.getEmail(), result.getEmail());
        assertEquals(user.getAddress(), result.getAddress());
        assertEquals(user.getPhoneNumber(), result.getPhoneNumber());
        assertEquals(user.getJobTitle(), result.getJobTitle());
        assertEquals(user.getDepartment(), result.getDepartment());
        assertEquals(user.getCompany(), result.getCompany());
        assertEquals(user.getLocation(), result.getLocation());
        assertEquals(user.getBio(), result.getBio());
        assertEquals(user.getProfilePic(), result.getProfilePic());
        assertEquals(user.getPlan(), result.getPlan());
        assertEquals(user.getMemberSince(), result.getMemberSince());
        assertEquals(user.getLastLogin(), result.getLastLogin());

        verify(userRepository).findByEmail(email);
    }

    @Test
    void getUserProfile_UserNotFound() {
        // Arrange
        String email = "nonexistent@example.com";
        
        when(userRepository.findByEmail(email)).thenReturn(Optional.empty());

        // Act & Assert
        Exception exception = assertThrows(Exception.class, () -> {
            userService.getUserProfile(email);
        });

        assertEquals("User not found", exception.getMessage());
        verify(userRepository).findByEmail(email);
    }
}
