package com.example.company_finance_management_system.identity.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.OffsetDateTime;

@Entity
@Table(name = "sessions", schema = "finance_management")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class Session {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(nullable = false)
    private String refreshTokenHash;

    @Column(nullable = false)
    private Boolean revoked;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private OffsetDateTime createdAt;

    @Column(nullable = false, updatable = false)
    private OffsetDateTime expiresAt;

}
