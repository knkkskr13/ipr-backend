package com.nic.ipr.employee.repository;

import com.nic.ipr.employee.entity.IprReturn;
import com.nic.ipr.shared.status.IprStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface IprReturnRepository extends JpaRepository<IprReturn, Long> {

    @Query("SELECT i FROM IprReturn i WHERE i.employee.id = ?1")
    List<IprReturn> findByEmployeeId(Long employeeId);

    boolean existsByEmployeeIdAndReportingYear(Long employeeId, String reportingYear);

    @Query("SELECT i FROM IprReturn i WHERE (i.employee.office.department.id = ?1 OR i.employee.department.id = ?1)")
    List<IprReturn> findByDepartmentId(Long departmentId);

    @Query("SELECT i FROM IprReturn i WHERE (i.employee.office.department.id = ?1 OR i.employee.department.id = ?1) AND i.status = ?2")
    List<IprReturn> findByDepartmentIdAndStatus(Long departmentId, IprStatus status);
}