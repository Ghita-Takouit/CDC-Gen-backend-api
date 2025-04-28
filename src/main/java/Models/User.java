package Models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String name;
    
    @Column(nullable = false, unique = true)
    private String email;
    
    @Column
    private String address;
    
    @Column(name = "phone_number")
    private String phoneNumber;
    
    @Column(nullable = false)
    private String password;
    
    @Column(name = "job_title")
    private String jobTitle;
    
    @Column
    private String department;
    
    @Column
    private String company;
    
    @Column
    private String location;
    
    @Column(columnDefinition = "TEXT")
    private String bio;
    
    @Column(name = "profile_pic")
    private String profilePic;
    
    @Column
    private String plan;
    
    @Column(name = "member_since")
    private LocalDateTime memberSince;
    
    @Column(name = "last_login")
    private LocalDateTime lastLogin;
}
