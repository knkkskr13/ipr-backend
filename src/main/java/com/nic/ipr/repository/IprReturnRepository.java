package com.nic.ipr.repository;

import com.nic.ipr.entity.IprReturn;
import com.nic.ipr.status.IprStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface IprReturnRepository extends JpaRepository<IprReturn, Long> {

    // JpaRepository<IprReturn, Long>
    // IprReturn → which table/entity am I working with
    // Long      → type of the primary key (iprId)

    // Get all IPR returns for a specific employee
    @Query("SELECT i FROM IprReturn i WHERE i.employee.id = ?1")
    List<IprReturn> findByEmployeeId(Long employeeId);

    // Get specific year's IPR return for an employee
    // SELECT i FROM IprReturn i WHERE i.employee.id = ?1 AND i.reportingYear = ?2
    @Query("SELECT i FROM IprReturn i WHERE i.employee.id = ?1 AND i.reportingYear = ?2")
    Optional<IprReturn> findByEmployeeIdAndReportingYear(Long employeeId, String reportingYear);

    // Get all IPR returns by status — e.g. all SUBMITTED ones for admin
    // SELECT i FROM IprReturn i WHERE i.status = ?1
    @Query("SELECT i FROM IprReturn i WHERE i.status = ?1")
    List<IprReturn> findByStatus(IprStatus status);
}