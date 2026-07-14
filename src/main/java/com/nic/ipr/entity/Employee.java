package com.nic.ipr.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "employee")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Employee {

    @Id
    @SequenceGenerator(name = "employee_sequence", sequenceName = "employee_sequence", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "employee_sequence")
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = true)
    private String email;

    @Column(nullable = true)
    private String service;

    @Column(nullable = true)
    private String lengthOfService;

    @Column(nullable = true)
    private String presentPostHeld;

    @Column(nullable = true)
    private String placeOfPosting;

    @ManyToOne
    @JoinColumn(name = "office_id", nullable = false,
            foreignKey = @ForeignKey(name = "fk_employee_office"))
    private Office office;

    // Cascade delete — when an employee is deleted, remove their user account
    @OneToOne(mappedBy = "employee", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private User user;

    // Cascade delete — when an employee is deleted, remove all their IPR returns
    @OneToMany(mappedBy = "employee", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<IprReturn> iprReturns;
}