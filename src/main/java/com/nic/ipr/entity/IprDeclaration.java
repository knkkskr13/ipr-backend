package com.nic.ipr.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "ipr_declaration")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class IprDeclaration {

    @Id
    @SequenceGenerator(
            name = "declaration_sequence",
            sequenceName = "declaration_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "declaration_sequence"
    )
    private Long declarationId;

    @OneToOne
    @JoinColumn(name = "ipr_id", nullable = false)
    private IprReturn iprReturn;

    // "I hereby declare that the return enclosed is complete, true and correct"
    @Column(columnDefinition = "TEXT")
    private String declarationText;

    private Boolean agreed = false;

    private LocalDate declarationDate;
    private String place;
    private String employeeSignature;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @PrePersist
    public void prePersist() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    public void preUpdate() {
        updatedAt = LocalDateTime.now();
    }
}