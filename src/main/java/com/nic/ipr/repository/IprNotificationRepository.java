package com.nic.ipr.repository;

import com.nic.ipr.entity.IprNotification;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface IprNotificationRepository extends JpaRepository<IprNotification, Long> {
    List<IprNotification> findByOfficeId(Long officeId);
    List<IprNotification> findByActive(Boolean active);
    Optional<IprNotification> findByOfficeIdAndActiveTrue(Long officeId);
    boolean existsByOfficeIdAndActiveTrue(Long officeId);
}