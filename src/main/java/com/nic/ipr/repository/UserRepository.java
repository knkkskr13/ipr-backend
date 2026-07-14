package com.nic.ipr.repository;

import com.nic.ipr.entity.User;
import com.nic.ipr.shared.enums.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
    boolean existsByUsername(String username);
    boolean existsByEmployeeId(Long employeeId);
    List<User> findByRole(Role role);
    Optional<User> findByEmployeeId(Long employeeId);

    boolean existsByRoleAndEmployeeOfficeDepartmentId(Role role, Long departmentId);
    boolean existsByRoleAndEmployeeOfficeDepartmentIdAndIdNot(Role role, Long departmentId, Long userId);
}