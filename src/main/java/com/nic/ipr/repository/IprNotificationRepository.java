package com.nic.ipr.repository;

import com.nic.ipr.entity.IprNotification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface IprNotificationRepository extends JpaRepository<IprNotification, Long> {

    Optional<IprNotification> findByActiveTrue();
}