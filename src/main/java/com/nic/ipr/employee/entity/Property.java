package com.nic.ipr.employee.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "property")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Property {

    @Id
    @SequenceGenerator(name = "property_sequence", sequenceName = "property_sequence", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "property_sequence")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ipr_return_id", nullable = false, foreignKey = @ForeignKey(name = "fk_property_ipr_return"))
    @JsonIgnore
    private IprReturn iprReturn;

    private String locationAddress;
    private String propertyType;
    private String propertyDescription;
    private Double acquisitionCost;
    private String acquisitionYear;
    private Double presentValue;
    private String ownerName;
    private String ownerRelation;
    private String acquisitionMode;
    private String acquisitionDetails;
    private Double annualIncome;
    private String remarks;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @PrePersist
    public void prePersist() { createdAt = LocalDateTime.now(); }

    @PreUpdate
    public void preUpdate() { updatedAt = LocalDateTime.now(); }
}