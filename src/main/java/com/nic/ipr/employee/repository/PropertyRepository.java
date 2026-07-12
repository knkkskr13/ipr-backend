package com.nic.ipr.employee.repository;

import com.nic.ipr.employee.entity.Property;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PropertyRepository extends JpaRepository<Property, Long> {
    List<Property> findByIprReturnIprId(Long iprId);
}