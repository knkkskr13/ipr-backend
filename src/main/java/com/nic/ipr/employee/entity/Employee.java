package com.nic.ipr.employee.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.nic.ipr.hod.entity.Department;
import com.nic.ipr.hod.entity.Office;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import java.util.List;

@Entity
@Table(name = "employee")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Employee {

    @Id
    @SequenceGenerator(name = "employee_sequence", sequenceName = "employee_sequence", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "employee_sequence")
    private Long id;

    private String name;
    private String email;
    private String service;
    private String lengthOfService;
    private String presentPostHeld;
    private String placeOfPosting;

    // Standard employee office link
    @ManyToOne
    @JoinColumn(name = "office_id",
            foreignKey = @ForeignKey(name = "fk_employee_office"))
    private Office office;

    @ManyToOne
    @JoinColumn(name = "department_id",
            nullable = false,
            foreignKey = @ForeignKey(name = "fk_employee_department"))
    private Department department;

    @JsonIgnore
    @OneToMany(mappedBy = "employee", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<IprReturn> iprReturns;

    @JsonIgnore
    @OneToOne(mappedBy = "employee", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private User user;
}