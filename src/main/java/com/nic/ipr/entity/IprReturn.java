package com.nic.ipr.entity;

import com.nic.ipr.status.IprStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "ipr_return",
        uniqueConstraints = @UniqueConstraint(columnNames = {"employee_id", "reporting_year"}))
@Data
@AllArgsConstructor
@NoArgsConstructor
public class IprReturn {

    @Id
    @SequenceGenerator(
            name = "ipr_sequence",
            sequenceName = "ipr_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "ipr_sequence"
    )
    private Long iprId;// keep track of different ipr returns, may be of same employee_id or different

    @ManyToOne // because a employee will have different ipr returns every year
    @JoinColumn(name = "employee_id", nullable = false)// creates a employee_id column for keeping track of employee id
    private Employee employee;

    private String reportingYear;       // e.g. 2025-26
    private LocalDate asOnDate;         // e.g. 31/12/2025
    private BigDecimal totalAnnualIncome;
    private Boolean isNoProperty = false; // true if employee has no property

    @Enumerated(EnumType.STRING)
    private IprStatus status = IprStatus.DRAFT;  // DRAFT/SUBMITTED/APPROVED/RETURNED

    private LocalDateTime submittedAt;
    private LocalDateTime approvedAt;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // @PrePersist → runs automatically BEFORE a new record is INSERT into DB
    @PrePersist
    public void prePersist() {
        createdAt = LocalDateTime.now(); // set creation time when first saved
        updatedAt = LocalDateTime.now(); // set updatedAt same as createdAt on creation
    }

    // @PreUpdate → runs automatically BEFORE an existing record is UPDATE in DB
    @PreUpdate
    public void preUpdate() {
        updatedAt = LocalDateTime.now(); // every time record is updated, refresh this timestamp
    }
}