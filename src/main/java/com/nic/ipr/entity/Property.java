package com.nic.ipr.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "property")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Property {

    @Id
    @SequenceGenerator(
            name = "property_sequence",
            sequenceName = "property_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "property_sequence"
    )
    private Long propertyId;

    @ManyToOne
    @JoinColumn(name = "ipr_id", nullable = false)
    private IprReturn iprReturn;

    private String locationAddress;
    private String propertyType;
    private String propertyDescription;

    private BigDecimal acquisitionCost;
    private Integer acquisitionYear;

    private BigDecimal presentValue;

    private String ownerName;
    private String ownerRelation;

    private String acquisitionMode;
    private String acquisitionDetails;

    private BigDecimal annualIncome;
    private String remarks;

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