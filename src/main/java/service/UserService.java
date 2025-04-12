package service;

import dto.SignupRequest;
import Models.User;

public interface UserService {
    User registerUser(SignupRequest signupRequest) throws Exception;
}
