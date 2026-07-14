package com.nic.ipr.hod.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "ipr_notification")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class IprNotification {

    @Id
    @SequenceGenerator(name = "notification_sequence", sequenceName = "notification_sequence", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "notification_sequence")
    private Long id;

    private String session;
    private String message;
    private LocalDate startDate;
    private LocalDate endDate;
    private Boolean active = true;

    @ManyToOne
    @JoinColumn(name = "office_id", nullable = false,
            foreignKey = @ForeignKey(name = "fk_notification_office"))
    private Office office;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @PrePersist
    public void prePersist() { createdAt = LocalDateTime.now(); }

    @PreUpdate
    public void preUpdate() { updatedAt = LocalDateTime.now(); }
}