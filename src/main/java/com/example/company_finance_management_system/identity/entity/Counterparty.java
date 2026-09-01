package com.example.company_finance_management_system.identity.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.time.OffsetDateTime;

@Entity
@Table(name = "counterparties", schema = "finance_management")
@Data
@Builder
public class Counterparty {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String inn;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CounterpartyType type;

    @CreationTimestamp
    @Column(nullable = false)
    private OffsetDateTime createdAt;

}
