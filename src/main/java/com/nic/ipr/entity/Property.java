package com.nic.ipr.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;

@Entity
@Table(name = "property")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Property {

    @Id
    @SequenceGenerator(name = "property_sequence", sequenceName = "property_sequence", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "property_sequence")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "ipr_return_id", nullable = false,
            foreignKey = @ForeignKey(name = "fk_property_ipr_return"))
    private IprReturn iprReturn;

    @Column(nullable = false)
    private String locationAddress;

    @Column(nullable = false)
    private String propertyType;

    private String propertyDescription;
    private BigDecimal acquisitionCost;
    private String acquisitionYear;
    private BigDecimal presentValue;
    private String ownerName;
    private String ownerRelation;
    private String acquisitionMode;
    private String acquisitionDetails;
    private BigDecimal annualIncome;
    private String remarks;
}