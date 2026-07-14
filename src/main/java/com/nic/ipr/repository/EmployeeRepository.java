package com.nic.ipr.repository;

import com.nic.ipr.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    List<Employee> findByOfficeId(Long officeId);
    List<Employee> findByOfficeDepartmentId(Long departmentId);
}