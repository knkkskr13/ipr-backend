package com.nic.ipr.employee.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "users", uniqueConstraints = {
        @UniqueConstraint(name = "uq_users_username", columnNames = "username"),
        @UniqueConstraint(name = "uq_users_employee_id", columnNames = "employee_id")
})
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class User {

    @Id
    @SequenceGenerator(name = "user_sequence", sequenceName = "user_sequence", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_sequence")
    private Long id;

    @Column(nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String role;

    @OneToOne
    @JoinColumn(name = "employee_id",
            foreignKey = @ForeignKey(name = "fk_user_employee"))
    @JsonIgnore
    private Employee employee;
}