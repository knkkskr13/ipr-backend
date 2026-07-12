package com.nic.ipr.hod.repository;

import com.nic.ipr.hod.entity.IprNotification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public interface IprNotificationRepository extends JpaRepository<IprNotification, Long> {
    Optional<IprNotification> findByOfficeIdAndActiveTrue(Long officeId);
    List<IprNotification> findByOfficeId(Long officeId);

    List<IprNotification> findByActiveTrue();

    List<IprNotification> findByOfficeDepartmentId(Long departmentId);
}