package com.nic.ipr.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "employee")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Employee {
    @Id
    @SequenceGenerator(
            name = "employeeSequence",
            sequenceName = "employee_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "employee_sequence"
    )
    private Long id;

    private String name;                  // Name of the Employee (in full)
    private String email;                 // for login purposes
    private String service;               // Service
    private String department;            // Department
    private String lengthOfService;       // Total Length of Services
    private String presentPostHeld;       // Present Post Held  ← rename designation to this
    private String placeOfPosting;        // Place of Posting



}
