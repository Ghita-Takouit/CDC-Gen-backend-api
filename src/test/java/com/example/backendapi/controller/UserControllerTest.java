package com.example.backendapi.controller;

import Controller.UserController;
import dto.AuthResponse;
import dto.UserProfileResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import service.UserService;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserControllerTest {

    @Mock
    private UserService userService;

    @Mock
    private UserDetails userDetails;

    @InjectMocks
    private UserController userController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getUserProfile_Success() throws Exception {
        // Arrange
        String email = "test@example.com";
        UserProfileResponse expectedProfile = new UserProfileResponse();
        expectedProfile.setName("Test User");
        expectedProfile.setEmail(email);

        when(userDetails.getUsername()).thenReturn(email);
        when(userService.getUserProfile(email)).thenReturn(expectedProfile);

        // Act
        ResponseEntity<?> response = userController.getUserProfile(userDetails);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedProfile, response.getBody());
        verify(userService).getUserProfile(email);
    }

    @Test
    void getUserProfile_Error() throws Exception {
        // Arrange
        String email = "test@example.com";
        String errorMessage = "User not found";
        
        when(userDetails.getUsername()).thenReturn(email);
        when(userService.getUserProfile(email)).thenThrow(new Exception(errorMessage));

        // Act
        ResponseEntity<?> response = userController.getUserProfile(userDetails);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertTrue(response.getBody() instanceof AuthResponse);
        
        AuthResponse authResponse = (AuthResponse) response.getBody();
        assertFalse(authResponse.isSuccess());
        assertTrue(authResponse.getMessage().contains(errorMessage));
        verify(userService).getUserProfile(email);
    }
}
