package com.nic.ipr.hod.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.nic.ipr.employee.entity.Employee;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "office")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Office {

    @Id
    @SequenceGenerator(name = "office_sequence", sequenceName = "office_sequence", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "office_sequence")
    private Long id;

    @Column(nullable = false)
    private String name;

    @ManyToOne
    @JoinColumn(name = "department_id", nullable = false,
            foreignKey = @ForeignKey(name = "fk_office_department"))
    private Department department;

    @JsonIgnore
    @OneToMany(mappedBy = "office", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<Employee> employees;
}