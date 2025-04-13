package service;

import dto.LoginRequest;
import dto.SignupRequest;
import Models.User;

public interface UserService {
    User registerUser(SignupRequest signupRequest) throws Exception;
    String authenticateUser(LoginRequest loginRequest) throws Exception;
}
