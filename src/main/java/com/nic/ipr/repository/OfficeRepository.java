package com.nic.ipr.repository;

import com.nic.ipr.entity.Office;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface OfficeRepository extends JpaRepository<Office, Long> {
    List<Office> findByDepartmentId(Long departmentId);
}