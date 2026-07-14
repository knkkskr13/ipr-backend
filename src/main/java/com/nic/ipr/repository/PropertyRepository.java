package com.nic.ipr.repository;

import com.nic.ipr.entity.Property;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface PropertyRepository extends JpaRepository<Property, Long> {
    List<Property> findByIprReturnIprId(Long iprId);
}