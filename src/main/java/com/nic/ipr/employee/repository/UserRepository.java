package com.nic.ipr.employee.repository;

import com.nic.ipr.employee.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List; // <-- This was the missing piece!
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    // This single method now handles finding BOTH Employees and HODs for login!
    Optional<User> findByUsername(String username);

    List<User> findByRole(String role);

    boolean existsByUsername(String username);
}