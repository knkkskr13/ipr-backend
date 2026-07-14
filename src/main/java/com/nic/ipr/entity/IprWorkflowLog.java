package com.nic.ipr.entity;

import com.nic.ipr.shared.enums.WorkflowAction;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "ipr_workflow_log")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class IprWorkflowLog {

    @Id
    @SequenceGenerator(name = "workflow_log_sequence", sequenceName = "workflow_log_sequence", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "workflow_log_sequence")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "ipr_return_id", nullable = false,
            foreignKey = @ForeignKey(name = "fk_log_ipr_return"))
    private IprReturn iprReturn;

    @Enumerated(EnumType.STRING)
    private WorkflowAction action;

    private Long actionByUserId;
    private String role;
    private String remarks;
    private LocalDateTime actionDate;
}