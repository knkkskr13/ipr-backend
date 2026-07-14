package com.nic.ipr.repository;

import com.nic.ipr.entity.IprDeclaration;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface IprDeclarationRepository extends JpaRepository<IprDeclaration, Long> {
    Optional<IprDeclaration> findByIprReturnIprId(Long iprId);
    boolean existsByIprReturnIprId(Long iprId);
}