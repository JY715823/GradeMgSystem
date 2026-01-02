package cn.edu.ctbu.gmsbackend.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Instant;

@Getter
@Setter
@Entity
public class PasswordResetRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private UserRole role;

    @Column(nullable = false, length = 64)
    private String username;

    @Column(nullable = false, length = 64)
    private String name;

    private String remark;

    @Column(nullable = false, length = 16)
    private String status = "PENDING";

    @CreationTimestamp
    private Instant createdAt;

    private Instant handledAt;

    private Long handledBy;
}
