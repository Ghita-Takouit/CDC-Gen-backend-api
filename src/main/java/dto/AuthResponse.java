package dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data

@NoArgsConstructor
public class AuthResponse {
    private boolean success;
    private String message;
    public AuthResponse(boolean success, String message) {
        this.success = success;
        this.message = message;
    }

}
