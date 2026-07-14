package com.nic.ipr.employee.repository;

import com.nic.ipr.employee.entity.IprWorkflowLog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IprWorkflowLogRepository extends JpaRepository<IprWorkflowLog, Long> {
    List<IprWorkflowLog> findByIprReturnIprId(Long iprId);
}