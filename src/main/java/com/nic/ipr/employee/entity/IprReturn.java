package com.nic.ipr.employee.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.nic.ipr.shared.status.IprStatus;
import jakarta.persistence.*;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "ipr_return")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class IprReturn {

    @Id
    @SequenceGenerator(name = "ipr_sequence", sequenceName = "ipr_sequence", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ipr_sequence")
    private Long iprId;

    @ManyToOne
    @JoinColumn(name = "employee_id", nullable = false,
            foreignKey = @ForeignKey(name = "fk_ipr_return_employee"))
    private Employee employee;

    private String reportingYear;
    private LocalDate asOnDate;
    private Double totalAnnualIncome;
    private Boolean isNoProperty;

    @Enumerated(EnumType.STRING)
    private IprStatus status;

    private LocalDateTime submittedAt;
    private LocalDateTime approvedAt;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @PrePersist
    public void prePersist() { createdAt = LocalDateTime.now(); }

    @PreUpdate
    public void preUpdate() { updatedAt = LocalDateTime.now(); }

    @JsonIgnore
    @OneToMany(mappedBy = "iprReturn", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<Property> properties;

    @JsonIgnore
    @OneToMany(mappedBy = "iprReturn", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<IprWorkflowLog> workflowLogs;

    @JsonIgnore
    @OneToOne(mappedBy = "iprReturn", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private IprDeclaration declaration;
}