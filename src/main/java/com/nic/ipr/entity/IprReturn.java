package com.nic.ipr.entity;

import com.nic.ipr.shared.enums.IprStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "ipr_return")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class IprReturn {

    @Id
    @SequenceGenerator(name = "ipr_return_sequence", sequenceName = "ipr_return_sequence", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ipr_return_sequence")
    private Long iprId;

    @ManyToOne
    @JoinColumn(name = "employee_id", nullable = false,
            foreignKey = @ForeignKey(name = "fk_ipr_return_employee"))
    private Employee employee;

    @Column(nullable = false)
    private String reportingYear;

    @Column(nullable = false)
    private LocalDate asOnDate;

    @Column(nullable = false)
    private BigDecimal totalAnnualIncome;

    @Column(nullable = false)
    private Boolean isNoProperty;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private IprStatus status;

    private LocalDateTime submittedAt;
    private LocalDateTime approvedAt;

    // Cascade delete — when an IprReturn is deleted, remove all its children
    @OneToMany(mappedBy = "iprReturn", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<Property> properties;

    @OneToMany(mappedBy = "iprReturn", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<IprWorkflowLog> workflowLogs;

    @OneToOne(mappedBy = "iprReturn", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private IprDeclaration declaration;
}