package com.nic.ipr.employee.repository;

import com.nic.ipr.employee.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    Optional<Employee> findByEmail(String email);

    // Dynamic search: Ignores parameters if they are passed as NULL
    @Query("SELECT e FROM Employee e WHERE " +
            "(:name IS NULL OR LOWER(e.name) LIKE LOWER(CONCAT('%', :name, '%'))) AND " +
            "(:email IS NULL OR LOWER(e.email) LIKE LOWER(CONCAT('%', :email, '%'))) AND " +
            "(:departmentId IS NULL OR e.department.id = :departmentId) AND " +
            "(:officeId IS NULL OR e.office.id = :officeId)")
    List<Employee> searchEmployees(@Param("name") String name,
                                   @Param("email") String email,
                                   @Param("departmentId") Long departmentId,
                                   @Param("officeId") Long officeId);
}