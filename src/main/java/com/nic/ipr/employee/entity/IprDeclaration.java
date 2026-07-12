package com.nic.ipr.employee.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "ipr_declaration", uniqueConstraints = {
        @UniqueConstraint(name = "uq_declaration_ipr_return_id", columnNames = "ipr_return_id")
})
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class IprDeclaration {

    @Id
    @SequenceGenerator(name = "declaration_sequence", sequenceName = "declaration_sequence", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "declaration_sequence")
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ipr_return_id", nullable = false, foreignKey = @ForeignKey(name = "fk_declaration_ipr_return"))
    @JsonIgnore
    private IprReturn iprReturn;

    @Column(columnDefinition = "TEXT")
    private String declarationText;
    private Boolean agreed;
    private LocalDate declarationDate;
    private String place;
    private String employeeSignature;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @PrePersist
    public void prePersist() { createdAt = LocalDateTime.now(); }

    @PreUpdate
    public void preUpdate() { updatedAt = LocalDateTime.now(); }
}