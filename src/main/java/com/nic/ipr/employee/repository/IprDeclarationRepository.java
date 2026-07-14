package com.nic.ipr.employee.repository;

import com.nic.ipr.employee.entity.IprDeclaration;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface IprDeclarationRepository extends JpaRepository<IprDeclaration, Long> {
    Optional<IprDeclaration> findByIprReturnIprId(Long iprId);
}