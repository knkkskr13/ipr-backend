package com.nic.ipr.repository;

import com.nic.ipr.entity.Property;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PropertyRepository extends JpaRepository<Property, Long> {

    // JpaRepository<IprPropertyDetail, Long>
    // IprPropertyDetail → which table/entity am I working with
    // Long              → type of the primary key (propertyId)

    // Get all properties belonging to a specific IPR return
    @Query("SELECT p FROM Property p WHERE p.iprReturn.iprId = ?1")
    List<Property> findByIprReturnIprId(Long iprId);

}