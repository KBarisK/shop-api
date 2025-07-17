package com.staj.gib.shopapi.model;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "\"user\"")  // user is reserved in postgresql
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class User
{
    public enum UserType {
        CUSTOMER,
        ADMIN
    }

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "user_id")
    private UUID userId;

    private String username;
    private String password;

    @Column(name = "user_type")
    private UserType userType;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
