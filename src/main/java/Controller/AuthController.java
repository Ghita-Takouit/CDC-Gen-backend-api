package Controller;

import service.UserService;
import dto.AuthResponse;
import dto.SignupRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;

    @PostMapping("/signup")
    public ResponseEntity<AuthResponse> registerUser(@RequestBody SignupRequest signupRequest) {
        try {
            userService.registerUser(signupRequest);
            return ResponseEntity.ok(new AuthResponse(true, "User registered successfully"));
        } catch (Exception e) {
            // Return appropriate HTTP status based on the error
            HttpStatus status = HttpStatus.BAD_REQUEST;
            
            // Log the error for debugging
            System.err.println("Error during user registration: " + e.getMessage());
            
            return ResponseEntity
                    .status(status)
                    .body(new AuthResponse(false, "Registration failed: " + e.getMessage()));
        }
    }
}
