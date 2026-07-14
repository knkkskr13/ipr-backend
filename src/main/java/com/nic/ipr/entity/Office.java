package com.nic.ipr.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Entity
@Table(name = "office")
@Data
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

    @OneToMany(mappedBy = "office", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<Employee> employees;
}