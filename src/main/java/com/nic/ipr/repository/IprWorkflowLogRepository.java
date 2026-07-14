package com.nic.ipr.repository;

import com.nic.ipr.entity.IprWorkflowLog;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface IprWorkflowLogRepository extends JpaRepository<IprWorkflowLog, Long> {
    List<IprWorkflowLog> findByIprReturnIprIdOrderByActionDateAsc(Long iprId);
}