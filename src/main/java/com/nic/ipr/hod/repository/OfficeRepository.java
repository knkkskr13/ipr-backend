package com.nic.ipr.hod.repository;

import com.nic.ipr.hod.entity.Office;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OfficeRepository extends JpaRepository<Office, Long> {
    List<Office> findByDepartmentId(Long departmentId);
    boolean existsByNameAndDepartmentId(String name, Long departmentId);
}