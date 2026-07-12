package com.nic.ipr.employee.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.nic.ipr.shared.status.WorkflowAction;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "ipr_workflow_log")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class IprWorkflowLog {

    @Id
    @SequenceGenerator(name = "workflow_log_sequence", sequenceName = "workflow_log_sequence", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "workflow_log_sequence")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ipr_return_id", nullable = false, foreignKey = @ForeignKey(name = "fk_workflow_log_ipr_return"))
    @JsonIgnore
    private IprReturn iprReturn;

    @Enumerated(EnumType.STRING)
    private WorkflowAction action;

    private Long actionByUserId;
    private String role;
    private String remarks;
    private LocalDateTime actionDate;

    @PrePersist
    public void prePersist() { actionDate = LocalDateTime.now(); }
}