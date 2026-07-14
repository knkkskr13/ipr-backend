package com.nic.ipr.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

@Entity
@Table(name = "ipr_notification")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class IprNotification {

    @Id
    @SequenceGenerator(name = "ipr_notification_sequence", sequenceName = "ipr_notification_sequence", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ipr_notification_sequence")
    private Long id;

    private String session;

    @Column(columnDefinition = "TEXT")
    private String message;

    private LocalDate startDate;
    private LocalDate endDate;
    private Boolean active;

    @ManyToOne
    @JoinColumn(name = "office_id", nullable = false,
            foreignKey = @ForeignKey(name = "fk_notification_office"))
    private Office office;
}