package com.nic.ipr.repository;

import com.nic.ipr.entity.IprDeclaration;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IprDeclarationRepository extends JpaRepository<IprDeclaration, Long> {

    // JpaRepository<IprDeclaration, Long>
    // IprDeclaration → which table/entity am I working with
    // Long           → type of the primary key (declarationId)

    // Get declaration for a specific IPR return
    @Query("SELECT d FROM IprDeclaration d WHERE d.iprReturn.iprId = ?1")
    Optional<IprDeclaration> findByIprReturnIprId(Long iprId);

}