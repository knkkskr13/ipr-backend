package com.nic.ipr.repository;

import com.nic.ipr.entity.IprReturn;
import com.nic.ipr.shared.enums.IprStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IprReturnRepository extends JpaRepository<IprReturn, Long> {
    List<IprReturn> findByEmployeeId(Long employeeId);
    List<IprReturn> findByEmployeeOfficeDepartmentId(Long departmentId);
    List<IprReturn> findByStatusAndEmployeeOfficeDepartmentId(IprStatus status, Long departmentId);
    List<IprReturn> findByStatus(IprStatus status);
    boolean existsByEmployeeIdAndReportingYear(Long employeeId, String reportingYear);
}